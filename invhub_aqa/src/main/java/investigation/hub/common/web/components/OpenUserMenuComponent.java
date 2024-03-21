package investigation.hub.common.web.components;

import static com.codeborne.selenide.Selenide.$x;

import com.smile.components.PageComponent;
import investigation.hub.common.web.pages.LoginPage;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
@PageComponent
public class OpenUserMenuComponent extends PageGeneralComponent {

    @Step("User clicks Logout button")
    public LoginPage clickLogoutButton() {
        $x("//a[contains(@href,'logout')]")
                .as("Logout button")
                .click();
        log.info("Click Logout button");
        return new LoginPage();
    }

    @Step("User clicks `Preferences` button")
    public UserPreferencesPageComponent clickUserPreferencesButton() {
        $x("//a[contains(@href,'edit-user-preferences')]")
                .as("Preferences button")
                .click();
        return new UserPreferencesPageComponent();
    }
}
