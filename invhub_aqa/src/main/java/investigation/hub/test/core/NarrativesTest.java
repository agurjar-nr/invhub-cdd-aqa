package investigation.hub.test.core;

import com.github.javafaker.Faker;
import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.core.util.Retry;
import investigation.hub.common.web.components.NarrativePageComponent;
import investigation.hub.common.web.components.modals.GeneratedByAiModalComponent;
import investigation.hub.common.web.components.modals.LeftFloatMenuInvestigationModalComponent;
import investigation.hub.common.web.components.tables.InvestigationsTablePageComponent;
import investigation.hub.common.web.pages.HelpPage;
import investigation.hub.common.web.pages.InvestigationPage;
import io.qameta.allure.TmsLink;
import org.assertj.core.api.SoftAssertions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.*;

public class NarrativesTest extends InvHubUiTest {
    InvestigationPage investigationPage;
    InvestigationsTablePageComponent investigationsTablePageComponent;
    NarrativePageComponent narrativePageComponent;
    GeneratedByAiModalComponent generatedByAiModalComponent = new GeneratedByAiModalComponent();
    HelpPage helpPage = new HelpPage();

    @BeforeMethod
    public void openPage() {
        open(apiProperties.getUrl() + apiProperties.getOpenInvestigations());
        openInvestigationsPage.waitForLoad();
        investigationsTablePageComponent = openInvestigationsPage
                .clickAllWorkButton()
                .getInvestigationsTable();

        String subjectId = investigationsTablePageComponent.getRandomSubjectId();

        investigationPage = investigationsTablePageComponent.clickRowBySubjectId(subjectId);
        narrativePageComponent = investigationPage
                .getLeftMenuComponent()
                .clickMenuItemByName(LeftFloatMenuInvestigationModalComponent.LeftMenuItemName.NARRATIVE);
    }

    @TmsLink("INVHUB-1244")
    @Test(retryAnalyzer = Retry.class, description = "Verify user can add new narrative manually")
    public void checkNarrativeCanBeAddedManuallyTest() {
        narrativePageComponent
                .enterTextIntoNarrativeTextArea(new Faker().animal().name())
                .clickSaveButton();

        Assert.assertTrue(narrativePageComponent.isNarrativeHasBeenSavedMessageAppeared(),
                "Narrative has been saved message should appear");
    }

    @TmsLink("INVHUB-1905")
    @Test(retryAnalyzer = Retry.class, description = "Verify user can generate narrative")
    public void checkNarrativeCanBeGeneratedTest() {
        narrativePageComponent
                .clickGenerateAINarrativeButton()
                .clickSaveButton();

        Assert.assertTrue(narrativePageComponent.isNarrativeHasBeenSavedMessageAppeared(),
                "Narrative has been saved message should appear");
    }

    @TmsLink("INVHUB-1906")
    @Test(retryAnalyzer = Retry.class, description = "Verify new narrative won't be saved if user moves to a different panel without saving")
    public void checkNarrativeWontBeSavedWithoutSavingTest() {
        String narrativeName = new Faker().animal().name();
        narrativePageComponent
                .enterTextIntoNarrativeTextArea(narrativeName);

        investigationPage.getLeftMenuComponent()
                .clickMenuItemByName(LeftFloatMenuInvestigationModalComponent.LeftMenuItemName.SUBJECT_DETAILS);
        narrativePageComponent.getUnsavedChangesModalComponent()
                .clickLeaveWithoutSavingButton();
        investigationPage.getLeftMenuComponent()
                .clickMenuItemByName(LeftFloatMenuInvestigationModalComponent.LeftMenuItemName.NARRATIVE);

        Assert.assertNotEquals(narrativePageComponent.getTextFromNarrativeTextArea(), narrativeName,
                "Narrative should not be saved if user moves to a different panel without saving");
    }

    @TmsLink("INVHUB-1931")
    @Test(retryAnalyzer = Retry.class, description = " Verify the confirmation if user moves to a different panel without saving")
    public void checkConfirmationIfUserMovesToDifferentPanelTest() {
        String narrativeName = new Faker().animal().name();
        narrativePageComponent
                .enterTextIntoNarrativeTextArea(narrativeName);

        investigationPage.getLeftMenuComponent()
                .clickMenuItemByName(LeftFloatMenuInvestigationModalComponent.LeftMenuItemName.SUBJECT_DETAILS);
        narrativePageComponent.getUnsavedChangesModalComponent()
                .clickSaveChangesButton();

        Assert.assertTrue(narrativePageComponent.isNarrativeHasBeenSavedMessageAppeared(),
                "Narrative has been saved message should appear");
    }

    @TmsLink("INVHUB-1924")
    @Test(retryAnalyzer = Retry.class, description = "Verify while AI is streaming user are not able to save narrative")
    public void checkWhileAIStreamingUserNotAbleToSaveNarrativeTest() {
        narrativePageComponent
                .clickGenerateAINarrativeButton();

        Assert.assertFalse(narrativePageComponent.isSaveButtonEnabled(),
                "Save button should not be clickable");
    }

    @TmsLink("INVHUB-3742")
    @Test(retryAnalyzer = Retry.class, description = "Verify 'Show AI' provides info about the Copilot work on the Narrative page")
    public void checkShowAiProvidesInfoAboutCopilotWorkForNarrativesTest() {
        narrativePageComponent
                .clickGenerateAINarrativeButton()
                .clickGeneratedByAiDisclaimerLink()
                .clickOpenDocumentationLink();

        switchTo().window(1);
        helpPage.waitForLoad();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(helpPage.isPageTitleVisible())
                .as("The 'Help' page main title should be visible")
                .isTrue();
        closeWindow();
        switchTo().window(0);

        generatedByAiModalComponent.clickGotItButton();
        softly.assertThat(generatedByAiModalComponent.isGeneratedByAiModalComponentDisplayed())
                .as("The 'Generated by Ai' modal dialog hasn't been closed")
                .isFalse();
        softly.assertAll();
    }
}
