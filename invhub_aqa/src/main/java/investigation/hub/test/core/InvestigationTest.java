package investigation.hub.test.core;

import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.core.util.Retry;
import investigation.hub.common.web.components.tables.InvestigationsTablePageComponent;
import investigation.hub.common.web.pages.InvestigationPage;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static investigation.hub.common.web.components.tables.InvestigationsTablePageComponent.HeaderName.SUBJECT_ID;
import static investigation.hub.common.web.components.tables.InvestigationsTablePageComponent.HeaderName.SUBJECT_NAME;

public class InvestigationTest extends InvHubUiTest {

    public String subjectId;
    InvestigationPage investigationPage;
    InvestigationsTablePageComponent investigationsTablePageComponent;

    @BeforeMethod
    public void openPage() {
        open(apiProperties.getUrl() + apiProperties.getOpenInvestigations());
        openInvestigationsPage.waitForLoad();
        investigationsTablePageComponent = openInvestigationsPage
                .clickAllWorkButton()
                .getInvestigationsTable();
        subjectId = investigationsTablePageComponent.getRandomSubjectId();
    }

    @TmsLink("INVHUB-1234")
    @Test
    public void checkSubjectInfoStructureTest() {
        Map<InvestigationsTablePageComponent.HeaderName, String> rowInfo = investigationsTablePageComponent
                .getRowValuesByHeaderName(SUBJECT_ID, subjectId);

        investigationPage = investigationsTablePageComponent
                .clickRowBySubjectId(subjectId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(investigationPage.isInfoSectionVisible(),
                "The information about the subject is hidden!");

        softAssert.assertEquals(investigationPage.getInvestigationTitle(),
                rowInfo.get(SUBJECT_NAME),
                "Subject name in the header doesn't correspond to Subject Name on All Investigations page!");

        softAssert.assertTrue(rowInfo.get(SUBJECT_ID).contains(investigationPage.getNumbersOfPersonalSubjectId()),
                "Subject ID in the sub-header doesn't correspond to Subject ID on All Investigations page!");

        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1235")
    @Test(retryAnalyzer = Retry.class)
    public void checkSubjectInfoShownByDefaultTest() {
        investigationPage = investigationsTablePageComponent
                .clickRowBySubjectId(subjectId);

        Assert.assertTrue(investigationPage.isInfoSectionVisible(),
                "The information about the subject should be expanded by default");
    }

    @TmsLink("INVHUB-1235")
    @Test(retryAnalyzer = Retry.class)
    public void checkSubjectInfoCanBeHiddenTest() {
        investigationPage = investigationsTablePageComponent
                .clickRowBySubjectId(subjectId);

        investigationPage.switchSubjectOverviewCheckbox();

        Assert.assertFalse(investigationPage.isInfoSectionVisible(),
                "The information about the subject is still expanded, but should be hidden");
    }
}
