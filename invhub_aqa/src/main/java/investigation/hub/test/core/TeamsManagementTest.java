package investigation.hub.test.core;

import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.web.components.tables.TableGeneralComponent;
import investigation.hub.common.web.components.tables.TeamsManagementTablePageComponent;
import investigation.hub.common.web.components.tables.TeamsManagementTablePageComponent.HeaderName;
import investigation.hub.common.web.pages.LogHistoryPage;
import investigation.hub.common.web.pages.TeamsManagementPage;
import investigation.hub.common.web.test.data.dtos.TeamDto;
import investigation.hub.common.web.test.data.repository.LogRepository;
import investigation.hub.common.web.test.data.repository.UserDtoRepository;
import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static investigation.hub.common.web.test.data.repository.TeamDtoRepository.createAdminAllAMLAccessTeamInstance;

public class TeamsManagementTest extends InvHubUiTest {
    SoftAssert softAssert;
    TeamsManagementPage teamsManagementPage;
    TeamsManagementTablePageComponent teamsManagementTablePageComponent = new TeamsManagementPage()
            .getTeamsManagementTablePageComponent();

    @BeforeMethod
    public void openPage() {
        open(apiProperties.getUrl() + apiProperties.getOpenInvestigations());
        openInvestigationsPage.waitForLoad();
        teamsManagementPage = openInvestigationsPage.getMainHeaderComponent().openTeamsManagement();
    }

    @TmsLink("INVHUB-2343")
    @Test(description = " Verify Team can be added")
    public void checkUserCanCreateTeamTest() {
        softAssert = new SoftAssert();
        TeamDto teamDto = createAdminAllAMLAccessTeamInstance();
        createTeamWithData(teamDto);
        softAssert.assertTrue(teamsManagementPage.isTeamCreatedSuccessfullyMessageAppeared(),
                "Team created successfully message should appear");
        teamsManagementTablePageComponent.sortTable(HeaderName.CREATED_ON, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertTrue(teamsManagementTablePageComponent.getColumnData(HeaderName.NAME.getStringValue())
                .contains(teamDto.getName()), "Created team should be present in the table");
        checkLog(LogRepository.getCreateTeamLog(teamDto));
        softAssert.assertAll();
    }

    @Issue("INVHUB-2644")
    @TmsLink("INVHUB-1249")
    @Test(description = "Verify Teams can be sorted by 'Name' column")
    public void checkNameColumnIsSortableTest() {
        softAssert = new SoftAssert();
        HeaderName headerName = HeaderName.NAME;
        sortTableColumnsAndCheck(headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableColumnsAndCheck(headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    @Issue("INVHUB-2644")
    @TmsLink("INVHUB-1243")
    @Test(description = "Verify Teams can be sorted by 'Description' column")
    public void checkDescriptionColumnIsSortableTest() {
        softAssert = new SoftAssert();
        HeaderName headerName = HeaderName.DESCRIPTION;
        sortTableColumnsAndCheck(headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableColumnsAndCheck(headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1242")
    @Test(description = "Verify Teams can be sorted by 'Last Update' column")
    public void checkLastUpdateColumnIsSortableTest() {
        softAssert = new SoftAssert();
        HeaderName headerName = HeaderName.LAST_UPDATE;
        sortTableDateColumnsAndCheck(headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableDateColumnsAndCheck(headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-2335")
    @Test(description = "Verify Teams can be sorted by 'Created On' column")
    public void checkCreatedOnColumnIsSortableTest() {
        softAssert = new SoftAssert();
        HeaderName headerName = HeaderName.CREATED_ON;
        sortTableDateColumnsAndCheck(headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableDateColumnsAndCheck(headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-2336")
    @Test(description = "Verify Teams can be sorted by '# of Members' column")
    public void checkMembersColumnIsSortableTest() {
        softAssert = new SoftAssert();
        HeaderName headerName = HeaderName.MEMBERS;
        sortTableNumericColumnsAndCheck(headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableNumericColumnsAndCheck(headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    private void sortTableColumnsAndCheck(HeaderName headerName,
                                          TableGeneralComponent.SortStatus sortType) {
        teamsManagementTablePageComponent.sortTable(headerName, sortType);
        softAssert.assertEquals(teamsManagementTablePageComponent
                        .getColumnData(headerName.getStringValue()),
                teamsManagementTablePageComponent
                        .getListByDirection(headerName.getStringValue(), sortType),
                headerName.getStringValue() + " column should be sorted properly, sort type is: " + sortType);
    }

    private void sortTableNumericColumnsAndCheck(HeaderName headerName,
                                          TableGeneralComponent.SortStatus sortType) {
        teamsManagementTablePageComponent.sortTable(headerName, sortType);
        List<Double> columnData = teamsManagementTablePageComponent.getNumericColumnData(headerName.getStringValue());
        softAssert.assertEquals(columnData,
                teamsManagementTablePageComponent
                        .getNumericListByDirection(columnData, sortType),
                headerName.getStringValue() + " column should be sorted properly, sort type is: " + sortType);
    }

    private void sortTableDateColumnsAndCheck(HeaderName headerName,
                                              TableGeneralComponent.SortStatus sortType) {
        teamsManagementTablePageComponent
                .sortTable(headerName, sortType);
        softAssert.assertEquals(teamsManagementTablePageComponent.getColumnData(headerName.getStringValue()),
                teamsManagementTablePageComponent.getDateListByDirection(
                        teamsManagementTablePageComponent.getFormatter(),
                        headerName.getStringValue(),
                        sortType),
                headerName.getStringValue() + " column should be sorted properly, sort type is: " + sortType);
    }

    private void createTeamWithData(TeamDto teamDto) {
        teamsManagementPage
                .clickCreateTeamButton()
                .enterName(teamDto.getName())
                .enterDescription(teamDto.getDescription())
                .selectRole(teamDto.getRole())
                .selectAccessPolicy(teamDto.getAccessPolicy())
                .clickCreateButton();
    }

    private void checkLog(Map<String, String> logToFind) {
        String userFullName = UserDtoRepository.createTestUserInstance().getFullName();
        LogHistoryPage logHistoryPage = openInvestigationsPage.getMainHeaderComponent()
                .openUsers()
                .searchUser(UserDtoRepository.createTestUserInstance().getFirstName())//known bug
                .openUserProfile(userFullName)
                .clickLogHistoryButton();
        Map<String, String> recentAuditLogData = logHistoryPage.getLogHistoryTable().getRecentAuditLogData();
        softAssert.assertTrue(recentAuditLogData.entrySet().containsAll(logToFind.entrySet()),
                "Log: %s should be present".formatted(logToFind.entrySet()));
    }
}
