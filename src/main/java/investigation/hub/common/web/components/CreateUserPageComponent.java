package investigation.hub.common.web.components;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import investigation.hub.common.web.components.modals.PhotoUploadModalComponent;
import investigation.hub.common.web.test.data.constants.TimeZone;
import investigation.hub.common.web.test.data.constants.Weekday;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Keys;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$x;

@Log4j2
@Component
public class CreateUserPageComponent {

    private final SelenideElement container = $x("//div[contains(@class, 'divide-y') and .//text()='Create User']");

    @Step("User enters First Name")
    public CreateUserPageComponent enterFirstName(String firstName) {
        container
                .$("#first_name")
                .as("First Name field")
                .shouldBe(Condition.enabled, Duration.ofSeconds(3))
                .sendKeys(Keys.chord(Keys.CONTROL, "a"), firstName);
        log.info("Enter First Name: " + firstName);
        return this;
    }

    @Step("User enters Last Name")
    public CreateUserPageComponent enterLastName(String lastName) {
        container
                .$("#last_name")
                .as("Last Name field")
                .sendKeys(Keys.chord(Keys.CONTROL, "a"), lastName);
        log.info("Enter Last Name: " + lastName);
        return this;
    }

    @Step("User clicks Upload button")
    public PhotoUploadModalComponent clickUploadButton() {
        container
                .$x(".//div[contains(text(),'Upload')]")
                .as("Upload button")
                .click();
        log.info("Click 'Upload' button");
        return new PhotoUploadModalComponent();
    }

    @Step("User enters Email")
    public CreateUserPageComponent enterEmail(String email) {
        container
                .$("#email")
                .as("Email field")
                .sendKeys(Keys.chord(Keys.CONTROL, "a"), email);
        log.info("Enter Email: " + email);
        return this;
    }

    @Step("User clicks Language dropdown")
    public CreateUserPageComponent clickLanguageDropdown() {
        container
                .$x(".//span[contains(text(),'Language')]/parent::div//button")
                .as("Language dropdown")
                .click();
        log.info("Click Language dropdown");
        return this;
    }

    @Step("User selects Language")
    public CreateUserPageComponent selectLanguage(String language) {
        container
                .$x(".//li//span[contains(text(),'%s')]".formatted(language))
                .as("Language")
                .click();
        log.info("Select Language: " + language);
        return this;
    }

    @Step("User clicks on Week starts on dropdown")
    public CreateUserPageComponent clickWeekStarsOnDropdown() {
        container
                .$x(".//span[contains(text(),'Week starts on')]/parent::div//button")
                .as("Week starts on dropdown")
                .click();
        log.info("Click Week starts on dropdown");
        return this;
    }

    @Step("User selects Week starts on Day")
    public CreateUserPageComponent selectWeekStartsOnDay(Weekday weekday) {
        String day = weekday.getWeekday();
        container
                .$x(".//li//span[contains(text(),'%s')]".formatted(day))
                .as("Weekday")
                .click();
        log.info("Select Week starts on Day: " + day);
        return this;
    }

    @Step("User clicks on Time Zone dropdown")
    public CreateUserPageComponent clickTimeZoneDropdown() {
        container
                .$x(".//span[contains(text(),'Time Zone')]/parent::div//button")
                .as("Time Zone dropdown")
                .click();
        log.info("Click Time Zone dropdown");
        return this;
    }

    @Step("User selects Time Zone")
    public CreateUserPageComponent selectTimeZone(TimeZone timeZone1) {
        String timeZone = timeZone1.getTimeZone();
        container
                .$x(".//li//span[contains(text(),'%s')]".formatted(timeZone))
                .as("Time zone")
                .click();
        log.info("Select time zone: " + timeZone);
        return this;
    }
}
