package investigation.hub.common.web.components.modals;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.ModalComponent;
import investigation.hub.common.web.pages.TeamDetailsPage;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ModalComponent
public class RemoveConfirmationModalComponent {
    private final SelenideElement container = $(".dn-modal");

    @Step("User confirms removal")
    public TeamDetailsPage confirmRemoval() {
        container
                .$x(".//button//div[contains(text(),'Yes, Remove')]")
                .as("'Yes, Remove' button")
                .click();
        log.info("Click 'Yes, Remove' button");
        return new TeamDetailsPage();
    }

    public String getTeamDeletionWillUnsubscribeAllMembersMessage() {
        return container.$x(".//div[contains(@class, 'px-6')]/div")
                .as("This action will unsubscribe all members from the team, and then remove the team. This action "
                        + "is irreversible. message")
                .getText();
    }

}
