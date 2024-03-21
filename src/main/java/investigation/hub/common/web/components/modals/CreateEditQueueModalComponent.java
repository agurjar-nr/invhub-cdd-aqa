package investigation.hub.common.web.components.modals;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.smile.components.ModalComponent;
import investigation.hub.common.web.pages.queues.QueuesDetailsPage;
import investigation.hub.common.web.pages.queues.QueuesManagementPage;
import investigation.hub.common.web.test.data.dtos.QueueDto;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

@Log4j2
@ModalComponent
public class CreateEditQueueModalComponent {
    private final SelenideElement container = $x("//div[@class='dn-modal ']");
    private final SelenideElement title = container.$x(".//h2[@class='dn-panel-title']");
    private final SelenideElement nameField = container.$x(".//label[text()='Name']/..//input[@placeholder='Enter Name']");
    private final SelenideElement nameErrorMessage = nameField.$x("./following::p[contains(@class, 'text-sm')]");
    private final SelenideElement descriptionField = container.$x(".//label[text()='Description']/..//textarea[@placeholder='Enter Description']");
    private final SelenideElement logicField = container.$x(".//label[text()='Logic']/..//textarea");
    private final SelenideElement logicErrorMessage = logicField.$x("./following::p[contains(@class, 'text-sm')]");
    //Create/Update button
    private final SelenideElement submitButton = container.$x(".//button[@type='submit']");

    public CreateEditQueueModalComponent(String popupTitle) {
        waitForLoad(popupTitle);
    }

    public void waitForLoad(String popupTitle) {
        title.shouldHave(Condition.textCaseSensitive(popupTitle));
        log.info(popupTitle + " modal is opened");
    }

    @Step("Create Queue modal is not visible")
    public QueuesManagementPage createQueueModalIsNotVisible() {
        container.shouldNotBe(visible);
        return new QueuesManagementPage();
    }

    @Step("Edit Queue modal is not visible")
    public QueuesDetailsPage editQueueModalIsNotVisible() {
        container.shouldNotBe(visible);
        return new QueuesDetailsPage();
    }

    @Step("User enters mandatory Queue data")
    public CreateEditQueueModalComponent enterMandatoryQueueData(QueueDto queue) {
        nameField.sendKeys(queue.getName());
        log.info("Enter Queue name: " + queue.getName());
        logicField.sendKeys(queue.getLogic());
        log.info("Enter Queue logic: " + queue.getLogic());
        return this;
    }

    @Step("User clears mandatory Queue data")
    public CreateEditQueueModalComponent clearMandatoryQueueData() {
        nameField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        log.info("Clear Queue name");
        logicField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        log.info("Clear Queue Logic");
        return this;
    }

    @Step("User enters Queue name")
    public CreateEditQueueModalComponent enterQueueName(String name) {
        nameField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        nameField.sendKeys(name);
        log.info("Enter Queue name: " + name);
        return this;
    }

    @Step("User enters Queue description")
    public CreateEditQueueModalComponent enterQueueDescription(String description) {
        descriptionField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        descriptionField.sendKeys(description);
        log.info("Enter Queue description: " + description);
        return this;
    }

    @Step("User clicks Submit button")
    public void clickSubmitButton() {
        submitButton.click();
        log.info("Click 'Submit' button");
    }

    @Step("Submit button is disabled")
    public CreateEditQueueModalComponent submitButtonIsDisabled() {
        submitButton.shouldBe(Condition.disabled);
        return this;
    }

    @Step("Validation messages for Queue mandatory fields are displayed")
    public CreateEditQueueModalComponent mandatoryFieldsValidationMessagesAreDisplayed() {
        String nameErrorMessageText = "Name is required";
        nameErrorMessage.shouldHave(Condition.textCaseSensitive(nameErrorMessageText));
        log.info(nameErrorMessageText + " message is displayed");

        String logicErrorMessageText = "logic is invalid";
        logicErrorMessage.shouldHave(Condition.textCaseSensitive(logicErrorMessageText));
        log.info(logicErrorMessageText + " message is displayed");
        return this;
    }
}
