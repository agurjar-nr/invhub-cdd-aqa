package investigation.hub.common.web.pages.queues;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.smile.components.Page;
import investigation.hub.common.web.components.modals.CreateEditQueueModalComponent;
import investigation.hub.common.web.pages.base.BasePage;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

@Getter
@Log4j2
@Page
public class QueuesDetailsPage extends BasePage {

    private final SelenideElement pageTitle = $x("//div[contains(@class, 'leading-none')]");
    private final SelenideElement editButton = $x("//button[@aria-label='Edit']");

    public QueuesDetailsPage() {
        waitForLoad();
    }

    public void waitForLoad() {
        pageTitle.shouldHave(Condition.textCaseSensitive("Queues Management"));
        log.info("Queues Management page is opened");
    }

    @Step("User clicks on Edit Queue button")
    public CreateEditQueueModalComponent clickOnEditButton() {
        editButton.click();
        return new CreateEditQueueModalComponent("Edit Queue Details");
    }
}
