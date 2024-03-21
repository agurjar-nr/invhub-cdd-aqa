package investigation.hub.common.web.components;

import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.PageComponent;
import investigation.hub.common.web.pages.InvestigationPage;
import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Keys;

@Getter
@Log4j2
@PageComponent
public class SMTPSettingsPageComponent extends PageGeneralComponent {

    private final SelenideElement container = $x("//span[contains(text(), 'SMTP Settings')]/ancestor::form");

    private final SelenideElement hostNameField = container.$("#hostName");

    private final SelenideElement portField = container.$("#port");

    private final SelenideElement userNameField = container.$("#userName");

    private final SelenideElement passwordField = container.$("#password");

    private final SelenideElement fromEmailField = container.$("#fromEmail");

    private final SelenideElement fromNameField = container.$("#fromName");

    @Step("User types new host name")
    public SMTPSettingsPageComponent enterNewHostName(String hostName) {
        hostNameField.sendKeys(Keys.chord(Keys.CONTROL, "a"), hostName);
        log.info("Enter host name: " + hostName);
        return this;
    }

    @Step("User gets host name value")
    public String getHostName() {
        log.info("Get host name");
        return hostNameField.getValue();
    }

    @Step("User types new port")
    public SMTPSettingsPageComponent enterPortName(String port) {
        portField.sendKeys(Keys.chord(Keys.CONTROL, "a"), port);
        log.info("Enter port: " + port);
        return this;
    }

    @Step("User gets port field value")
    public String getPortValue() {
        log.info("Get port");
        return portField.getValue();
    }

    @Step("User types new user name")
    public SMTPSettingsPageComponent enterUserName(String userName) {
        userNameField.sendKeys(Keys.chord(Keys.CONTROL, "a"), userName);
        log.info("Enter user name: " + userName);
        return this;
    }

    @Step("User gets own userId")
    public String getUserName() {
        log.info("Get userId");
        return userNameField.getValue();
    }

    @Step("User gets `Password` field value (asterisks)")
    public String getPassword() {
        log.info("gets `Password` field value (asterisks)");
        return passwordField.getValue();
    }

    @Step("User types new email")
    public SMTPSettingsPageComponent enterEmail(String email) {
        fromEmailField.sendKeys(Keys.chord(Keys.CONTROL, "a"), email);
        log.info("Enter email: " + email);
        return this;
    }

    @Step("User gets `From Email` field value")
    public String getFromEmail() {
        log.info("Get email");
        return fromEmailField.getValue();
    }

    @Step("User types new from name")
    public SMTPSettingsPageComponent enterFromName(String name) {
        fromNameField.sendKeys(Keys.chord(Keys.CONTROL, "a"), name);
        log.info("Enter from name: " + name);
        return this;
    }

    @Step("User gets `From Email` field value")
    public String getFromName() {
        log.info("Get from name");
        return fromNameField.getValue();
    }

    @Step("User types new password")
    public SMTPSettingsPageComponent enterPassword(String password) {
        passwordField.sendKeys(Keys.chord(Keys.CONTROL, "a"), password);
        log.info("Enter new password: " + password);
        return this;
    }

    @Step("User clicks cancel button")
    public InvestigationPage clickCancelButton() {
        $x("//div[contains(text(),'Cancel')]")
                .as("Cancel button")
                .click();
        log.info("Click 'Cancel' button");
        return new InvestigationPage();
    }

    @Step("User clicks verify button")
    public SMTPSettingsPageComponent clickVerifyButton() {
        $x("//div[contains(text(),'Verify')]")
                .as("Verify button")
                .click();
        log.info("Click 'Verify' button");
        return this;
    }

    @Step("User clicks submit button")
    public SMTPSettingsPageComponent clickSubmitButton() {
        $x("//div[contains(text(),'Submit')]")
                .as("Submit button")
                .click();
        log.info("Click 'Submit' button");
        return this;
    }

    public boolean isSMTPSettingsVerifiedMessageAppeared() {
        return $x("//p[contains(text(),'SMTP Settings successfully verified')]")
                .as("SMTP Settings successfully verified message")
                .isEnabled();
    }

    public boolean isSMTPSettingsSavedAMessageAppeared() {
        return $x("//p[contains(text(),'SMTP Settings saved successfully')]")
                .as("SMTP Settings successfully saved message")
                .isDisplayed();
    }

    public boolean isHeaderSectionVisible() {
        return $x("//div/span[contains(text(), 'SMTP Settings')]")
                .as("SMTP Settings page header")
                .isDisplayed();
    }

    public boolean isSMTPSettingsNOTVerifiedMessageAppeared() {
        return $x("//p[contains(text(),'smtp.settings.not.verified')]")
                .as("SMTP Settings NOT verified message")
                .isEnabled();
    }

    @Getter
    @AllArgsConstructor
    public enum DefaultSMTPSettings {
        HOSTNAME("smtp.postmarkapp.com"),
        PORT("587"),
        USER_NAME("b3526f95-a6f2-4442-8115-b909da638775"),
        PASSWORD("********"),
        FROM_EMAIL("sensa@sensadev.com"),
        FROM_NAME("Sensa");

        public final String stringValue;
    }
}
