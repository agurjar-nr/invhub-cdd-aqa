package investigation.hub.test.core;

import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.core.util.Retry;
import io.qameta.allure.TmsLink;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static com.codeborne.selenide.Selenide.localStorage;
import static com.codeborne.selenide.Selenide.open;

@Log4j2
public class LoginTest extends InvHubUiTest {

    private String fakeCode = generateFakeCode();

    @BeforeMethod
    public void openPage() {
        localStorage().clear();
        open(apiProperties.getUrl() + apiProperties.getLogin());
    }

    @TmsLink("INVHUB-1049")
    @Test(retryAnalyzer = Retry.class, description = "Verify user able to login with valid credentials")
    public void loginTest() {
        loginPage.enterEmail(email)
                .clickSendCodeButton()
                .enterCode(loginClient.getLoginCodeForEmail(email))
                .clickSendCodeButton();
        Assert.assertTrue(loginPage.isSymphonyLogoShown(),
                "Symphony AI logo should be displayed");
    }

    @TmsLink("INVHUB-2421")
    @Test(retryAnalyzer = Retry.class, description = "Verify user can't login without code")
    public void loginWithoutCodeNegativeTest() {
        loginPage.enterEmail(email)
                .clickSendCodeButton()
                .enterCode(" ")
                .clickSendCodeButton();
        Assert.assertTrue(loginPage.isRequiredFieldMessageAppeared(),
                "Required code field message should be displayed");
    }

    @TmsLink("INVHUB-2423")
    @Test(retryAnalyzer = Retry.class, description = "Verify user can't login with invalid code")
    public void loginWithInvalidCodeNegativeTest() {
        loginPage.enterEmail(email)
                .clickSendCodeButton()
                .enterCode(fakeCode)
                .clickSendCodeButton();
        Assert.assertTrue(loginPage.isWrongCodeMessageAppeared(),
                "Wrong or expired code message should be displayed");
    }

    @TmsLink("INVHUB-2422")
    @Test(retryAnalyzer = Retry.class, description = "Verify user can't login with invalid email")
    public void loginWithInvalidEmailNegativeTest() {
        loginPage.enterEmail(email.substring(0,8))
                .clickSendCodeButton();
        Assert.assertTrue(loginPage.isWrongEmailMessageAppeared(),
                "Invalid email format message shouldn't be displayed");
    }


    private String generateFakeCode() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }
}
