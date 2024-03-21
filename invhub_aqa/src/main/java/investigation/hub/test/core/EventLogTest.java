package investigation.hub.test.core;

import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.core.util.Retry;
import investigation.hub.common.web.components.EventLogPageComponent;
import investigation.hub.common.web.components.modals.LeftFloatMenuInvestigationModalComponent;
import investigation.hub.common.web.components.tables.EventLogTablePageComponent;
import investigation.hub.common.web.components.tables.InvestigationsTablePageComponent;
import investigation.hub.common.web.components.tables.TableGeneralComponent;
import investigation.hub.common.web.pages.InvestigationPage;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.codeborne.selenide.Selenide.open;

public class EventLogTest extends InvHubUiTest {

    public String subjectId;
    InvestigationPage investigationPage;
    InvestigationsTablePageComponent investigationsTable;

    @BeforeMethod
    public void openPage() {
        open(apiProperties.getUrl() + apiProperties.getOpenInvestigations());
        openInvestigationsPage.waitForLoad();
        investigationsTable = openInvestigationsPage
                .clickAllWorkButton()
                .getInvestigationsTable();
        subjectId = investigationsTable.getRandomSubjectId();
    }

    @TmsLink("INVHUB-1247")
    @Test(retryAnalyzer = Retry.class, description = "Verify Event Log can be filtered by 'Event' column")
    public void checkEventLogCanBeFilteredByEventColumnTest() {
        EventLogTablePageComponent.HeaderName headerName = EventLogTablePageComponent.HeaderName.EVENT;
        List<String> filterQueries = Arrays.asList("Subject Added", "Subject Changed");

        investigationPage = investigationsTable.clickRowBySubjectId(subjectId);

        EventLogPageComponent eventLogPageComponent = investigationPage.getLeftMenuComponent()
                .clickMenuItemByName(LeftFloatMenuInvestigationModalComponent.LeftMenuItemName.EVENT_LOG);
        EventLogTablePageComponent eventLogTablePageComponent = eventLogPageComponent
                .getEventLogTablePageComponent();
        eventLogTablePageComponent
                .getFilterPageComponent()
                .clickColumnFilterIcon(headerName.getStringValue())
                .selectFilterQueries(filterQueries);

        Set<String> eventColumnValues = new HashSet<>(eventLogTablePageComponent.getColumnData(headerName.getStringValue()));

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(eventColumnValues.size(), filterQueries.size(),
                "There should be only those values which were filtered");
        softAssert.assertEquals(eventColumnValues, new HashSet<>(filterQueries),
                "The value should correspond to filter query");
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1248")
    @Test(retryAnalyzer = Retry.class, description = "Verify Event Log can be sorted by 'Date' column")
    public void checkDateColumnIsSortableTest() {
        EventLogTablePageComponent.HeaderName headerName = EventLogTablePageComponent.HeaderName.DATE;
        TableGeneralComponent.SortStatus sortType = TableGeneralComponent.SortStatus.ASCENDING;

        investigationPage = investigationsTable
                .clickRowBySubjectId(subjectId);

        EventLogPageComponent eventLogPageComponent = investigationPage.getLeftMenuComponent()
                .clickMenuItemByName(LeftFloatMenuInvestigationModalComponent.LeftMenuItemName.EVENT_LOG);
        EventLogTablePageComponent eventLogTablePageComponent = eventLogPageComponent
                .getEventLogTablePageComponent()
                .sortTable(headerName, sortType);

        List<String> columnData = eventLogTablePageComponent
                .getColumnData(headerName.getStringValue());
        Assert.assertEquals(columnData,
                eventLogTablePageComponent.getDateListByDirection(
                        eventLogTablePageComponent.getFormatter(),
                        headerName.getStringValue(),
                        sortType),
                headerName.getStringValue() + " column should be sorted properly, sort type is: " + sortType);
    }
}
