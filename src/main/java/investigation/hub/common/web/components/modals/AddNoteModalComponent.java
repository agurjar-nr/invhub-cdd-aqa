package investigation.hub.common.web.components.modals;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.ModalComponent;
import investigation.hub.common.web.pages.InvestigationPage;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

import static com.codeborne.selenide.Selenide.$;

@Log4j2
@ModalComponent
public class AddNoteModalComponent {

    private final SelenideElement container = $(".dn-modal");

    @Step
    public AddNoteModalComponent enterText(String text) {
        container
                .$("#noteBody")
                .as("Add Note text field")
                .sendKeys(text);
        log.info("Enter text into Add Note text field");
        return this;
    }

    @Step
    public InvestigationPage clickSaveButton() {
        container
                .$("button[type='submit']")
                .as("Save button of Add Note modal")
                .click();
        log.info("Click Save button on Add Note modal");
        return new InvestigationPage();
    }

    @Step
    public InvestigationPage closeModal() {
        container
                .$("button[title='Close']")
                .as("Close button of Add Note modal")
                .click();
        log.info("Click Close button on Add Note modal");
        return new InvestigationPage();
    }
}
