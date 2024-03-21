package investigation.hub.common.web.components;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.PageComponent;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Keys;

@Getter
@Log4j2
@PageComponent
public class UserPreferencesPageComponent extends PageGeneralComponent {

    private final SelenideElement container = $("form");

    @Step("User change `Time Zone` in system to other one")
    public UserPreferencesPageComponent setTimezoneToNextOne() {
        container
                .$x(".//span[contains(text(), 'Time Zone')]/following-sibling::div//button")
                .as("TimeZone field button")
                .click();
        SelenideElement timeZoneList = container.$("ul");
        timeZoneList.sendKeys(Keys.ARROW_DOWN);
        timeZoneList.sendKeys(Keys.ENTER);
        return this;
    }

    public boolean isUpdateButtonEnabled() {
        return container.$("button[type='submit']")
                .as("`Update` button")
                .isEnabled();
    }
}