package investigation.hub.common.web.components;

import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.PageComponent;
import investigation.hub.common.web.pages.AllOpenInvestigationsPage;
import investigation.hub.common.web.test.data.constants.Role;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
@PageComponent
public class SAMLSettingsPageComponent extends PageGeneralComponent {

    private final SelenideElement container = $x("//span[contains(text(), 'SAML Settings')]/ancestor::form");

    @Step("User enter SAML URL")
    public SAMLSettingsPageComponent enterSamlURL(String url) {
        container.$("#samlMetaDataUrl")
                .as("SAML URL field")
                .sendKeys(url);
        log.info("Enter SAML URL: " + url);
        return this;
    }

    @Step("User enter user access URL")
    public SAMLSettingsPageComponent enterUserAccessURL(String url) {
        container.$("#idpSsoUrl")
                .as("User access URL field")
                .sendKeys(url);
        log.info("Enter User access URL: " + url);
        return this;
    }

    @Step("Fill SAML fields mapping")
    public SAMLSettingsPageComponent fillSAMLFieldMapping(String name, String lastName,
                                                          String email, String group) {
        container.$("#fieldMappingFirstName")
                .as("SAML First Name field name field")
                .sendKeys(name);
        container.$("#fieldMappingLastName")
                .as("SAML Last Name field name field")
                .sendKeys(lastName);
        container.$("#fieldMappingEmail")
                .as("SAML Email field name field")
                .sendKeys(email);
        container.$("#fieldMappingGroup")
                .as("SAML Group field name field")
                .sendKeys(group);
        log.info("Fill SAML fields mapping");
        return this;
    }

    @Step("Click add mapping")
    public SAMLSettingsPageComponent clickAddMapping() {
        container.$x(".//div[contains(text(), 'Add Mapping')]")
                .as("Add mapping button")
                .click();
        return this;
    }

    @Step("Click Cancel button")
    public AllOpenInvestigationsPage clickCancelButton() {
        $x("//div[contains(text(), 'Cancel')]")
                .as("Cancel button")
                .click();
        return new AllOpenInvestigationsPage();
    }

    @Step("Click delete mapping")
    public SAMLSettingsPageComponent clickDeleteMapping() {
        container.$x(".//div[contains(text(), 'Delete mapping')]")
                .as("Delete mapping button")
                .click();
        return this;
    }

    @Step("Click save SAML")
    public SAMLSettingsPageComponent clickSaveSAML() {
        $x(".//div[contains(text(), 'Save and Enable SAML')]")
                .as("Save and Enable SAML button")
                .click();
        return this;
    }

    @Step("Fill SAML fields mapping")
    public SAMLSettingsPageComponent fillSAMLGroupsToRolesMapping(String groupID,
                                                                  String groupName,
                                                                  Role role) {
        container.$("input[name='roleMappings.0.groupId']")
                .as("Group ID field")
                .sendKeys(groupID);
        container.$("input[name='roleMappings.0.groupName']")
                .as("SAML Last Name field name field")
                .sendKeys(groupName);
        container.$x(".//span[contains(text(), 'select role')]")
                .as("Role dropdown")
                .click();
        container.$x(".//div[@class='relative inline']//span[contains(text(),'%s')]"
                .formatted(role.getStringValue()))
                .as("Role")
                .click();
        log.info("Fill SAML fields mapping");
        return this;
    }

    public boolean isHeaderSectionVisible() {
        return container.$x(".//div/span[contains(text(), 'SAML Settings')]")
                .as("SAML Settings page header")
                .isDisplayed();
    }

    public boolean isDownloadingFileMessageVisible() {
        return $x("//p[contains(text(),'Downloading File')]")
                .as("Downloading file message")
                .isDisplayed();
    }
}
