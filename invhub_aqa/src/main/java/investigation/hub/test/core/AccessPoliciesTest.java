package investigation.hub.test.core;

import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.core.util.Retry;
import investigation.hub.common.web.components.AccessPoliciesPageComponent;
import investigation.hub.common.web.components.tables.DataAccessTablePageComponent;
import investigation.hub.common.web.components.tables.DataAccessTablePageComponent.HeaderName;
import investigation.hub.common.web.components.tables.TableGeneralComponent;
import investigation.hub.common.web.pages.AllOpenInvestigationsPage;
import io.qameta.allure.TmsLink;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.codeborne.selenide.Selenide.open;

public class AccessPoliciesTest extends InvHubUiTest {
    SoftAssert softAssert = new SoftAssert();
    AllOpenInvestigationsPage allOpenInvestigationsPage = new AllOpenInvestigationsPage();
    AccessPoliciesPageComponent accessPoliciesPageComponent = new AccessPoliciesPageComponent();
    DataAccessTablePageComponent dataAccessTablePageComponent = accessPoliciesPageComponent
            .getDataAccessTablePageComponent();

    @BeforeClass
    public void openPage() {
        open(apiProperties.getUrl() + apiProperties.getOpenInvestigations());
        allOpenInvestigationsPage.waitForLoad();
        accessPoliciesPageComponent = allOpenInvestigationsPage
                .getMainHeaderComponent()
                .openDataAccess()
                .getAccessPoliciesPageComponent();
    }

    @TmsLink("INVHUB-3746")
    @Test(retryAnalyzer = Retry.class, description = "Verify Data Access can be sorted by 'Name' column")
    public void checkDataAccessNameColumnIsSortableTest() {
        softAssert = new SoftAssert();
        HeaderName headerName = HeaderName.NAME;
        sortTableColumnsAndCheck(headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableColumnsAndCheck(headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-3752")
    @Test(retryAnalyzer = Retry.class, description = "Verify Data Access can be sorted by 'Last Update' column")
    public void checkDataAccessLastUpdateColumnIsSortableTest() {
        softAssert = new SoftAssert();
        HeaderName headerName = HeaderName.LAST_UPDATE;
        sortTableDateColumnsAndCheck(headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableDateColumnsAndCheck(headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-3751")
    @Test(retryAnalyzer = Retry.class, description = "Verify Data Access can be sorted by 'Last Update' column")
    public void checkDataAccessCreatedOnColumnIsSortableTest() {
        softAssert = new SoftAssert();
        HeaderName headerName = HeaderName.CREATED_ON;
        sortTableDateColumnsAndCheck(headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableDateColumnsAndCheck(headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    private void sortTableColumnsAndCheck(HeaderName headerName,
                                          TableGeneralComponent.SortStatus sortType) {
        dataAccessTablePageComponent.sortTable(headerName, sortType);
        softAssert.assertEquals(dataAccessTablePageComponent.getColumnData(headerName.getStringValue()),
                dataAccessTablePageComponent.getListByDirection(headerName.getStringValue(), sortType),
                headerName.getStringValue() + " column should be sorted properly, sort type is: " + sortType);
    }

    private void sortTableDateColumnsAndCheck(HeaderName headerName,
                                              TableGeneralComponent.SortStatus sortType) {
        dataAccessTablePageComponent.sortTable(headerName, sortType);
        softAssert.assertEquals(dataAccessTablePageComponent.getColumnData(headerName.getStringValue()),
                dataAccessTablePageComponent.getDateListByDirection(
                        dataAccessTablePageComponent.getFormatter(),
                        headerName.getStringValue(),
                        sortType), headerName.getStringValue() + " column should be sorted properly, sort type is: " + sortType);
    }
}
