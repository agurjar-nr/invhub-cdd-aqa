package investigation.hub.common.web.pages;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

import com.smile.components.Page;
import investigation.hub.common.web.components.modals.ChooseColumnsModalComponent;
import investigation.hub.common.web.components.tables.UserAndRolesPageTableComponent;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
@Page
public class UsersPage {

    UserAndRolesPageTableComponent userAndRolesPageTable = new UserAndRolesPageTableComponent();
    ChooseColumnsModalComponent columns = new ChooseColumnsModalComponent();

    @Step("User clicks Create New User button")
    public NewUserPage clickCreateNewUserButton() {
        $x("//div[contains(text(),'Create New User')]")
                .as("Create New User button")
                .click();
        log.info("Click 'Create New User' button");
        return new NewUserPage();
    }
    @Step("User searches another user by name")
    public UsersPage searchUser(String userName) {
        $("input[placeholder='Search']")
                .as("Search field")
                .sendKeys(userName);
        log.info("Search user by: " + userName);
        return this;
    }

    @Step("User clicks Columns button")
    public ChooseColumnsModalComponent clickColumnsButton() {
        $x("//*[@data-testid='ExpandMoreIcon']/parent::button")
                .as("Columns")
                .click();
        log.info("Click 'Columns' button");
        return new ChooseColumnsModalComponent();
    }

    public boolean isNewUserAddedSuccessfullyMessageAppeared() {
        return $x("//p[text()='New User added successfully']")
                .as("New User added successfully message")
                .isDisplayed();
    }

    @Step("User opens Profile page")
    public UserProfilePage openUserProfile(String fullName) {
        getUserAndRolesPageTable().clickOnRowByUserHeader(fullName);
        log.info("Open user's %s Profile page".formatted(fullName));
        return new UserProfilePage();
    }

    public boolean isButtonCreateNewUserEnabled() {
       return  $x("//div[contains(text(),'Create New User')]")
                .as("Create New User button")
                .isEnabled();
    }
}
