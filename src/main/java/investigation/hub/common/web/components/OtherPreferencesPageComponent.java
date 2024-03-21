package investigation.hub.common.web.components;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.PageComponent;
import investigation.hub.common.web.enums.InvHubDateFormat;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import static com.codeborne.selenide.Selenide.$x;

@Getter
@Log4j2
@PageComponent
public class OtherPreferencesPageComponent extends PageGeneralComponent {

    private final SelenideElement container = $x("//span[contains(text(), 'Other Preferences')]/ancestor::form");
    private final SelenideElement currencyDropdownButton = container.$$("button").get(0);
    private final SelenideElement dateFormatDropdownButton = container.$$("button").get(1);

    @Step("User selects currency")
    public OtherPreferencesPageComponent selectCurrency(String currency) {
        currencyDropdownButton
                .as("Currency dropdown")
                .click();
        container.$x(".//span[contains(text(), '" + currency + "')]/ancestor::li")
                .as(currency + " currency")
                .click();

        log.info("Select currency: " + currency);
        return this;
    }

    @Step("User selects date format")
    public OtherPreferencesPageComponent selectDateFormat(InvHubDateFormat dateTimeFormat) {
        String dateFormatStringValue = dateTimeFormat.getUiValue();

        dateFormatDropdownButton
                .as("Date format dropdown")
                .click();
        container.$x(".//span[contains(text(), '" + dateFormatStringValue + "')]/ancestor::li")
                .as(dateFormatStringValue + " date time format")
                .click();

        log.info("Select date format: " + dateFormatStringValue);
        return this;
    }

    @Step("User clicks Update button")
    public OtherPreferencesPageComponent clickUpdateButton() {
        $x("//div[contains(text(),'Update')]/ancestor::button")
                .click();
        log.info("Click Update button");
        return this;
    }

    public boolean isCurrencyAlreadySetAsDefault(String currency) {
        return currencyDropdownButton
                .$x(".//span[contains(text(),'" + currency + "')]")
                .isDisplayed();
    }

    public boolean isDateFormatAlreadySetAsDefault(String dateFormat) {
        return dateFormatDropdownButton
                .$x(".//span[contains(text(),'" + dateFormat + "')]")
                .isDisplayed();
    }

    public boolean isSystemPreferencesSavedSuccessfullyMessageAppeared() {
        return $x("//p[contains(text(),'System Preferences saved successfully')]")
                .as("System Preferences saved successfully message")
                .isDisplayed();
    }

}
