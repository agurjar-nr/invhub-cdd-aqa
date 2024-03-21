package investigation.hub.common.web.components;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.smile.components.PageComponent;
import investigation.hub.common.web.pages.*;
import investigation.hub.common.web.pages.queues.QueuesManagementPage;
import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static investigation.hub.common.web.components.MainHeaderComponent.AdminDropdown.*;
import static investigation.hub.common.web.components.MainHeaderComponent.TopLineButtons.ADMIN;
import static investigation.hub.common.web.components.MainHeaderComponent.TopLineButtons.OPEN_USER_MENU;

@Log4j2
@PageComponent
public class MainHeaderComponent {

    private final SelenideElement container = $("#app-header");
    private final SelenideElement getNextButton = container.$x(".//button[text()='Get Next']");
    private final SelenideElement helpButton = container.$("a");

    @Step("Open All Open Investigations page")
    public AllOpenInvestigationsPage openAllOpenInvestigations() {
        container
                .$x(".//button/div[contains(text(), 'Investigations')]")
                .as("Investigations button")
                .click();
        container
                .$x(".//p[contains(text(), 'All Open Investigations')]")
                .as("All Open Investigations dropdown")
                .click();
        log.info("Open All Open Investigations page");
        return new AllOpenInvestigationsPage();
    }

    @Step("Open `Users` page")
    public UsersPage openUsers() {
        openDropdownItems(USERS);
        return new UsersPage();
    }

    @Step("Open System Configuration page")
    public SystemConfigurationPage openSystemConfiguration() {
        openDropdownItems(SYSTEM_CONFIGURATION);
        return new SystemConfigurationPage();
    }

    @Step("Open Teams Management page")
    public TeamsManagementPage openTeamsManagement() {
        openDropdownItems(TEAMS_MANAGEMENT);
        return new TeamsManagementPage();
    }

    @Step("Open `Queues Management` page")
    public QueuesManagementPage openQueuesManagement() {
        openDropdownItems(QUEUES_MANAGEMENT);
        return new QueuesManagementPage();
    }

    @Step("Open Data Access page")
    public DataAccessPage openDataAccess() {
        openDropdownItems(DATA_ACCESS);
        return new DataAccessPage();
    }

    @Step("Click Get Next button")
    public MainHeaderComponent clickGetNextButton() {
        getNextButton.click();
        log.info("Click Get Next button");
        return this;
    }

    @Step("Click Help button")
    public MainHeaderComponent clickHelpButton() {
        helpButton.click();
        log.info("Click Help button");
        return this;
    }

    private void clickAdminButton() {
        container
                .$x(".//button/div[contains(text(), 'Admin')]")
                .parent()
                .as("Admin button")
                .click();
        log.info("Click Admin button");
    }


    public boolean isAdminButtonVisible() {
        return container.$x(".//div[contains(text(), '%s')]/ancestor::button".formatted(ADMIN.getStringValue()))
                .as("Admin button")
                .isDisplayed();
    }

    public void openDropdownItems(AdminDropdown item) {
        clickAdminButton();
        container
                .$x(".//p[contains(text(), '%s')]/ancestor::a".formatted(item.getStringValue()))
                .as(item.getStringValue() + " dropdown item")
                .click();
        log.info("Open `%s` page".formatted(item.getStringValue()));
    }

    @Step("Get accessible for user `Admin` dropdown items")
    public List<String> getDropdownItems() {
        clickAdminButton();
        List<String> list = container
                .$$("a")
                .as("all `Admin` dropdown items").texts();
        clickAdminButton();
        return list;
    }

    @Step("Open user menu view")
    public OpenUserMenuComponent openUserMenuButton() {
        $x(".//span[contains(text(), '%s')]/ancestor::button".formatted(OPEN_USER_MENU.getStringValue()))
                .as("User menu button")
                .shouldBe(Condition.visible)
                .click();
        return new OpenUserMenuComponent();
    }

    @Getter
    @AllArgsConstructor
    public enum TopLineButtons {
        INVESTIGATIONS("Investigations"),
        OPEN_USER_MENU("Open user menu"),
        ADMIN("Admin");

        private final String stringValue;
    }

    @Getter
    @AllArgsConstructor
    public enum AdminDropdown {
        USERS("Users"),
        ROLES("Roles"),
        TEAMS_MANAGEMENT("Teams Management"),
        SYSTEM_CONFIGURATION("System Configuration"),
        QUEUES_MANAGEMENT("Queues Management"),
        DATA_ACCESS("Data Access");

        private final String stringValue;
    }
}
