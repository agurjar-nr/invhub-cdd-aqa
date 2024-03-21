package investigation.hub.test.core;

import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.core.util.Retry;
import investigation.hub.common.web.components.WebSearchPageComponent;
import investigation.hub.common.web.components.WebSearchPageComponent.Values;
import investigation.hub.common.web.components.modals.GeneratedByAiModalComponent;
import investigation.hub.common.web.components.tables.InvestigationsTablePageComponent;
import investigation.hub.common.web.pages.HelpPage;
import investigation.hub.common.web.pages.InvestigationPage;
import investigation.hub.common.web.test.data.dtos.Result;
import investigation.hub.common.web.test.data.dtos.ResultProvider;
import io.qameta.allure.TmsLink;
import io.qameta.allure.TmsLinks;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static investigation.hub.common.web.components.WebSearchPageComponent.Names.*;
import static investigation.hub.common.web.components.WebSearchPageComponent.Values.*;
import static investigation.hub.common.web.components.modals.LeftFloatMenuInvestigationModalComponent.LeftMenuItemName.WEB_SEARCH;
import static investigation.hub.common.web.enums.CommonButton.REMOVE;
import static investigation.hub.common.web.enums.CommonButton.SEARCH;

@Log4j2
public class WebSearchTest extends InvHubUiTest {

    @DataProvider(name = "queryAndResult")
    static Object[][] queryAndResult() {
        return new Object[][] {
                {LOW_RELEVANCE_QUERY, new ResultProvider(LOW, false).provide()},
                {HIGH_RELEVANCE_QUERY, new ResultProvider(HIGH, false).provide()}};
    }

    @DataProvider(name = "query")
    static Object[][] query() {
        return new Object[][] {
                {LOW_RELEVANCE_QUERY},
                {HIGH_RELEVANCE_QUERY}};
    }

    @DataProvider(name = "level")
    static Object[][] level() {
        return new Object[][] {
                {LOW},
                {HIGH}};
    }

    InvestigationPage investigationPage;
    InvestigationsTablePageComponent investigationsTablePageComponent;
    WebSearchPageComponent webSearchPageComponent;
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
        webSearchPageComponent = investigationPage
                .getLeftMenuComponent()
                .clickMenuItemByName(WEB_SEARCH);
    }

    @TmsLinks({
            @TmsLink("INVHUB-2042"),
            @TmsLink("INVHUB-2043")
    })
    @Test(retryAnalyzer = Retry.class, description = "Verify user can process general and 'finance crime'-related web search", dataProvider = "queryAndResult")
    public void checkUserCanProcessWebSearchTest(Values query, Result expectedResult) {
        webSearchPageComponent
                .enterSearchString(query.getStringValue())
                .clickButton(SEARCH)
                .waitUntilSearchResultsAreLoaded();

        List<Result> actualResults = webSearchPageComponent.getResults(SEARCH_RESULTS);
        String searchPageUrl = webSearchPageComponent.openGetUrlFirstSearchResult();

        SoftAssertions softly = new SoftAssertions();
        validateSearchResults(softly, actualResults, expectedResult);
        softly.assertThat(searchPageUrl).as("Check opened page url")
                .containsIgnoringCase(actualResults.get(0).getLinkUrl());
        softly.assertAll();
    }

    private void validateSearchResults(SoftAssertions softly, List<Result> actualResults, Result expected) {
        actualResults.forEach(actual -> {
            softly.assertThat(actual).usingRecursiveComparison()
                    .ignoringFields(COPILOT_SUMMARY.getFieldName(), LINK_URL.getFieldName()).isEqualTo(expected)
                    .describedAs("Search Engine, Financial Crime Relevance, Button comparison");
            softly.assertThat(actual).extracting(Result::getCopilotSummary).asString()
                    .describedAs("`Copilot summary` contains expected finance crime relevance")
                    .contains(expected.getCopilotSummary());
        });
    }

    @TmsLink("INVHUB-2044")
    @Test(retryAnalyzer = Retry.class, description = "Verify user can save general and 'finance crime'-related web search result.", dataProvider = "query")
    public void checkUserCanSaveWebSearchResultTest(Values query) {
        webSearchPageComponent
                .enterSearchString(query.getStringValue())
                .clickButton(SEARCH)
                .waitUntilSearchResultsAreLoaded();

        int initialSavedResults = webSearchPageComponent.getSavedResultsNumber();
        int initialUnsavedResults = webSearchPageComponent.getSearchResultsNumber();
        Result firstUnsaved = webSearchPageComponent.getFirstSearchResult();
        firstUnsaved.setButton(REMOVE.getTitle());

        webSearchPageComponent.saveFirstSearchResult()
                .messageBlinked(SAVE_MESSAGE);
        List<Result> actualResults = webSearchPageComponent.getResults(SAVED_RESULTS);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(webSearchPageComponent.getSavedResultsNumber()).describedAs("Saved result became 1 more")
                .isEqualTo(initialSavedResults + 1);
        softly.assertThat(webSearchPageComponent.getSearchResultsNumber()).describedAs("Unsaved result became 1 less")
                .isEqualTo(initialUnsavedResults - 1);
        softly.assertThat(actualResults).anyMatch(firstUnsaved::equals);
        softly.assertAll();
    }

    @TmsLink("INVHUB-2048")
    /**
     * Test precondition: user has saved results for general and 'finance crime' web searches related for investigation
     */
    @Test(retryAnalyzer = Retry.class, description = "Verify user can process general and 'finance crime'-related saved search", dataProvider = "level")
    public void checkUserCanProcessSavedWebSearchTest(Values level) {
        Result firstSaved = webSearchPageComponent.getFirstSavedResult(level);
        String savedPageUrl = webSearchPageComponent.openGetUrlFirstSavedResult(level);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(firstSaved).extracting(FINANCIAL_CRIME_RELEVANCE.getFieldName()).isEqualTo(level.getStringValue());
        softly.assertThat(savedPageUrl).containsIgnoringCase(firstSaved.getLinkUrl());
        softly.assertAll();
    }

    @TmsLink("INVHUB-2050")
    /**
     * Test precondition: user has saved web search results general and 'finance crime' related for investigation
     */
    @Test(retryAnalyzer = Retry.class, description = "Verify user can delete general and 'finance crime'-related saved search", dataProvider = "level")
    public void checkUserCanDeleteSavedWebSearchTest(Values level) {
        Result resultToBeDeleted = webSearchPageComponent.getFirstSavedResult(level);
        int resultNumberBeforeDeleting = webSearchPageComponent.getSavedResultsNumber();

        webSearchPageComponent.deleteFirstSaved(level)
                .getRefreshed();
        List<Result> actualResults = webSearchPageComponent.getResults(SAVED_RESULTS);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(actualResults)
                .as("Saved results should not contain deleted result")
                .doesNotContain(resultToBeDeleted);
        softly.assertThat(webSearchPageComponent.getSavedResultsNumber())
                .as("Saved results should become 1 less")
                .isEqualTo(resultNumberBeforeDeleting - 1);
        softly.assertAll();
    }

    @TmsLink("INVHUB-3773")
    @Test(retryAnalyzer = Retry.class, description = "Verify 'Show AI' provides info about the Copilot work on the Web Search page")
    public void checkShowAiProvidesInfoAboutCopilotWorkForWebSearchTest() {
        webSearchPageComponent
                .enterSearchString(HIGH_RELEVANCE_QUERY.getStringValue())
                .clickButton(SEARCH)
                .waitUntilSearchResultsAreLoaded()
                .clickGeneratedByAiDisclaimerLink()
                .clickOpenDocumentationLink();

        switchTo().window(1);
        helpPage.waitForLoad();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(helpPage.isPageTitleVisible())
                .as("The 'Help' page header should be displayed")
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
