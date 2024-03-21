package investigation.hub.common.web.components.modals;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.ModalComponent;
import investigation.hub.common.web.pages.TeamsManagementPage;
import investigation.hub.common.web.test.data.constants.AccessPolicy;
import investigation.hub.common.web.test.data.constants.Role;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Keys;

@Log4j2
@ModalComponent
public class UpdateTeamModalComponent {
    private final SelenideElement container = $(".dn-modal");

    @Step("User updates Team Name")
    public UpdateTeamModalComponent updateName(String name) {
        container
                .$("#name")
                .as("Team Name field")
                .sendKeys(Keys.chord(Keys.CONTROL, "a"), name);
        log.info("Update Team Name: " + name);
        return this;
    }

    @Step("User updates Team Description")
    public UpdateTeamModalComponent updateDescription(String description) {
        container
                .$("textarea[placeholder='Enter Description']")
                .as("Team Description field")
                .sendKeys(Keys.chord(Keys.CONTROL, "a"), description);
        log.info("Update Team Description: " + description);
        return this;
    }

    @Step("User updates Role")
    public UpdateTeamModalComponent updateRole(Role role) {
        String teamUsersRole = role.getStringValue();
        container
                .$x(".//label[contains(text(),'Role')]/following-sibling::div//button")
                .as("Role dropdown")
                .click();
        container
                .$x(".//li//span[contains(text(),'%s')]".formatted(teamUsersRole))
                .as("Role")
                .click();
        log.info("Update role: " + teamUsersRole);
        return this;
    }

    @Step("User updates Access Policy")
    public UpdateTeamModalComponent updateAccessPolicy(AccessPolicy accessPolicy) {
        String policy = accessPolicy.getStringValue();
        container
                .$x(".//label[contains(text(),'Access Policy')]/following-sibling::div//button")
                .as("Access Policy dropdown")
                .click();
        container
                .$x(".//li//span[contains(text(),'%s')]".formatted(policy))
                .as("Access Policy")
                .click();
        log.info("Update access policy: " + policy);
        return this;
    }

    @Step("User clicks Update button")
    public TeamsManagementPage clickUpdateButton() {
        container.$x(".//div[text()='Update']")
                .as("Update button")
                .click();
        log.info("Click 'Update' button");
        return new TeamsManagementPage();
    }
}
