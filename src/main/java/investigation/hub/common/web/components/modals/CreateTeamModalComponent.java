package investigation.hub.common.web.components.modals;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.ModalComponent;
import investigation.hub.common.web.pages.TeamsManagementPage;
import investigation.hub.common.web.test.data.constants.AccessPolicy;
import investigation.hub.common.web.test.data.constants.Role;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ModalComponent
public class CreateTeamModalComponent {
    private final SelenideElement container = $(".dn-modal");

    @Step("User enters Team Name")
    public CreateTeamModalComponent enterName(String name) {
        container
                .$("#name")
                .as("Team Name field")
                .sendKeys(name);
        log.info("Enter Team Name: " + name);
        return this;
    }

    @Step("User enters Team Description")
    public CreateTeamModalComponent enterDescription(String description) {
        container
                .$("textarea[placeholder='Enter Description']")
                .as("Team Description field")
                .sendKeys(description);
        log.info("Enter Team Description: " + description);
        return this;
    }

    @Step("User selects Role")
    public CreateTeamModalComponent selectRole(Role role) {
        String teamUsersRole = role.getStringValue();
        container
                .$x(".//label[contains(text(),'Role')]/following-sibling::div//button")
                .as("Role dropdown")
                .click();
        container
                .$x(".//li//span[contains(text(),'%s')]".formatted(teamUsersRole))
                .as("Role")
                .click();
        log.info("Select role: " + teamUsersRole);
        return this;
    }

    @Step("User selects Access Policy")
    public CreateTeamModalComponent selectAccessPolicy(AccessPolicy accessPolicy) {
        String policy = accessPolicy.getStringValue();
        container
                .$x(".//label[contains(text(),'Access Policy')]/following-sibling::div//button")
                .as("Access Policy dropdown")
                .click();
        container
                .$x(".//li//span[contains(text(),'%s')]".formatted(policy))
                .as("Access Policy")
                .click();
        log.info("Select access Policy: " + policy);
        return this;
    }

    @Step("User clicks Create button")
    public TeamsManagementPage clickCreateButton() {
        container.$x(".//div[text()='Create']")
                .as("Create button")
                .click();
        log.info("Click 'Create' button");
        return new TeamsManagementPage();
    }
}
