package investigation.hub.common.web.pages;

import com.smile.components.Page;
import investigation.hub.common.web.components.modals.DeactivationModalComponent;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

import static com.codeborne.selenide.Selenide.$x;

@Log4j2
@Page
public class UserProfilePage {

    private final String accessLevelXpath = "//*[text()='%s']/parent::div/following-sibling::div/span";

    @Step("User clicks Edit button")
    public UserEditPage clickEditButton() {
        $x("//div[contains(text(),'Edit')]")
                .as("Edit button")
                .click();
        log.info("Click 'Edit' button");
        return new UserEditPage();
    }

    @Step("User clicks Deactivate button")
    public DeactivationModalComponent clickDeactivateButton() {
        $x("//div[contains(text(),'Deactivate')]")
                .as("Deactivate button")
                .click();
        log.info("Click 'Deactivate' button");
        return new DeactivationModalComponent();
    }

    @Step("User clicks log history button")
    public LogHistoryPage clickLogHistoryButton() {
        $x("//div[contains(text(),'Log History')]")
                .as("Deactivate button")
                .click();
        log.info("Click 'Log History' button");
        return new LogHistoryPage();
    }

    @Step("User clicks Admin App Section")
    public UserProfilePage clickAdminAppSection() {
        $x("//span[contains(text(),'App Section')]/parent::div//div[contains(text(),'Admin')]")
                .as("Admin App Section")
                .click();
        log.info("Click 'Admin' App Section");
        return this;
    }

    @Step("User gets Full Name field value")
    public String getUserFullName() {
        return $x("//dt[contains(text(),'Full Name')]/following-sibling::dd")
                .as("Full Name")
                .getText();
    }

    @Step("User gets Email field value")
    public String getUserEmail() {
        return $x("//dt[contains(text(),'Email')]/following-sibling::dd")
                .as("Email")
                .getText();
    }

    @Step("User gets Language field value")
    public String getUserLanguage() {
        return $x("//dt[contains(text(),'Language')]/following-sibling::dd")
                .as("Language")
                .getText();
    }

    @Step("User gets Week starts On field value")
    public String getUserWeekStartsOn() {
        return $x("//dt[contains(text(),'Week starts on')]/following-sibling::dd")
                .as("Week starts on")
                .getText();
    }

    @Step("User gets Time Zone field value")
    public String getUserTimeZone() {
        return $x("//dt[contains(text(),'Time Zone')]/following-sibling::dd")
                .as("Time Zone")
                .getText();
    }

    @Step("User gets Role field value")
    public String getUserRole() {
        return $x("//dt[contains(text(),'Role')]/following-sibling::dd")
                .as("Role")
                .getText();
    }

    @Step("User gets Investigations Access Level field value")
    public String getInvestigationsAccessLevel() {
        return $x(accessLevelXpath.formatted("investigations"))
                .as("Investigations Access Level")
                .getText();
    }

    @Step("User gets Users and Roles Access Level field value")
    public String getUsersAndRolesAccessLevel() {
        return $x(accessLevelXpath.formatted("users-roles"))
                .as("Users and Roles Access Level")
                .getText();
    }

    @Step("User gets SMTP Settings Access Level field value")
    public String getSmtpSettingsAccessLevel() {
        return $x(accessLevelXpath.formatted("smtp"))
                .as("SMTP Settings Access Level")
                .getText();
    }

    @Step("User gets SAML Settings Access Level field value")
    public String getSamlSettingsAccessLevel() {
        return $x(accessLevelXpath.formatted("saml"))
                .as("SAML Settings Access Level")
                .getText();
    }

    @Step("User gets System Configuration Access Level field value")
    public String getSystemConfigurationAccessLevel() {
        return $x(accessLevelXpath.formatted("system-configuration"))
                .as("System Configuration Access Level")
                .getText();
    }
}
