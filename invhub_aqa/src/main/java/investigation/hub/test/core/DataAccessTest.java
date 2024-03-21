package investigation.hub.test.core;

import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.core.services.api.client.LoginClient;
import investigation.hub.common.core.util.Retry;
import investigation.hub.common.web.components.tables.InvestigationsTablePageComponent;
import investigation.hub.common.web.components.tables.InvestigationsTablePageComponent.HeaderName;
import investigation.hub.common.web.components.tables.OrganisationUnitsTablePageComponent.Codes;
import investigation.hub.common.web.pages.AllOpenInvestigationsPage;
import investigation.hub.common.web.pages.LoginPage;
import investigation.hub.common.web.pages.NewUserPage;
import investigation.hub.common.web.test.data.constants.AccessPolicy;
import investigation.hub.common.web.test.data.dtos.TeamDto;
import investigation.hub.common.web.test.data.dtos.UserDto;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.codeborne.selenide.Selenide.open;
import static investigation.hub.common.web.test.data.repository.TeamDtoRepository.createAdminAllAMLAccessTeamInstance;
import static investigation.hub.common.web.test.data.repository.UserDtoRepository.createAdminUserInstance;

public class DataAccessTest extends InvHubUiTest {
    AllOpenInvestigationsPage allOpenInvestigationsPage = new AllOpenInvestigationsPage();
    InvestigationsTablePageComponent investigationsTable = allOpenInvestigationsPage.getInvestigationsTable();
    UserDto userDto;

    @BeforeClass
    public void openPage() {
        open(apiProperties.getUrl() + apiProperties.getOpenInvestigations());
        allOpenInvestigationsPage.waitForLoad();
    }

    @BeforeMethod
    public void createUser() {
        userDto = createAdminUserInstance();
        createUserWithData(userDto);
    }

    @TmsLink("INVHUB-2795")
    @Test(retryAnalyzer = Retry.class,
            description = "Verify an empty screen is seen by default user isn't assigned to any of the teams")
    public void checkEmptyScreenIfUserNotAssignToAnyTeamTest() {
        open(apiProperties.getUrl() + apiProperties.getLogin());
        new LoginPage().loginUser(userDto.getEmail(), new LoginClient())
                .getMainHeaderComponent()
                .openAllOpenInvestigations()
                .clickAllWorkButton();
        Assert.assertEquals(investigationsTable.getAllRowsNumber(), 0,
                "An empty table should be seen");
    }

    @TmsLink("INVHUB-2796")
    @Test(retryAnalyzer = Retry.class,
            description = "Verify user can see investigations with Org Units and child Org Units according to Access Policy")
    public void checkOpenInvestigationsFilterAccordingToUserAccessPolicyTest() {
        UserDto userDto = createAdminUserInstance();
        createUserWithData(userDto);
        TeamDto teamDto = createAdminAllAMLAccessTeamInstance();
        teamDto.setAccessPolicy(AccessPolicy.ALL_AML_ACCESS);
        createTeamAndAddUser(teamDto, userDto);
        open(apiProperties.getUrl() + apiProperties.getLogin());
        new LoginPage().loginUser(userDto.getEmail(), new LoginClient())
                .getMainHeaderComponent()
                .openAllOpenInvestigations()
                .clickAllWorkButton();
        List<String> orgUnitsOfAccessPolicy = Codes.getAllStringValues();
        Assert.assertTrue(orgUnitsOfAccessPolicy.containsAll(investigationsTable
                        .getColumnData(HeaderName.ORGANISATION_UNIT.getStringValue())),
                "Investigations table should be filtered according to user's Access Policy");
    }

    @TmsLink("INVHUB-2829")
    @Test(retryAnalyzer = Retry.class, description = "Verify user can not see investigations out of user's Access Policy")
    public void checkUserCanNotSeeOpenInvestigationsOutOfAccessPolicyTest() {
        UserDto userDto = createAdminUserInstance();
        createUserWithData(userDto);
        TeamDto teamDto = createAdminAllAMLAccessTeamInstance();
        teamDto.setAccessPolicy(AccessPolicy.EUR_AML_ACCESS);
        createTeamAndAddUser(teamDto, userDto);
        open(apiProperties.getUrl() + apiProperties.getLogin());
        new LoginPage().loginUser(userDto.getEmail(), new LoginClient())
                .getMainHeaderComponent()
                .openAllOpenInvestigations()
                .clickAllWorkButton();
        Set<String> orgUnitsOutOfAccessPolicy = Set.of(Codes.GROUP.getStringValue(), Codes.NA.getStringValue(),
                Codes.APAC.getStringValue());
        Assert.assertFalse(orgUnitsOutOfAccessPolicy.containsAll(investigationsTable
                        .getColumnData(HeaderName.ORGANISATION_UNIT.getStringValue())),
                "Investigations table should be filtered according to user's Access Policy");

    }

    private void createUserWithData(UserDto userDto) {
        NewUserPage newUserPage = new NewUserPage();
        allOpenInvestigationsPage
                .getMainHeaderComponent()
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

    private void createTeamAndAddUser(TeamDto teamDto, UserDto userDto) {
        allOpenInvestigationsPage
                .getMainHeaderComponent()
                .openTeamsManagement()
                .clickCreateTeamButton()
                .enterName(teamDto.getName())
                .enterDescription(teamDto.getDescription())
                .selectRole(teamDto.getRole())
                .selectAccessPolicy(teamDto.getAccessPolicy())
                .clickCreateButton()
                .getTeamsManagementTablePageComponent()
                .clickRowByTeamName(teamDto.getName())
                .clickAddTeamMembersButton()
                .selectUsers(Collections.singletonList(userDto.getFullName()))
                .clickAddButton();
    }

}
