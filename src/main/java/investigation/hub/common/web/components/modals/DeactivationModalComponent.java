package investigation.hub.common.web.components.modals;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.ModalComponent;
import investigation.hub.common.web.pages.UsersPage;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

import static com.codeborne.selenide.Selenide.$;

/**
 * Deactivation modal component is a pop-up which appears after clicking 'Deactivate' button
 * on User and Roles page to confirm/cancel deactivation
 */
@Log4j2
@ModalComponent
public class DeactivationModalComponent {

    private final SelenideElement container = $(".rounded-lg");

    @Step("User confirms deactivation")
    public UsersPage confirmDeactivation() {
        container
                .$x(".//button//div[contains(text(),'Deactivate')]")
                .as("Deactivate button")
                .click();
        log.info("Confirm deactivation");
        return new UsersPage();
    }
}
