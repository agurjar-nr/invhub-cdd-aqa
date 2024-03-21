package investigation.hub.test.core;

import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.core.util.Retry;
import investigation.hub.common.web.components.tables.LogHistoryTableComponent;
import investigation.hub.common.web.components.tables.LogHistoryTableComponent.HeaderName;
import investigation.hub.common.web.components.tables.TableGeneralComponent;
import investigation.hub.common.web.pages.LogHistoryPage;
import investigation.hub.common.web.test.data.dtos.UserDto;
import investigation.hub.common.web.test.data.repository.UserDtoRepository;
import io.qameta.allure.TmsLink;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static investigation.hub.common.web.enums.InvHubDateFormat.YEAR_MONTH_DAY_TIME;

@Log4j2
public class LogHistoryTest extends InvHubUiTest {
    LogHistoryPage logHistoryPage = new LogHistoryPage();
    UserDto userDto = UserDtoRepository.createTestUserInstance(); //TODO: use test user from login with not empty log history

    @BeforeMethod
    public void openPage() {
        open(apiProperties.getUrl() + apiProperties.getOpenInvestigations());
        openInvestigationsPage.waitForLoad();
    }

    @TmsLink("INVHUB-1067")
    @Test(retryAnalyzer = Retry.class, description = "Verify user can check log history of the current user")
    public void checkUserLogHistoryPageTest() {
        openInvestigationsPage
                .getMainHeaderComponent()
                .openUsers()
                .searchUser(userDto.getLastName())
                .getUserAndRolesPageTable()
                .clickOnRowByUserHeader(userDto.getFullName())
                .clickLogHistoryButton();

        Assert.assertTrue(logHistoryPage.isLogHistoryBreadCrumbVisible(), "Log history page should be visible");
    }

    @TmsLink("INVHUB-1076")
    @Test(retryAnalyzer = Retry.class, description = "Verify Log History can be sorted by 'Time' column")
    public void checkTimeColumnIsSortableTest() {
        SoftAssert softAssert = new SoftAssert();

        openInvestigationsPage
                .getMainHeaderComponent()
                .openUsers()
                .searchUser(userDto.getLastName())
                .getUserAndRolesPageTable()
                .clickOnRowByUserHeader(userDto.getFullName())
                .clickLogHistoryButton();

        HeaderName headerName = HeaderName.TIME;
        sortTableDateColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableDateColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1070")
    @Test(retryAnalyzer = Retry.class, description = "Verify Log History can be sorted by 'Activity' column")
    public void checkActivityColumnIsSortableTest() {
        SoftAssert softAssert = new SoftAssert();
        openInvestigationsPage
                .getMainHeaderComponent()
                .openUsers()
                .searchUser(userDto.getLastName())
                .getUserAndRolesPageTable()
                .clickOnRowByUserHeader(userDto.getFullName())
                .clickLogHistoryButton();

        HeaderName headerName = HeaderName.ACTIVITY;
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1324")
    //BUG
    @Test(retryAnalyzer = Retry.class, description = "Verify Log History can be sorted by 'Performed On' column")
    public void checkPerformedOnColumnIsSortableTest() {
        SoftAssert softAssert = new SoftAssert();
        openInvestigationsPage
                .getMainHeaderComponent()
                .openUsers()
                .searchUser(userDto.getLastName())
                .getUserAndRolesPageTable()
                .clickOnRowByUserHeader(userDto.getFullName())
                .clickLogHistoryButton();

        HeaderName headerName = HeaderName.PERFORMED_ON;
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1323")
    @Test(retryAnalyzer = Retry.class, description = "Verify Log History can be sorted by 'Performed By' column")
    public void checkPerformedByColumnIsSortableTest() {
        SoftAssert softAssert = new SoftAssert();
        openInvestigationsPage
                .getMainHeaderComponent()
                .openUsers()
                .searchUser(userDto.getLastName())
                .getUserAndRolesPageTable()
                .clickOnRowByUserHeader(userDto.getFullName())
                .clickLogHistoryButton();

        HeaderName headerName = HeaderName.PERFORMED_BY;
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1054")
    @Test(retryAnalyzer = Retry.class, description = "Verify log history can be filtered by 'Time period'")
    public void checkLogHistoryCanBeFilteredByTimePeriodTest() {
        String headerName = HeaderName.TIME.getStringValue();
        openInvestigationsPage
                .getMainHeaderComponent()
                .openUsers()
                .searchUser(userDto.getLastName())
                .getUserAndRolesPageTable()
                .clickOnRowByUserHeader(userDto.getFullName())
                .clickLogHistoryButton();
        String startDate = logHistoryPage.getLogHistoryTable().getAnyTimeColumnValue();
        String endDate = logHistoryPage.getLogHistoryTable().getAnyTimeColumnValue();
        logHistoryPage
                .clickTimePeriodForm()
                .pickTimePeriod(startDate)
                .pickTimePeriod(endDate)
                .clickApplyButton();
        boolean dateColumnFiltered = logHistoryPage.getLogHistoryTable()
                .getFilterPageComponent()
                .isDateColumnFiltered(
                        logHistoryPage.getLogHistoryTable().getColumnData(headerName),
                        startDate, endDate,
                        YEAR_MONTH_DAY_TIME.getFormatter());
        Assert.assertTrue(
                dateColumnFiltered,
                "Logs should be filtered by '%s' column correctly".formatted(headerName));
    }

    private void sortTableColumnsAndCheck(SoftAssert softAssert, HeaderName headerName,
                                          TableGeneralComponent.SortStatus sortType) {
        LogHistoryTableComponent table = logHistoryPage.getLogHistoryTable();
        table
                .sortTable(headerName, sortType);
        softAssert.assertEquals(table.getColumnData(headerName.getStringValue()),
                table
                        .getListByDirection(headerName.getStringValue(), sortType),
                headerName.getStringValue() + " column should be sorted properly, sort type is: " + sortType);
    }

    private void sortTableDateColumnsAndCheck(SoftAssert softAssert, HeaderName headerName,
                                              TableGeneralComponent.SortStatus sortType) {
        LogHistoryTableComponent table = logHistoryPage.getLogHistoryTable();
        table
                .sortTable(headerName, sortType);
        List<String> columnData = table.getColumnData(headerName.getStringValue());
        softAssert.assertEquals(columnData,
                table.getDateListByDirection(table.getFormatter(), headerName.getStringValue(), sortType),
                headerName.getStringValue() + " column should be sorted properly, sort type is: " + sortType);
    }
}
