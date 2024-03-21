package investigation.hub.test.core;

import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.core.util.Retry;
import investigation.hub.common.web.components.tables.TableGeneralComponent;
import investigation.hub.common.web.components.tables.UserAndRolesPageTableComponent.HeaderName;
import investigation.hub.common.web.pages.NewUserPage;
import investigation.hub.common.web.pages.UserEditPage;
import investigation.hub.common.web.pages.UserProfilePage;
import investigation.hub.common.web.pages.UsersPage;
import investigation.hub.common.web.test.data.dtos.UserDto;
import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.SoftAssertions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static investigation.hub.common.web.test.data.repository.UserDtoRepository.createAdminUserInstance;
import static investigation.hub.common.web.test.data.repository.UserDtoRepository.createGuestUserInstance;

@Log4j2
public class UsersAndRolesTest extends InvHubUiTest {
    UsersPage usersAndRolesPage = new UsersPage();
    NewUserPage newUserPage = new NewUserPage();

    @BeforeMethod
    public void openPage() {
        open(apiProperties.getUrl() + apiProperties.getOpenInvestigations());
        openInvestigationsPage.waitForLoad();
        openInvestigationsPage
                .getMainHeaderComponent()
                .openUsers();
    }

    @TmsLink("INVHUB-1267")
    @Test(retryAnalyzer = Retry.class, description = "Verify Admin user can create new user")// left it as it is
    public void checkAdminUserCanCreateNewUserTest() {
        SoftAssert softAssert = new SoftAssert();
        UserDto userDto = createAdminUserInstance();
        createUserWithData(userDto);
        softAssert.assertTrue(usersAndRolesPage.isNewUserAddedSuccessfullyMessageAppeared(),
                "New User added successfully message should appear");
        usersAndRolesPage.searchUser(userDto.getLastName())
                .getUserAndRolesPageTable()
                .waitForTableContent();
        softAssert.assertTrue(usersAndRolesPage.getUserAndRolesPageTable().getUserColumnData()
                .contains(userDto.getFullName()), "Created user should be present in the table");
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1056")
    @Test(retryAnalyzer = Retry.class, description = "Verify Admin user can deactivate created user")
    public void checkAdminUserCanDeactivateUserTest() {
        UserDto userDto = createAdminUserInstance();
        List<String> columnData = createUserWithData(userDto)
                .searchUser(userDto.getLastName())
                .getUserAndRolesPageTable()
                .clickOnRowByUserHeader(userDto.getFullName())
                .clickDeactivateButton()
                .confirmDeactivation()
                .searchUser(userDto.getLastName())
                .getUserAndRolesPageTable()
                .getUserColumnData();
        Assert.assertFalse(columnData.contains(userDto.getFullName()), "Deactivated user should not be present in the table");
    }

    //TODO add link to bug after Yuriy's clarifications
    @TmsLink("INVHUB-1058")
    @Test(retryAnalyzer = Retry.class, description = "Verify Admin user can edit created user")
    public void checkAdminUserCanEditUserNameTest() {
        SoftAssert softAssert = new SoftAssert();
        UserDto userDto = createAdminUserInstance();
        UserDto updatedUserDto = createGuestUserInstance();
        UserEditPage userEditPage = createUserWithData(userDto)
                .searchUser(userDto.getLastName())
                .getUserAndRolesPageTable()
                .clickOnRowByUserHeader(userDto.getFullName())
                .clickEditButton();
        userEditPage.getPersonalDetails()
                .enterFirstName(updatedUserDto.getFirstName())
                .enterLastName(updatedUserDto.getLastName())
                .enterEmail(updatedUserDto.getEmail())
                .clickLanguageDropdown()
                .selectLanguage(updatedUserDto.getLanguage())
                .clickWeekStarsOnDropdown()
                .selectWeekStartsOnDay(updatedUserDto.getWeekStartsOn())
                .clickTimeZoneDropdown()
                .selectTimeZone(updatedUserDto.getTimeZone());
        UserProfilePage userProfilePage = userEditPage.clickSaveButton();
        softAssert.assertEquals(userProfilePage.getUserFullName(), updatedUserDto.getFullName(),
                "Full name in User Profile should correspond to the updated value");
        softAssert.assertEquals(userProfilePage.getUserEmail(), updatedUserDto.getEmail(),
                "Email in User Profile should correspond to the updated value");
        softAssert.assertEquals(userProfilePage.getUserLanguage(), updatedUserDto.getLanguage(),
                "Language in User Profile should correspond to the updated value");
        softAssert.assertEquals(userProfilePage.getUserWeekStartsOn(), updatedUserDto.getWeekStartsOn().weekday,
                "Week starts on in User Profile should correspond to the updated value");
        softAssert.assertEquals(userProfilePage.getUserTimeZone(), updatedUserDto.getTimeZone().getTimeZoneName(),
                "Time Zone in User Profile should correspond to the updated value");
        softAssert.assertAll();
    }

    @Issue("INVHUB-2964")
    @TmsLink("INVHUB-1057")
    @Test(retryAnalyzer = Retry.class, description = "Verify users can be filtered by full name")
    public void checkUsersCanBeFilteredByFullNameTest() {
        UserDto userDto = createAdminUserInstance();
        List<String> columnData = createUserWithData(userDto)
                .searchUser(userDto.getFullName())
                .getUserAndRolesPageTable()
                .getUserColumnData();
        Assert.assertTrue(columnData.contains(userDto.getFullName()), "Users should be filtered by full name");
    }

    @TmsLink("INVHUB-1266")
    @Test(retryAnalyzer = Retry.class, description = "Verify user can hide column from Users and Roles table")
    public void checkUsersCanHideColumnsFromTableTest() {
        String columnName = HeaderName.USER.getStringValue();

        List<String> headersNamesActual = usersAndRolesPage
                .clickColumnsButton()
                .searchColumnValue(columnName)
                .clickColumnCheckbox(columnName)
                .clickApplyButton()
                .getUserAndRolesPageTable()
                .getAllHeadersNames();

        Assert.assertFalse(headersNamesActual.contains(columnName),
                "Headers names are not as expected!");
    }

    @Issue("INVHUB-2405")
    @TmsLink("INVHUB-1051")
    @Test(retryAnalyzer = Retry.class, description = "Verify Users can be sorted by 'User' column")
    public void checkUserColumnIsSortableTest() {
        SoftAssert softAssert = new SoftAssert();
        HeaderName headerName = HeaderName.USER;
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1087")
    @Test(retryAnalyzer = Retry.class, description = "Verify Users can be sorted by 'Roles' column")
    public void checkRolesColumnIsSortableTest() {
        SoftAssert softAssert = new SoftAssert();
        HeaderName headerName = HeaderName.ROLES;
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    //TODO Add the email field error and toast notification error assertions after the bug is fixed
    @Issue("INVHUB-2759")
    @TmsLink("INVHUB-1265")
    @Test(retryAnalyzer = Retry.class, description = "Verify two users with the same email cannot be created")
    public void checkTwoUsersWithTheSameEmailCannotBeCreatedTest() {
        UserDto userDto = createAdminUserInstance();
        createUserWithData(userDto);
        createUserWithData(userDto);

        //TODO Add the email field error and toast notification error assertions here after the bug is fixed
        List<String> userColumnData = usersAndRolesPage
                .searchUser(userDto.getLastName())
                .getUserAndRolesPageTable()
                .getUserColumnData();
        verifyUserEmail(userColumnData, userDto);
    }

    private void sortTableColumnsAndCheck(SoftAssert softAssert, HeaderName headerName,
                                          TableGeneralComponent.SortStatus sortType) {
        usersAndRolesPage
                .getUserAndRolesPageTable()
                .sortTable(headerName, sortType);
        softAssert.assertEquals(usersAndRolesPage.getUserAndRolesPageTable().waitForTableContent()
                        .getColumnData(headerName.stringValue),
                usersAndRolesPage.getUserAndRolesPageTable().waitForTableContent()
                        .getListByDirection(headerName.stringValue, sortType),
                headerName.getStringValue() + " column should be sorted properly, sort type is: " + sortType);
    }

    private UsersPage createUserWithData(UserDto userDto) {
        usersAndRolesPage
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
        return newUserPage.clickSaveButton();
    }

    private void verifyUserEmail(List<String> userColumnData, UserDto userDto) {
        final String fullName = userDto.getFullName();
        String email = userDto.getEmail();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(userColumnData.size())
                    .as("Only one user '%s' had to be created", fullName).isEqualTo(1);
            softly.assertThat(userColumnData.get(0))
                    .as("The created user '%s' should be present in the table", fullName).isEqualTo(fullName);
            softly.assertThat(usersAndRolesPage.openUserProfile(fullName).getUserEmail())
                    .as("The user's '%s' email '%s' hasn't been correct".formatted(fullName, email)).isEqualTo(email);
        });
    }
}
