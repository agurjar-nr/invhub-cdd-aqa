package investigation.hub.test.core;

import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.core.util.Retry;
import investigation.hub.common.web.components.SAMLSettingsPageComponent;
import investigation.hub.common.web.components.modals.LeftFloatMenuConfigurationModalComponent;
import investigation.hub.common.web.test.data.constants.Role;
import io.qameta.allure.TmsLink;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;

@Log4j2
public class SAMLSettingsUITest extends InvHubUiTest {
    SAMLSettingsPageComponent samlSettingsPageComponent;

    @BeforeMethod
    public void openPage() {
        open(apiProperties.getUrl() + apiProperties.getOpenInvestigations());
        openInvestigationsPage.waitForLoad();
    }

    @TmsLink("INVHUB-1052")
    @Test(retryAnalyzer = Retry.class, description = "Check SAML Settings page is visible")
    public void checkSamlSettingPageIsVisibleTest() {
        samlSettingsPageComponent = openInvestigationsPage
                .getMainHeaderComponent()
                .openSystemConfiguration()
                .getLeftMenuComponent()
                .clickMenuItemByName(LeftFloatMenuConfigurationModalComponent.LeftMenuItemName.SAML_SETTINGS);
        Assert.assertTrue(samlSettingsPageComponent.isHeaderSectionVisible(),
                "SAML Settings page header should be displayed");
    }

    @TmsLink("INVHUB-2482")
    @Test(retryAnalyzer = Retry.class, description = "Update SAML Settings")
    public void updateSamlSettingTest() {
        String url = "www.test.com";
        samlSettingsPageComponent = openInvestigationsPage.getMainHeaderComponent()
                .openSystemConfiguration()
                .getLeftMenuComponent()
                .clickMenuItemByName(LeftFloatMenuConfigurationModalComponent.LeftMenuItemName.SAML_SETTINGS);
        samlSettingsPageComponent.enterSamlURL(url)
                .fillSAMLFieldMapping("test", "test", email, "test")
                .clickAddMapping()
                .fillSAMLGroupsToRolesMapping("test1234", "TestGroup", Role.ADMIN)
                .clickSaveSAML();
        Assert.assertTrue(samlSettingsPageComponent.isDownloadingFileMessageVisible(),
                "Downloading file message should be displayed");
        samlSettingsPageComponent.clickDeleteMapping();
        samlSettingsPageComponent.clickCancelButton();
    }
}
