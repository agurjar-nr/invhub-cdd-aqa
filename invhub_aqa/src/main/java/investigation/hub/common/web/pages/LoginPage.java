package investigation.hub.common.web.pages;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

import com.smile.components.Page;
import investigation.hub.common.core.services.api.client.LoginClient;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Page
public class LoginPage {

    @Step("User enters email")
    public LoginPage enterEmail(String email){
        $("#email")
                .as("Email field")
                .sendKeys(email);
        log.info("Enter email: " + email);
        return this;
    }

    @Step("User clicks send code button")
    public LoginPage clickSendCodeButton() {
        $("button[type='submit']")
                .as("Submit button")
                .click();
        log.info("Click 'Submit' button");


        return this;
    }

    @Step("User enters verification code")
    public LoginPage enterCode(String code) {
        $("#loginCode")
                .as("Code field")
                .sendKeys(code);
        log.info("Enter verification code: " + code);
        return this;
    }

    public boolean isWrongEmailMessageAppeared() {
        return $x("//p[contains(text(),'You have entered an invalid email format')]")
                .as("Invalid email warning")
                .isDisplayed();
    }

    public boolean isWrongCodeMessageAppeared() {
        return $x("//div[contains(text(),'Error: Expired or invalid login code') or contains(text(),'Error: Invalid login credentials.')]")
                .as("Invalid Code warning")
                .isDisplayed();
    }

    public boolean isRequiredFieldMessageAppeared() {
        return $x("//p[contains(text(),'This field is required.')]")
                .as("Invalid field warning")
                .isDisplayed();
    }

    public boolean isSymphonyLogoShown() {
        return $("img[alt='Symphony AI Sensa NetReveal']")
                .as("Symphony AI logo")
                .isEnabled();
    }

    @Step("User login with email and verification code")
    public AllOpenInvestigationsPage loginUser(String email, LoginClient loginClient) {
        enterEmail(email)
                .clickSendCodeButton()
                .enterCode(loginClient.getLoginCodeForEmail(email))
         .clickSendCodeButton();
        return new AllOpenInvestigationsPage();
    }
}
