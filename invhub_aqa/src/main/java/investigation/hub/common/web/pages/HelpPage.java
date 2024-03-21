package investigation.hub.common.web.pages;

import com.codeborne.selenide.Condition;
import com.smile.components.Page;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

/**
 * We do not validate the contents of Help page, so no page description here
 */
@Getter
@Log4j2
@Page
public class HelpPage {

    public void waitForLoad() {
        $("div.Row-three-home-tiles")
                .as("Help page main menu")
                .shouldBe(Condition.visible);
        log.info("Help page main menu should be visible");
    }

    public boolean isPageTitleVisible() {
       return $x("//h3[contains(text(),'Investigation Hub Online Help')]")
               .isDisplayed();
    }
}
