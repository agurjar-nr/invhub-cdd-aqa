package investigation.hub.common.web.components.modals;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.smile.components.ModalComponent;
import investigation.hub.common.web.components.NarrativePageComponent;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$x;

@Log4j2
@ModalComponent
public class UnsavedChangesModalComponent {

    private final SelenideElement container =
            $x("//div[contains(@class, 'dn-modal-container') and .//h2[text()='Unsaved changes']]");

    @Step("User clicks Save changes button")
    public NarrativePageComponent clickSaveChangesButton() {
        container
                .$x(".//button[.//text()='Save changes']")
                .as("Save Changes button")
                .shouldBe(Condition.enabled, Duration.ofSeconds(60))
                .click();
        log.info("Click Save changes button");
        return new NarrativePageComponent();
    }

    @Step("User clicks Leave without saving button")
    public NarrativePageComponent clickLeaveWithoutSavingButton() {
        container
                .$x(".//button[.//text()='Leave without saving']")
                .as("Leave without saving button")
                .shouldBe(Condition.enabled, Duration.ofSeconds(60))
                .click();
        log.info("Click Leave without saving button");
        return new NarrativePageComponent();
    }
}
