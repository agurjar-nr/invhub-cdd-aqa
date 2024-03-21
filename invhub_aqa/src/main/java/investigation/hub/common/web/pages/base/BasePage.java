package investigation.hub.common.web.pages.base;

import com.smile.components.Page;
import investigation.hub.common.web.pages.queues.QueuesManagementPage;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

@Getter
@Log4j2
@Page
public class BasePage {

    @Step("'{messageText}' message is displayed")
    public BasePage messageIsDisplayed(String messageText) {
        $x("//p[text()='%s']".formatted(messageText)).shouldBe(visible);
        log.info(messageText + " message is displayed");
        return this;
    }
}
