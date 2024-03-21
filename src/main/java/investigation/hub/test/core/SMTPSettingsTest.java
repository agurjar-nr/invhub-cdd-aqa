package investigation.hub.test.core;

import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.core.util.Retry;
import investigation.hub.common.web.components.SMTPSettingsPageComponent;
import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.codeborne.selenide.Selenide.open;
import static investigation.hub.common.web.components.SMTPSettingsPageComponent.DefaultSMTPSettings.*;
import static investigation.hub.common.web.components.modals.LeftFloatMenuConfigurationModalComponent.LeftMenuItemName.SMTP_SETTINGS;

@Log4j2
public class SMTPSettingsTest extends InvHubUiTest {

    private SMTPSettingsPageComponent smtpSettingsPageComponent;

    @BeforeMethod
    public void openPage() {
        open(apiProperties.getUrl() + apiProperties.getOpenInvestigations());
        openInvestigationsPage.waitForLoad();
    }


    @TmsLink("INVHUB-1053")
    @Issue("INVHUB-3126")
    @Test(retryAnalyzer = Retry.class, description = "Check SMTP Settings page is visible, and contains default values")
    public void checkSMTPSettingPageIsVisibleTest() {
        SoftAssert softAssert = new SoftAssert();
        smtpSettingsPageComponent = openInvestigationsPage.getMainHeaderComponent()
                .openSystemConfiguration()
                .getLeftMenuComponent()
                .clickMenuItemByName(SMTP_SETTINGS);

        softAssert.assertTrue(smtpSettingsPageComponent.isHeaderSectionVisible(),
                "SMTP Settings page header should be displayed");

        softAssert.assertEquals(smtpSettingsPageComponent.getHostName(),HOSTNAME.getStringValue(),
                "`Hostname` field should contain default value:`%s`".formatted(HOSTNAME.getStringValue()));

        softAssert.assertEquals(smtpSettingsPageComponent.getPortValue(),PORT.getStringValue(),
                "`Port` field should contain default value:`%s`".formatted(PORT.getStringValue()));

        softAssert.assertEquals(smtpSettingsPageComponent.getUserName(),USER_NAME.getStringValue(),
                "`User Name` field should contain default value:`%s`".formatted(USER_NAME.getStringValue()));

        softAssert.assertEquals(smtpSettingsPageComponent.getPassword(),PASSWORD.getStringValue(),
                "`Password` field should contain default value (asterisks):`%s`".formatted(PASSWORD.getStringValue()));

        softAssert.assertEquals(smtpSettingsPageComponent.getFromEmail(),FROM_EMAIL.getStringValue(),
                "`From Email` field should contain default value:`%s`".formatted(FROM_EMAIL.getStringValue()));

        softAssert.assertEquals(smtpSettingsPageComponent.getFromName(),FROM_NAME.getStringValue(),
                "`From Name` field should contain default value:`%s`".formatted(FROM_NAME.getStringValue()));
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-2483")
    @Test(retryAnalyzer = Retry.class, description = "Update SMTP Settings")
    public void updateSMTPSettingTest() {
        smtpSettingsPageComponent = openInvestigationsPage.getMainHeaderComponent()
                .openSystemConfiguration()
                .getLeftMenuComponent()
                .clickMenuItemByName(SMTP_SETTINGS);
        smtpSettingsPageComponent.enterEmail(email)
                .enterFromName("Sensa2")
                .clickVerifyButton();
        Assert.assertTrue(smtpSettingsPageComponent.isSMTPSettingsNOTVerifiedMessageAppeared(),
                "SMTP Settings NOT verified message should be displayed");
        smtpSettingsPageComponent.clickCancelButton();
    }
}
