package investigation.hub.test.core;

import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.core.util.Retry;
import investigation.hub.common.web.components.AmlPageComponent;
import investigation.hub.common.web.components.OpenDetectionsPageComponent;
import investigation.hub.common.web.components.modals.GeneratedByAiModalComponent;
import investigation.hub.common.web.components.modals.LeftFloatMenuInvestigationModalComponent;
import investigation.hub.common.web.components.tables.HistoricalDetectionsTablePageComponent;
import investigation.hub.common.web.components.tables.InvestigationsTablePageComponent;
import investigation.hub.common.web.components.tables.OpenDetectionsTablePageComponent;
import investigation.hub.common.web.components.tables.TableGeneralComponent;
import investigation.hub.common.web.pages.HelpPage;
import investigation.hub.common.web.pages.InvestigationPage;
import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import org.assertj.core.api.SoftAssertions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.*;

public class OpenDetectionsTest extends InvHubUiTest {

    InvestigationPage investigationPage;
    InvestigationsTablePageComponent investigationsTablePageComponent;
    AmlPageComponent amlPageComponent;
    OpenDetectionsPageComponent openDetectionsPageComponent;
    GeneratedByAiModalComponent generatedByAiModalComponent = new GeneratedByAiModalComponent();
    HelpPage helpPage = new HelpPage();


    @BeforeMethod
    public void openPage() {
        open(apiProperties.getUrl() + apiProperties.getOpenInvestigations());
        openInvestigationsPage.waitForLoad();

        investigationsTablePageComponent = openInvestigationsPage
                .clickAllWorkButton()
                .getInvestigationsTable();

     //   String subjectId = "REF-CUSTEST-LIST-87-0107";
         String subjectId = investigationsTablePageComponent
             .getRandomSubjectId();
        String headerName = InvestigationsTablePageComponent.HeaderName.SUBJECT_ID.getStringValue();
        investigationsTablePageComponent
                .getFilterPageComponent()
                .clickColumnFilterIcon(headerName)
                .enterFilterValue(headerName, subjectId);

        investigationPage = investigationsTablePageComponent.clickRowBySubjectId(subjectId);
        amlPageComponent =
                investigationPage
                        .getLeftMenuComponent()
                        .clickMenuItemByName(LeftFloatMenuInvestigationModalComponent.LeftMenuItemName.DETECTIONS);
        openDetectionsPageComponent = amlPageComponent.getOpenDetectionsPageComponent();
    }

    @TmsLink("INVHUB-1530")
    @Test(retryAnalyzer = Retry.class, description = "Verify user can mark selected Detections as Unusual")
    public void checkDetectionCanBeMarkedAsUnusualTest() {
        investigationPage.switchSubjectOverviewCheckbox();

        OpenDetectionsTablePageComponent openDetectionsTablePageComponent = openDetectionsPageComponent
                .getOpenDetectionsTablePageComponent();
        Map<String, String> availableAmlDetection = new HashMap<>();
        availableAmlDetection.put(OpenDetectionsTablePageComponent.HeaderName.SCENARIO_NAME.getStringValue(),
                openDetectionsTablePageComponent
                        .getAnyColumnValue(OpenDetectionsTablePageComponent.HeaderName.SCENARIO_NAME));

        openDetectionsTablePageComponent
                .selectRowByRowData(availableAmlDetection)
                .clickUnusualButton();

        availableAmlDetection.put(HistoricalDetectionsTablePageComponent.HeaderName.OUTCOME.getStringValue(), "UNUSUAL");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(openDetectionsPageComponent.isSelectedDetectionMarkedAsUnusualMessageAppeared(),
                "Selected Detections marked as Unusual message should appear");

        HistoricalDetectionsTablePageComponent historicalDetectionsTablePageComponent = amlPageComponent
                .clickHistoricalDetectionsButton()
                .getHistoricalDetectionsTablePageComponent();

        softAssert.assertNotNull(historicalDetectionsTablePageComponent.getRowByContent(availableAmlDetection),
                "Marked Row should be present in Historical detections table");
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1576")
    @Test(retryAnalyzer = Retry.class, description = "Verify user can mark selected Detections as Not Unusual")
    public void checkDetectionCanBeMarkAsNotUnusualTest() {
        investigationPage.switchSubjectOverviewCheckbox();

        OpenDetectionsTablePageComponent openDetectionsTablePageComponent = openDetectionsPageComponent
                .getOpenDetectionsTablePageComponent();
        Map<String, String> availableAmlDetection = new HashMap<>();

        availableAmlDetection.put(OpenDetectionsTablePageComponent.HeaderName.SCENARIO_NAME.getStringValue(),
                openDetectionsTablePageComponent
                        .getAnyColumnValue(OpenDetectionsTablePageComponent.HeaderName.SCENARIO_NAME));

        openDetectionsTablePageComponent
                .selectRowByRowData(availableAmlDetection)
                .clickNotUnusualButton();

        availableAmlDetection.put(HistoricalDetectionsTablePageComponent.HeaderName.OUTCOME.getStringValue(), "NOT UNUSUAL");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(openDetectionsPageComponent.isSelectedDetectionMarkedAsNotUnusualMessageAppeared(),
                "Selected Detections marked as Not Unusual message should appear");

        HistoricalDetectionsTablePageComponent historicalDetectionsTablePageComponent = amlPageComponent
                .clickHistoricalDetectionsButton()
                .getHistoricalDetectionsTablePageComponent();

        softAssert.assertNotNull(historicalDetectionsTablePageComponent.getRowByContent(availableAmlDetection),
                "Marked Row should be present in Historical detections table");
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1717")
    @Test(retryAnalyzer = Retry.class, description = "Verify open detections can be sorted by 'Time Period' column")
    public void checkTimePeriodColumnIsSortableTest() {
        sortTableColumnsAndCheck(
                OpenDetectionsTablePageComponent.HeaderName.TIME_PERIOD,
                TableGeneralComponent.SortStatus.ASCENDING);
    }

    @TmsLink("INVHUB-1718")
    @Test(retryAnalyzer = Retry.class, description = "Verify open detections can be sorted by 'Scenario Name' column")
    public void checkScenarioNameColumnIsSortableTest() {
        sortTableColumnsAndCheck(
                OpenDetectionsTablePageComponent.HeaderName.SCENARIO_NAME,
                TableGeneralComponent.SortStatus.DESCENDING);
    }

    @TmsLink("INVHUB-1719")
    @Test(retryAnalyzer = Retry.class, description = "Verify open detections can be sorted by 'Event Date' column")
    public void checkEventDateColumnIsSortableTest() {
        sortTableDateColumnsAndCheck(
                OpenDetectionsTablePageComponent.HeaderName.EVENT_DATE,
                TableGeneralComponent.SortStatus.ASCENDING);
    }

    @TmsLink("INVHUB-1720")
    @Test(retryAnalyzer = Retry.class, description = "Verify open detections can be sorted by 'Due Date' column")
    public void checkDueDateColumnIsSortableTest() {
        sortTableDateColumnsAndCheck(
                OpenDetectionsTablePageComponent.HeaderName.DUE_DATE,
                TableGeneralComponent.SortStatus.DESCENDING);
    }

    @TmsLink("INVHUB-1728")
    @Test(description = "Verify open detections can be filtered by 'Time Period' column")
    public void checkDetectionsCanBeFilteredByTimePeriodTest() {
        OpenDetectionsTablePageComponent.HeaderName header = OpenDetectionsTablePageComponent.HeaderName.TIME_PERIOD;

        OpenDetectionsTablePageComponent openDetectionsTablePageComponent = openDetectionsPageComponent
                .getOpenDetectionsTablePageComponent();

        filterTableColumnsAndCheck(openDetectionsTablePageComponent, header);
    }

    @TmsLink("INVHUB-1729")
    @Test(retryAnalyzer = Retry.class, description = "Verify open detections can be filtered by 'Scenario Name' column")
    public void checkDetectionsCanBeFilteredByScenarioNameTest() {
        OpenDetectionsTablePageComponent.HeaderName header = OpenDetectionsTablePageComponent.HeaderName.SCENARIO_NAME;

        OpenDetectionsTablePageComponent openDetectionsTablePageComponent = openDetectionsPageComponent
                .getOpenDetectionsTablePageComponent();

        filterTableColumnsAndCheck(openDetectionsTablePageComponent, header);
    }

    @Issue("INVHUB-2502")
    @Issue("INVHUB-2503")
    @TmsLink("INVHUB-1730")
    @Test(retryAnalyzer = Retry.class, description = "Verify open detections can be filtered by 'Event Date' column")
    public void checkDetectionsCanBeFilteredByEventDateTest() {
        OpenDetectionsTablePageComponent.HeaderName header = OpenDetectionsTablePageComponent.HeaderName.EVENT_DATE;

        OpenDetectionsTablePageComponent openDetectionsTablePageComponent = openDetectionsPageComponent
                .getOpenDetectionsTablePageComponent();

        filterTableDateColumnsAndCheck(openDetectionsTablePageComponent, header);
    }

    @Issue("INVHUB-2502")
    @Issue("INVHUB-2503")
    @TmsLink("INVHUB-1731")
    @Test(retryAnalyzer = Retry.class, description = "Verify open detections can be filtered by 'Due Date' column")
    public void checkDetectionsCanBeFilteredByDueDateTest() {
        OpenDetectionsTablePageComponent.HeaderName header = OpenDetectionsTablePageComponent.HeaderName.DUE_DATE;

        OpenDetectionsTablePageComponent openDetectionsTablePageComponent = openDetectionsPageComponent
                .getOpenDetectionsTablePageComponent();

        filterTableDateColumnsAndCheck(openDetectionsTablePageComponent, header);
    }

    @TmsLink("INVHUB-3770")
    @Test(retryAnalyzer = Retry.class, description = "Verify 'Show AI' provides info about the Copilot work on the Detections page")
    public void checkShowAiProvidesInfoAboutCopilotWorkForDetectionsTest() {
        openDetectionsPageComponent
                .clickGeneratedByAiDisclaimerLink()
                .clickOpenDocumentationLink();

        switchTo().window(1);
        helpPage.waitForLoad();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(helpPage.isPageTitleVisible())
                .as("The 'Help' page main title should be visible")
                .isTrue();
        closeWindow();
        switchTo().window(0);

        generatedByAiModalComponent.clickGotItButton();
        softly.assertThat(generatedByAiModalComponent.isGeneratedByAiModalComponentDisplayed())
                .as("The 'Generated by Ai' modal dialog hasn't been closed")
                .isFalse();
        softly.assertAll();
    }

    private void filterTableColumnsAndCheck(OpenDetectionsTablePageComponent openDetectionsTablePageComponent,
                                            OpenDetectionsTablePageComponent.HeaderName header) {
        String randomColumnValue = openDetectionsTablePageComponent.getAnyColumnValue(header);
        openDetectionsTablePageComponent.getFilterPageComponent()
                .clickColumnFilterIcon(header.getStringValue())
                .enterFilterValue(header.getStringValue(), randomColumnValue);

        Assert.assertTrue(
                openDetectionsTablePageComponent.getColumnData(header.getStringValue())
                        .stream()
                        .allMatch(it -> it.equals(randomColumnValue)),
                "Open Detections should be filtered by + " + header.getStringValue() + " column correctly");
    }

    private void filterTableDateColumnsAndCheck(OpenDetectionsTablePageComponent openDetectionsTablePageComponent,
                                                OpenDetectionsTablePageComponent.HeaderName header) {
        String randomColumnValue = openDetectionsTablePageComponent.getAnyColumnValue(header);
        openDetectionsTablePageComponent.getFilterPageComponent()
                .clickColumnFilterIcon(header.getStringValue())
                .enterFilterDateValue(randomColumnValue, randomColumnValue,
                        openDetectionsTablePageComponent.getFormatter());

        Assert.assertTrue(
                openDetectionsTablePageComponent.getFilterPageComponent()
                        .isDateColumnFiltered(openDetectionsTablePageComponent.getColumnData(header.getStringValue()),
                                randomColumnValue, randomColumnValue,
                                openDetectionsTablePageComponent.getFormatter()),
                "Open Detections should be filtered by " + header.getStringValue() + " column correctly");
    }

    private void sortTableColumnsAndCheck(OpenDetectionsTablePageComponent.HeaderName headerName,
                                          TableGeneralComponent.SortStatus sortType) {

        OpenDetectionsTablePageComponent openDetectionsTablePageComponent = openDetectionsPageComponent
                .getOpenDetectionsTablePageComponent()
                .sortTable(headerName, sortType);

        List<String> actualColumnData = openDetectionsTablePageComponent.getColumnData(headerName.getStringValue());
        Assert.assertEquals(actualColumnData,
                openDetectionsTablePageComponent.getListByDirection(
                        headerName.getStringValue(),
                        sortType),
                headerName.getStringValue() + " сolumn should be sorted properly, sort type is: " + sortType);
    }

    private void sortTableDateColumnsAndCheck(OpenDetectionsTablePageComponent.HeaderName headerName,
                                              TableGeneralComponent.SortStatus sortType) {

        OpenDetectionsTablePageComponent openDetectionsTablePageComponent = openDetectionsPageComponent
                .getOpenDetectionsTablePageComponent()
                .sortTable(headerName, sortType);

        List<String> actualColumnData = openDetectionsTablePageComponent.getColumnData(headerName.getStringValue());
        Assert.assertEquals(actualColumnData,
                openDetectionsTablePageComponent.getDateListByDirection(
                        openDetectionsTablePageComponent.getFormatter(),
                        headerName.getStringValue(),
                        sortType),
                headerName.getStringValue() + " сolumn should be sorted properly, sort type is: " + sortType);
    }
}
