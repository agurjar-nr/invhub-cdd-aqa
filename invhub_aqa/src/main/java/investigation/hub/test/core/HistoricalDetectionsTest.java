package investigation.hub.test.core;

import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.core.util.Retry;
import investigation.hub.common.web.components.AmlPageComponent;
import investigation.hub.common.web.components.TransactionsPageComponent;
import investigation.hub.common.web.components.modals.LeftFloatMenuInvestigationModalComponent;
import investigation.hub.common.web.components.tables.HistoricalDetectionsTablePageComponent;
import investigation.hub.common.web.components.tables.InvestigationsTablePageComponent;
import investigation.hub.common.web.components.tables.TableGeneralComponent;
import investigation.hub.common.web.components.tables.TransactionsTablePageComponent;
import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;

public class HistoricalDetectionsTest extends InvHubUiTest {

    AmlPageComponent amlPageComponent;
    InvestigationsTablePageComponent investigationsTablePageComponent;
    HistoricalDetectionsTablePageComponent historicalDetectionsTablePageComponent;

    private static DateTimeFormatter getFormatter(HistoricalDetectionsTablePageComponent.HeaderName headerName,
                                                  HistoricalDetectionsTablePageComponent historicalDetectionsTablePageComponent) {
        DateTimeFormatter formatter;
        if (headerName.equals(HistoricalDetectionsTablePageComponent.HeaderName.EVENT_DATE)) {
            formatter = historicalDetectionsTablePageComponent.getEventDateColumnFormatter();
        } else if (headerName.equals(HistoricalDetectionsTablePageComponent.HeaderName.CLOSED_ON)) {
            formatter = historicalDetectionsTablePageComponent.getClosedOnColumnFormatter();
        } else {
            throw new IllegalArgumentException("Only 'Event Date' and 'Closed On' columns are possible");
        }
        return formatter;
    }

    @BeforeMethod
    public void openPage() {
        open(apiProperties.getUrl() + apiProperties.getOpenInvestigations());
        openInvestigationsPage.waitForLoad();

        investigationsTablePageComponent = openInvestigationsPage
                .clickAllWorkButton()
                .getInvestigationsTable();

        String subjectId = "AUTO-SUBJ-10"; //TODO: to be updated, when INVHUB-3194 is fixed

        amlPageComponent = investigationsTablePageComponent
                .clickRowBySubjectId(subjectId)
                .getLeftMenuComponent()
                .clickMenuItemByName(LeftFloatMenuInvestigationModalComponent.LeftMenuItemName.DETECTIONS);

        historicalDetectionsTablePageComponent = amlPageComponent
                .clickHistoricalDetectionsButton()
                .getHistoricalDetectionsTablePageComponent();
    }

    @TmsLink("INVHUB-1528")
    @Test(retryAnalyzer = Retry.class, description = "Verify user can include transaction in Narrative")
    public void checkTransactionCanBeIncludedToNarrativeTest() {
        Map<String, String> amlDetection = Map.of(
                HistoricalDetectionsTablePageComponent.HeaderName.SCENARIO_NAME.getStringValue(),
                historicalDetectionsTablePageComponent
                        .getAnyColumnValue(HistoricalDetectionsTablePageComponent.HeaderName.SCENARIO_NAME)
        );
        TransactionsPageComponent transactionsPageComponent =
                historicalDetectionsTablePageComponent.clickExpandRowIconByRowData(amlDetection);
        Map<String, String> amlDetectionTransaction = Map.of(
                TransactionsTablePageComponent.HeaderName.TRANSACTION_ID.getStringValue(),
                transactionsPageComponent.getTransactionsTablePageComponent()
                        .getAnyColumnValue(TransactionsTablePageComponent.HeaderName.TRANSACTION_ID)
        );
        transactionsPageComponent.getTransactionsTablePageComponent()
                .selectCheckboxByRowData(amlDetectionTransaction)
                .clickAddToNarrativeButton();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(amlPageComponent.isTransactionIncludedInNarrativeMessageAppeared(),
                "1 transaction included in narrative message should appear");
        softAssert.assertTrue(transactionsPageComponent.getEmbeddedTransactionsTablePageComponent()
                .isTransactionRowAddedToNarrative(amlDetectionTransaction), "Narrow icon should change opacity");
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1529")
    @Test(retryAnalyzer = Retry.class, description = "Verify user can exclude transaction from Narrative")
    public void checkTransactionCanBeExcludedFromNarrativeTest() {
        Map<String, String> amlDetection = Map.of(
                HistoricalDetectionsTablePageComponent.HeaderName.SCENARIO_NAME.getStringValue(),
                historicalDetectionsTablePageComponent
                        .getAnyColumnValue(HistoricalDetectionsTablePageComponent.HeaderName.SCENARIO_NAME)
        );
        TransactionsPageComponent transactionsPageComponent =
                historicalDetectionsTablePageComponent.clickExpandRowIconByRowData(amlDetection);
        Map<String, String> amlDetectionTransaction = Map.of(
                TransactionsTablePageComponent.HeaderName.TRANSACTION_ID.getStringValue(),
                transactionsPageComponent.getTransactionsTablePageComponent()
                        .getAnyColumnValue(TransactionsTablePageComponent.HeaderName.TRANSACTION_ID)
        );
        transactionsPageComponent.getTransactionsTablePageComponent()
                .selectCheckboxByRowData(amlDetectionTransaction)
                .clickRemoveFromNarrativeButton();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(amlPageComponent.isTransactionExcludedFromNarrativeMessageAppeared(),
                "1 transaction excluded from narrative message should appear");
        softAssert.assertTrue(transactionsPageComponent.getEmbeddedTransactionsTablePageComponent()
                .isTransactionRowRemovedFromNarrative(amlDetectionTransaction), "Narrow icon should change opacity");
        softAssert.assertAll();
    }

    @Issue("INVHUB-3194")
    @TmsLink("INVHUB-1723")
    @Test(retryAnalyzer = Retry.class, description = "Verify historical detections can be sorted by 'Time Period' column")
    public void checkTimePeriodColumnIsSortableTest() {
        sortTableColumnsAndCheck(
                HistoricalDetectionsTablePageComponent.HeaderName.TIME_PERIOD,
                TableGeneralComponent.SortStatus.ASCENDING);
    }

    @Issue("INVHUB-3194")
    @TmsLink("INVHUB-1724")
    @Test(retryAnalyzer = Retry.class, description = "Verify historical detections can be sorted by 'Scenario Name' column")
    public void checkScenarioNameColumnIsSortableTest() {
        sortTableColumnsAndCheck(
                HistoricalDetectionsTablePageComponent.HeaderName.SCENARIO_NAME,
                TableGeneralComponent.SortStatus.DESCENDING);
    }

    @Issue("INVHUB-3194")
    @TmsLink("INVHUB-1725")
    @Test(retryAnalyzer = Retry.class, description = "Verify historical detections can be sorted by 'Event Date' column")
    public void checkEventDateColumnIsSortableTest() {
        sortTableDateColumnsAndCheck(
                HistoricalDetectionsTablePageComponent.HeaderName.EVENT_DATE,
                TableGeneralComponent.SortStatus.ASCENDING);
    }

    @Issue("INVHUB-3194")
    @TmsLink("INVHUB-1726")
    @Test(retryAnalyzer = Retry.class, description = "Verify historical detections can be sorted by 'Outcome' column")
    public void checkOutcomeColumnIsSortableTest() {
        sortTableColumnsAndCheck(
                HistoricalDetectionsTablePageComponent.HeaderName.OUTCOME,
                TableGeneralComponent.SortStatus.DESCENDING);
    }

    @Issue("INVHUB-3194")
    @TmsLink("INVHUB-1727")
    @Test(retryAnalyzer = Retry.class, description = "Verify historical detections can be sorted by 'Closed On' column")
    public void checkClosedOnColumnIsSortableTest() {
        sortTableDateColumnsAndCheck(
                HistoricalDetectionsTablePageComponent.HeaderName.CLOSED_ON,
                TableGeneralComponent.SortStatus.ASCENDING);
    }

    @Issue("INVHUB-3194")
    @TmsLink("INVHUB-1732")
    @Test(retryAnalyzer = Retry.class, description = "Verify historical detections can be filtered by 'Time Period' column")
    public void checkDetectionsCanBeFilteredByTimePeriodTest() {
        filterTableColumnsAndCheck(
                historicalDetectionsTablePageComponent,
                HistoricalDetectionsTablePageComponent.HeaderName.TIME_PERIOD);
    }

    @Issue("INVHUB-3194")
    @TmsLink("INVHUB-1733")
    @Test(retryAnalyzer = Retry.class, description = "Verify historical detections can be filtered by 'Scenario Name' column")
    public void checkDetectionsCanBeFilteredByScenarioNameTest() {
        filterTableColumnsAndCheck(
                historicalDetectionsTablePageComponent,
                HistoricalDetectionsTablePageComponent.HeaderName.SCENARIO_NAME);
    }

    @Issue("INVHUB-3194")
    @Issue("INVHUB-2502")
    @Issue("INVHUB-2503")
    @TmsLink("INVHUB-1734")
    @Test(retryAnalyzer = Retry.class, description = "Verify historical detections can be filtered by 'Event Date' column")
    public void checkDetectionsCanBeFilteredByEventDateTest() {
        filterTableDateColumnsAndCheck(
                historicalDetectionsTablePageComponent,
                HistoricalDetectionsTablePageComponent.HeaderName.EVENT_DATE,
                historicalDetectionsTablePageComponent.getEventDateColumnFormatter());
    }

    @Issue("INVHUB-3194")
    @TmsLink("INVHUB-1735")
    @Test(retryAnalyzer = Retry.class, description = "Verify historical detections can be filtered by 'Outcome' column")
    public void checkDetectionsCanBeFilteredByOutcomeTest() {
        filterTableColumnsAndCheck(
                historicalDetectionsTablePageComponent,
                HistoricalDetectionsTablePageComponent.HeaderName.OUTCOME);
    }

    @Issue("INVHUB-3194")
    @TmsLink("INVHUB-1736")
    @Test(retryAnalyzer = Retry.class, description = "Verify historical detections can be filtered by 'Closed On' column")
    public void checkDetectionsCanBeFilteredByClosedOnTest() {
        filterTableDateColumnsAndCheck(
                historicalDetectionsTablePageComponent,
                HistoricalDetectionsTablePageComponent.HeaderName.CLOSED_ON,
                historicalDetectionsTablePageComponent.getClosedOnColumnFormatter());
    }

    private void filterTableColumnsAndCheck(HistoricalDetectionsTablePageComponent historicalDetectionsTablePageComponent,
                                            HistoricalDetectionsTablePageComponent.HeaderName header) {
        String randomColumnValue = historicalDetectionsTablePageComponent.getAnyColumnValue(header).toLowerCase();
        String columnOption = StringUtils.capitalize(randomColumnValue.toLowerCase());
        String headerName = header.getStringValue();
        historicalDetectionsTablePageComponent.getFilterPageComponent()
                .clickColumnFilterIcon(headerName)
                .enterFilterValue(headerName.toLowerCase(), StringUtils.capitalize(columnOption));
        Assert.assertTrue(
                historicalDetectionsTablePageComponent.getColumnData(headerName)
                        .stream()
                        .allMatch(it -> it.equals(randomColumnValue)),
                "Previous AML Detections should be filtered by %s column correctly".formatted(headerName));
    }

    private void filterTableDateColumnsAndCheck(HistoricalDetectionsTablePageComponent historicalDetectionsTablePageComponent,
                                                HistoricalDetectionsTablePageComponent.HeaderName header,
                                                DateTimeFormatter formatter) {
        String randomColumnValue = historicalDetectionsTablePageComponent.getAnyColumnValue(header);
        historicalDetectionsTablePageComponent.getFilterPageComponent()
                .clickColumnFilterIcon(header.getStringValue())
                .enterFilterDateValue(randomColumnValue, randomColumnValue, formatter);

        Assert.assertTrue(
                historicalDetectionsTablePageComponent.getFilterPageComponent()
                        .isDateColumnFiltered(historicalDetectionsTablePageComponent.getColumnData(header.getStringValue()),
                                randomColumnValue, randomColumnValue, formatter),
                "Historical Detections should be filtered by %s column correctly".formatted(header.getStringValue()));
    }

    private void sortTableColumnsAndCheck(HistoricalDetectionsTablePageComponent.HeaderName headerName,
                                          TableGeneralComponent.SortStatus sortType) {

        historicalDetectionsTablePageComponent.sortTable(headerName, sortType);

        List<String> actualColumnData = historicalDetectionsTablePageComponent.getColumnData(headerName.getStringValue());
        Assert.assertEquals(actualColumnData,
                historicalDetectionsTablePageComponent.getListByDirection(headerName.getStringValue(), sortType),
                "%s column should be sorted properly, sort type is: %s".formatted(headerName.getStringValue(), sortType));
    }

    private void sortTableDateColumnsAndCheck(HistoricalDetectionsTablePageComponent.HeaderName headerName,
                                              TableGeneralComponent.SortStatus sortType) {

        historicalDetectionsTablePageComponent.sortTable(headerName, sortType);

        List<String> actualColumnData = historicalDetectionsTablePageComponent.getColumnData(headerName.getStringValue());

        DateTimeFormatter formatter = getFormatter(headerName, historicalDetectionsTablePageComponent);
        Assert.assertEquals(actualColumnData,
                historicalDetectionsTablePageComponent.getDateListByDirection(
                        formatter, headerName.getStringValue(), sortType),
                "%s column should be sorted properly, sort type is: %s".formatted(headerName.getStringValue(), sortType));
    }
}
