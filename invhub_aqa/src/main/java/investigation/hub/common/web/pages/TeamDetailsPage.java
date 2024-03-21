package investigation.hub.common.web.pages;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

import com.smile.components.Page;
import investigation.hub.common.web.components.modals.AddMembersToTeamModalComponent;
import investigation.hub.common.web.components.modals.RemoveConfirmationModalComponent;
import investigation.hub.common.web.components.modals.UpdateTeamModalComponent;
import investigation.hub.common.web.components.tables.TeamMembersTableComponent;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
@Page
public class TeamDetailsPage {

    private final TeamMembersTableComponent teamMembersTableComponent =
            new TeamMembersTableComponent();
    private final RemoveConfirmationModalComponent removeConfirmationModalComponent =
            new RemoveConfirmationModalComponent();

    @Step("User clicks Remove Team button")
    public RemoveConfirmationModalComponent clickRemoveTeamButton() {
        $x("//span[text()='Remove Team']")
                .as("Remove Team button")
                .click();
        log.info("Click 'Remove Team' button");
        return new RemoveConfirmationModalComponent();
    }

    @Step("User clicks Edit Team button")
    public UpdateTeamModalComponent clickEditTeamButton() {
        $("button[aria-label= 'Edit']")
                .as("Edit Team button")
                .click();
        log.info("Click 'Edit Team' button");
        return new UpdateTeamModalComponent();
    }

    @Step("User clicks Add Team Members button")
    public AddMembersToTeamModalComponent clickAddTeamMembersButton() {
        $("button[aria-label= 'Add Team Members']")
                .as("Add Team Members button")
                .click();
        log.info("Click 'Add Team Members' button");
        return new AddMembersToTeamModalComponent();
    }

    @Step("User clicks Remove from Team button")
    public RemoveConfirmationModalComponent clickRemoveFromTeamButton() {
        $x("//span[.//text()='Remove from Team']/parent::button")
                .as("Remove from Team button")
                .click();
        log.info("Click Remove from Team button");
        return new RemoveConfirmationModalComponent();
    }

    public boolean isTeamRemovedSuccessfullyMessageAppeared() {
        return  $x("//p[contains(text(),'Team removed successfully')]")
                .as("Team removed successfully message")
                .isDisplayed();
    }

    public boolean isTeamDetailsUpdatedMessageAppeared() {
        return $x("//p[text()='Team details updated']")
                .as("Team details updated message")
                .isDisplayed();
    }

    public boolean isMembersWereSuccessfullyAddedToTeamMessageAppeared() {
        return $x("//p[text()='Members were successfully added to Team.']")
                .as("Members were successfully added to Team. message")
                .isDisplayed();
    }

    public boolean isTeamMembersRemovedSuccessfullyMessageAppeared() {
        return $x("//p[text()='Team members removed successfully']")
                .as("Team members removed successfully message")
                .isDisplayed();
    }

    public String getTeamName() {
        return $x(".//span[text()='Name']/following-sibling::span")
                .as("Team name")
                .getText();
    }

    public String getTeamDescription() {
        return $x(".//span[text()='Description']/following-sibling::span")
                .as("Team description")
                .getText();
    }

    public String getTeamRole() {
        return $x(".//span[text()='Role']/following-sibling::span")
                .as("Team role")
                .getText();
    }
}
