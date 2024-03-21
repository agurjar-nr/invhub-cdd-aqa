package investigation.hub.common.web.components.modals;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.ModalComponent;
import investigation.hub.common.web.components.OtherPreferencesPageComponent;
import investigation.hub.common.web.components.PageGeneralComponent;
import investigation.hub.common.web.components.SAMLSettingsPageComponent;
import investigation.hub.common.web.components.SMTPSettingsPageComponent;
import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

@Log4j2
@ModalComponent
public class LeftFloatMenuConfigurationModalComponent {

    private final SelenideElement container = $("#sidebar-options");

    @Step
    public <T extends PageGeneralComponent> T clickMenuItemByName(LeftMenuItemName menuName) {
        container
                .$("button[title$='" + menuName.getStringValue() + "']")
                .as("Button of left side menu with title " + menuName)
                .click();
        //move cursor from menu to close it
        $x("//a[contains(text(),'System Configuration')]")
                .as("System Configuration breadcrumb")
                .hover();

        log.info("Click left side menu item by its name: " + menuName);

        Class<? extends PageGeneralComponent> componentClass = getComponentClass(menuName);

        try {
            return (T) componentClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Component for menu " + menuName.getStringValue() + " couldn't be instantiated");
        }
    }

    private Class<? extends PageGeneralComponent> getComponentClass(LeftMenuItemName menuName) {
        return switch (menuName) {
            case OTHER_PREFERENCES -> OtherPreferencesPageComponent.class;
            case SMTP_SETTINGS -> SMTPSettingsPageComponent.class;
            case SAML_SETTINGS -> SAMLSettingsPageComponent.class;
        };
    }

    @Getter
    @AllArgsConstructor
    public enum LeftMenuItemName {

        OTHER_PREFERENCES("Other Preferences"),
        SMTP_SETTINGS("SMTP Settings"),
        SAML_SETTINGS("SAML Settings");

        private final String stringValue;
    }
}
