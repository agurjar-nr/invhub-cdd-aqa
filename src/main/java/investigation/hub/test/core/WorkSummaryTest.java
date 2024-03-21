package investigation.hub.test.core;

import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.core.util.Retry;
import investigation.hub.common.web.components.AmlPageComponent;
import investigation.hub.common.web.components.WorkSummaryPageComponent;
import investigation.hub.common.web.components.WorkSummaryPageComponent.AMLDetectionsGroups;
import investigation.hub.common.web.components.modals.LeftFloatMenuInvestigationModalComponent.LeftMenuItemName;
import investigation.hub.common.web.components.tables.HistoricalDetectionsTablePageComponent;
import investigation.hub.common.web.components.tables.InvestigationsTablePageComponent;
import investigation.hub.common.web.components.tables.OpenDetectionsTablePageComponent;
import investigation.hub.common.web.pages.InvestigationPage;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;

import static com.codeborne.selenide.Selenide.open;

public class WorkSummaryTest extends InvHubUiTest {

    public String subjectId;
    InvestigationsTablePageComponent openInvestigationsTable;
    InvestigationPage investigationPage;
    AmlPageComponent amlPageComponent;
    WorkSummaryPageComponent workSummaryPageComponent;

    @BeforeMethod
    public void openPage() {
        open(apiProperties.getUrl() + apiProperties.getOpenInvestigations());
        openInvestigationsPage.waitForLoad();
        openInvestigationsTable = openInvestigationsPage
                .clickAllWorkButton()
                .getInvestigationsTable();
        subjectId = openInvestigationsTable.getRandomSubjectId();

        investigationPage = openInvestigationsTable.clickRowBySubjectId(subjectId);
        amlPageComponent = investigationPage.getLeftMenuComponent()
                .clickMenuItemByName(LeftMenuItemName.DETECTIONS);
    }

    @TmsLink("INVHUB-2031")
    @Test(retryAnalyzer = Retry.class, description = "Verify AI Investigation Summary is not blank")
    public void checkAIInvestigationSummaryNotBlankTest() {
        Assert.assertFalse(amlPageComponent.getAIInvestigationSummary().isBlank(),
                "AI Investigation Summary should not be blank");
    }

    //BUG TODO add link to @Issue annotation
    @TmsLink("INVHUB-2030")
    @Test(retryAnalyzer = Retry.class, description = "Verify AML Detections data counting is correct")
    public void checkAmlDetectionsDataCountingCorrectTest() {
        OpenDetectionsTablePageComponent openDetectionsTablePageComponent = amlPageComponent
                .getOpenDetectionsPageComponent()
                .getOpenDetectionsTablePageComponent();

        HistoricalDetectionsTablePageComponent historicalDetectionsTablePageComponent = amlPageComponent
                .getHistoricalDetectionsPageComponent()
                .getHistoricalDetectionsTablePageComponent();

        HashMap<AMLDetectionsGroups, Integer> amlDetectionsCountsMap = new HashMap<>();
        amlDetectionsCountsMap.put(AMLDetectionsGroups.OPEN,
                openDetectionsTablePageComponent.getAllRowsNumber());
        amlPageComponent.clickHistoricalDetectionsButton();
        amlDetectionsCountsMap.put(AMLDetectionsGroups.CLOSED,
                historicalDetectionsTablePageComponent.getAllRowsNumber());
        amlDetectionsCountsMap.put(AMLDetectionsGroups.UNUSUAL,
                historicalDetectionsTablePageComponent.getUnusualAMLDetectionCount());
        amlDetectionsCountsMap.put(AMLDetectionsGroups.NOT_UNUSUAL,
                historicalDetectionsTablePageComponent.getNotUnusualAMLDetectionCount());
        amlDetectionsCountsMap.put(AMLDetectionsGroups.SKIPPED, 0); //TODO functionality not implemented yet
        workSummaryPageComponent = amlPageComponent.clickWorkSummaryButton();

        Assert.assertEquals(workSummaryPageComponent.getAMLDetectionsGroupsCounts(), amlDetectionsCountsMap,
                "AML Detections counting should be correct");
    }
}
