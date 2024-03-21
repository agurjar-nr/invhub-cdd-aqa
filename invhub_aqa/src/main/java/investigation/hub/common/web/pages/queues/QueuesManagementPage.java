package investigation.hub.common.web.pages.queues;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.smile.components.Page;
import investigation.hub.common.web.components.PaginationComponent;
import investigation.hub.common.web.components.modals.CreateEditQueueModalComponent;
import investigation.hub.common.web.components.tables.QueuesTablePageComponent;
import investigation.hub.common.web.pages.base.BasePage;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

@Getter
@Log4j2
@Page
public class QueuesManagementPage extends BasePage {

    private final QueuesTablePageComponent queuesTablePageComponent =
            new QueuesTablePageComponent();

    private final PaginationComponent paginationPageComponent =
            new PaginationComponent();

    private final SelenideElement pageTitle = $x("//p[contains(@class, '22px')]");
    private final SelenideElement createQueueButton = $x("//button//*[contains(text(), 'Create Queue')]/ancestor::button");

    public QueuesManagementPage() {
        waitForLoad();
    }

    public void waitForLoad() {
        pageTitle.shouldHave(Condition.textCaseSensitive("Queues Management"));
        log.info("Queues Management page is opened");
    }

    @Step("User clicks on Create Queue button")
    public CreateEditQueueModalComponent clickOnCreateQueueButton() {
        createQueueButton.click();
        return new CreateEditQueueModalComponent("Create Queue");
    }
}
