package investigation.hub.common.web.components;

import com.smile.components.PageComponent;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Log4j2
@PageComponent
public class PaginationComponent {

    @Step("User clicks next button")
    public void clickNextButton() {
        $(".next.queuesManagement-panel-pagination-next-button").click();
        log.info("Opened next page");
    }

    @Step("User clicks previous button")
    public void clickPreviousButton() {
        $(".previous.queuesManagement-panel-pagination-previous-button").click();
        log.info("Opened previous page");
    }

    @Step("User clicks button by number {number}")
    public void clickButtonByNumber(int number) {
        $$("button.page.queuesManagement-panel-pagination-pagenum")
                .findBy(text(String.valueOf(number))).click();
        log.info("Opened: " + number + "page");
    }
}
