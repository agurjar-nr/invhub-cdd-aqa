package investigation.hub.test.core;

import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.web.components.tables.TableGeneralComponent;
import investigation.hub.common.web.components.tables.TeamMembersTableComponent;
import investigation.hub.common.web.components.tables.TeamMembersTableComponent.HeaderName;
import investigation.hub.common.web.components.tables.TeamsManagementTablePageComponent;
import investigation.hub.common.web.pages.LogHistoryPage;
import investigation.hub.common.web.pages.NewUserPage;
import investigation.hub.common.web.pages.TeamDetailsPage;
import investigation.hub.common.web.pages.TeamsManagementPage;
import investigation.hub.common.web.test.data.dtos.TeamDto;
import investigation.hub.common.web.test.data.dtos.UserDto;
import investigation.hub.common.web.test.data.repository.LogRepository;
import investigation.hub.common.web.test.data.repository.UserDtoRepository;
import io.qameta.allure.TmsLink;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static investigation.hub.common.web.test.data.repository.TeamDtoRepository.createAdminAllAMLAccessTeamInstance;
import static investigation.hub.common.web.test.data.repository.TeamDtoRepository.createGuestEurAMLAccessTeamInstance;
import static investigation.hub.common.web.test.data.repository.UserDtoRepository.createAdminUserInstance;

public class TeamDetailsTest extends InvHubUiTest {

    SoftAssert softAssert;
    NewUserPage newUserPage = new NewUserPage();
    TeamsManagementPage teamsManagementPage;
    TeamDetailsPage teamDetailsPage = new TeamDetailsPage();
    TeamMembersTableComponent teamMembersTableComponent = teamDetailsPage.getTeamMembersTableComponent();
    UserDto userDto = createAdminUserInstance();
    TeamDto teamDto;

    @BeforeClass
    public void createUser() {
        open(apiProperties.getUrl() + apiProperties.getOpenInvestigations());
        openInvestigationsPage.waitForLoad();
        createUserWithData(userDto);
    }

    @BeforeMethod
    public void openPage() {
        teamDto = createAdminAllAMLAccessTeamInstance();
        teamsManagementPage = createTeam(teamDto);
        teamDetailsPage = teamsManagementPage.getTeamsManagementTablePageComponent()
                .clickRowByTeamName(teamDto.getName());
    }

    @TmsLink("INVHUB-2509")
    @Test(enabled = false, description = "Verify Team Members can be sorted by 'Member Name' column")
    public void checkMemberNameColumnIsSortableTest() {
        softAssert = new SoftAssert();
        teamDetailsPage.clickAddTeamMembersButton()
                .selectUsers(Arrays.asList(UserDtoRepository.createTestUserInstance().getFullName(), userDto.getFullName()))
                .clickAddButton();
        HeaderName headerName = HeaderName.MEMBER_NAME;
        sortTableColumnsAndCheck(headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableColumnsAndCheck(headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-2510")
    @Test(enabled = false, description = "Verify Team Members can be sorted by 'Added On' column")
    public void checkAddedOnColumnIsSortableTest() {
        softAssert = new SoftAssert();
        HeaderName headerName = HeaderName.ADDED_ON;
        teamDetailsPage.clickAddTeamMembersButton()
                .selectUsers(Arrays.asList(UserDtoRepository.createTestUserInstance().getFullName(), userDto.getFullName()))
                .clickAddButton();
        sortTableDateColumnsAndCheck(headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableDateColumnsAndCheck(headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-2349")
    @Test(description = " Verify Team can be removed")
    public void checkUserCanRemoveTeamTest() {
        softAssert = new SoftAssert();
        teamDetailsPage.clickRemoveTeamButton();
        softAssert.assertEquals(teamDetailsPage.getRemoveConfirmationModalComponent()
                        .getTeamDeletionWillUnsubscribeAllMembersMessage(),
                "This action will unsubscribe all members from the team, and then remove the team. This action "
                        + "is irreversible. message should appear", "Exact message should be present");
        teamDetailsPage.getRemoveConfirmationModalComponent().confirmRemoval();
        softAssert.assertFalse(teamsManagementPage.getTeamsManagementTablePageComponent()
                .waitForTableContent()
                .getColumnData(TeamsManagementTablePageComponent.HeaderName.NAME.getStringValue())
                .contains(teamDto.getName()), "Removed team should not be present in the table");
        checkLog(LogRepository.getDeleteTeamLog(teamDto));
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-2345")
    @Test(description = "Verify user can add users to a Team")
    public void checkUserCanAddUsersToTeamTest() {
        softAssert = new SoftAssert();
        Map<String, String> teamMember = new HashMap<>();
        teamMember.put(HeaderName.MEMBER_NAME.getStringValue(), userDto.getFullName());
        teamMember.put(HeaderName.ADDED_ON.getStringValue(), teamDto.getCreatedOn());
        teamDetailsPage.clickAddTeamMembersButton()
                .selectUsers(Collections.singletonList(teamMember.get(HeaderName.MEMBER_NAME.getStringValue())))
                .clickAddButton();
        softAssert.assertTrue(teamDetailsPage.isMembersWereSuccessfullyAddedToTeamMessageAppeared(),
                "Members were successfully added to Team should appear");
        teamMembersTableComponent.waitForTableContent();

        softAssert.assertNotNull(teamMembersTableComponent.getRowByContent(teamMember),
                "Team Member should be present in the table");
        checkLog(LogRepository.getAddTeamMembersLog(teamDto, userDto));
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-2346")
    @Test(description = "Verify user can remove users from a Team")
    public void checkUserCanRemoveUsersToTeamTest() {
        softAssert = new SoftAssert();
        Map<String, String> teamMember = new HashMap<>();
        teamMember.put(HeaderName.MEMBER_NAME.getStringValue(), userDto.getFullName());
        teamMember.put(HeaderName.ADDED_ON.getStringValue(), teamDto.getCreatedOn());
        teamDetailsPage.clickAddTeamMembersButton()
                .selectUsers(Collections.singletonList(teamMember.get(HeaderName.MEMBER_NAME.getStringValue())))
                .clickAddButton()
                .getTeamMembersTableComponent()
                .selectCheckboxByRowData(teamMember);
        teamDetailsPage.clickRemoveFromTeamButton()
                .confirmRemoval();
        softAssert.assertTrue(teamDetailsPage.isTeamMembersRemovedSuccessfullyMessageAppeared(),
                "Members were successfully removed from Team should appear");
        softAssert.assertEquals(teamMembersTableComponent.getAllRowsNumber(), 0,
                "Team Member table is empty");
        checkLog(LogRepository.getRemoveTeamMembersLog(teamDto, userDto));
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-2578")
    @Test(description = " Verify user can edit the Team")
    public void checkUserCanEditTeamTest() {
        softAssert = new SoftAssert();
        TeamDto updatedTeamDto = createGuestEurAMLAccessTeamInstance();
        teamDetailsPage.clickEditTeamButton()
                .updateName(updatedTeamDto.getName())
                .updateRole(updatedTeamDto.getRole())
                .updateAccessPolicy(updatedTeamDto.getAccessPolicy())
                .updateDescription(updatedTeamDto.getDescription())
                .clickUpdateButton();
        softAssert.assertTrue(teamDetailsPage.isTeamDetailsUpdatedMessageAppeared(),
                "Team updated successfully message should appear");
        softAssert.assertEquals(teamDetailsPage.getTeamName(), updatedTeamDto.getName(),
                "Name should correspond to the updated value");
        softAssert.assertEquals(teamDetailsPage.getTeamRole(), updatedTeamDto.getRole().getStringValue(),
                "Role should correspond to the updated value");
        softAssert.assertEquals(teamDetailsPage.getTeamDescription(), updatedTeamDto.getDescription(),
                "Description should correspond to the updated value");
        checkLog(LogRepository.getUpdateTeamLog(teamDto, updatedTeamDto));
        softAssert.assertAll();
    }

    private void sortTableColumnsAndCheck(HeaderName headerName,
                                          TableGeneralComponent.SortStatus sortType) {
        teamMembersTableComponent.sortTable(headerName, sortType);
        softAssert.assertEquals(teamMembersTableComponent
                        .getColumnData(headerName.getStringValue()),
                teamMembersTableComponent
                        .getListByDirection(headerName.getStringValue(), sortType),
                headerName.getStringValue() + " column should be sorted properly, sort type is: " + sortType);
    }

    private void sortTableDateColumnsAndCheck(HeaderName headerName,
                                              TableGeneralComponent.SortStatus sortType) {
        teamMembersTableComponent
                .sortTable(headerName, sortType);
        softAssert.assertEquals(teamMembersTableComponent.getColumnData(headerName.getStringValue()),
                teamMembersTableComponent.getDateListByDirection(
                        teamMembersTableComponent.getFormatter(),
                        headerName.getStringValue(),
                        sortType),
                headerName.getStringValue() + " column should be sorted properly, sort type is: " + sortType);
    }

    private TeamsManagementPage createTeam(TeamDto teamDto) {
        return openInvestigationsPage.getMainHeaderComponent()
                .openTeamsManagement()
                .clickCreateTeamButton()
                .enterName(teamDto.getName())
                .enterDescription(teamDto.getDescription())
                .selectRole(teamDto.getRole())
                .selectAccessPolicy(teamDto.getAccessPolicy())
                .clickCreateButton();
    }

    private void createUserWithData(UserDto userDto) {
        openInvestigationsPage.getMainHeaderComponent()
                .openUsers()
                .clickCreateNewUserButton()
                .getPersonalDetails()
                .enterFirstName(userDto.getFirstName())
                .enterLastName(userDto.getLastName())
                .clickUploadButton()
                .uploadPhoto(userDto.getPhoto())
                .clickUpload1FileButton()
                .clickDoneButton()
                .enterEmail(userDto.getEmail())
                .clickLanguageDropdown()
                .selectLanguage(userDto.getLanguage())
                .clickWeekStarsOnDropdown()
                .selectWeekStartsOnDay(userDto.getWeekStartsOn())
                .clickTimeZoneDropdown()
                .selectTimeZone(userDto.getTimeZone());
        newUserPage.clickSaveButton();
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
