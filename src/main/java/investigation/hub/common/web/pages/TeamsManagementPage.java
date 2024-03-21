package investigation.hub.common.web.pages;

import static com.codeborne.selenide.Selenide.$x;

import com.smile.components.Page;
import investigation.hub.common.web.components.modals.CreateTeamModalComponent;
import investigation.hub.common.web.components.tables.TeamsManagementTablePageComponent;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
@Page
public class TeamsManagementPage {

    private final TeamsManagementTablePageComponent teamsManagementTablePageComponent =
            new TeamsManagementTablePageComponent();

    @Step("User clicks Create Team button")
    public CreateTeamModalComponent clickCreateTeamButton() {
        $x("//div[contains(text(),'Create Team')]")
                .as("Create Team button")
                .click();
        log.info("Click 'Create Team' button");
        return new CreateTeamModalComponent();
    }

    public boolean isTeamCreatedSuccessfullyMessageAppeared() {
        return $x("//p[contains(text(),'Team created successfully')]")
                .as("Team created successfully message")
                .isDisplayed();
    }

    public boolean isCreateTeamButtonEnabled() {
        return $x("//div[contains(text(),'Create Team')]/ancestor::button")
                .as("Create Team button")
                .isEnabled();
    }
}
