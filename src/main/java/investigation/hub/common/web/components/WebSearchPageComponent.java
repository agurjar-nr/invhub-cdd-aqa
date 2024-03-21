package investigation.hub.common.web.components;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.smile.components.PageComponent;
import investigation.hub.common.web.components.modals.GeneratedByAiModalComponent;
import investigation.hub.common.web.components.modals.LeftFloatMenuInvestigationModalComponent;
import investigation.hub.common.web.enums.CommonButton;
import investigation.hub.common.web.pages.InvestigationPage;
import investigation.hub.common.web.test.data.dtos.Result;
import investigation.hub.common.web.test.data.dtos.ResultProvider;
import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static investigation.hub.common.web.components.WebSearchPageComponent.Names.FINANCIAL_CRIME_RELEVANCE;
import static investigation.hub.common.web.components.WebSearchPageComponent.Names.SAVED_RESULTS;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;

@Log4j2
@Getter
@PageComponent
public class WebSearchPageComponent extends PageGeneralComponent {

    private final SelenideElement container = $(".web-search-section-section");
    public static final String activeButtonXpath = ".//span[contains(text(),'%s')]/ancestor::button[not(@disabled)]";

    @Step("User enters query: `{query}`")
    public WebSearchPageComponent enterSearchString(String query) {
        SelenideElement search = container
                .$x(".//input")
                .as("Input text field for search");
        search.clear();
        search.setValue(query);
        log.info(format("Set: `%s` into search text area", query));
        return this;
    }

    @Step("Wait until search results are working...")
    public WebSearchPageComponent waitUntilSearchResultsAreLoaded() {
        $("div.search-box-loader").shouldBe(Condition.exist)
                .as("Search box loader")
                .shouldBe(Condition.disappear, Duration.ofSeconds(180));
        log.info("Wait until search results are loaded");
        return this;
    }

    @Step("User presses button `{button.name}`")
    public WebSearchPageComponent clickButton(CommonButton button) {
        container.$x(format(".//span[contains(text(), '%s')]/ancestor::button", button.getTitle()))
                .as("Pressed button: " + button.getTitle())
                .click();
        return this;
    }

    @Step("User gets `{section.name}` list elements")
    public List<Result> getResults(Names section) {
        List<Result> resultsList = new ArrayList<>();
        ElementsCollection results = $x(format(".//div[@class='search-results-section %s']", section.getName()))
                .as("collect all results for section")
                .$$("div.result-row");

        for (SelenideElement searchResult : results) {
            resultsList.add(getResult(searchResult));
        }
        return resultsList;
    }

    private Result getResult(SelenideElement searchResult) {
        String searchEngine = getEngineContainerValues(searchResult, Names.SEARCH_ENGINE);
        String financialCrimeRelevance = getEngineContainerValues(searchResult, FINANCIAL_CRIME_RELEVANCE);
        String linkUrl = searchResult.$("div.url")
                .as("Result `linkUrl` element ")
                .getText();
        String copilotSummary = searchResult.$("div.markdown-summary-content")
                .as("Result `Copilot Summary` element")
                .getText();
        String buttonName = searchResult.$("button")
                .as("Result `Button (Save/Remove)` element")
                .getText();

        return new ResultProvider(searchEngine, financialCrimeRelevance, linkUrl, copilotSummary, buttonName).provide();
    }

    private String getEngineContainerValues(SelenideElement engineMatchContainer, Names webSearch) {
        return engineMatchContainer.$x(format(".//span[contains(text(), '%s')]", webSearch.getName()))
                .as("Element from engine match container")
                .sibling(0)
                .getText();
    }

    @Step("User gets `Saved results` number")
    public int getSavedResultsNumber() {
        SelenideElement resultLabel = $x(String.format(".//div[contains(text(), '%s')]", SAVED_RESULTS.getName()));
        if (!resultLabel.exists()) {
            return 0;
        }
        return parseInt(resultLabel.as("Saved results count")
                .getText().replaceAll("[^\\d.]", ""));
    }

    @Step("User save first search result")
    public WebSearchPageComponent saveFirstSearchResult() {
        container.$x(String.format(activeButtonXpath, CommonButton.SAVE.getTitle())).click();
        return this;
    }

    @Step("User opens and get url from first suitable web search result.")
    public String openGetUrlFirstSearchResult() {
        container.$("div.name-source").click();
        return switchGetUrlCloseTab();
    }

    private String switchGetUrlCloseTab() {
        switchTo().window(1);
        String pageUrl = getWebDriver().getCurrentUrl();
        closeWindow();
        switchTo().window(0);
        return pageUrl;
    }

    @Step("User gets search results number")
    public int getSearchResultsNumber() {
        return container.$$x(String.format(activeButtonXpath, CommonButton.SAVE.getTitle())).size();
    }

    @Step("Wait for message `{message}` window blinking")
    public WebSearchPageComponent messageBlinked(Values message) {
        getMessageElement(message).shouldBe(Condition.exist)
                .as("Message element should be visible and then wait to disappear it")
                .shouldBe(Condition.disappear, Duration.ofSeconds(30));
        return this;
    }

    public SelenideElement getMessageElement(Values message) {
        return $x(format("//p[contains(text(),'%s')]", message.getStringValue()))
                .as(message + " element");
    }

    @Step("User gets first web search result")
    public Result getFirstSearchResult() {
        String resultXpath = ".//span[contains(text(),'%s')]/ancestor::button[not(@disabled)]/ancestor::div[@class='result-row']";
        return getResult(container.$x(String.format(resultXpath, CommonButton.SAVE.getTitle())));
    }

    @Step("User gets first web search result")
    public Result getFirstSavedResult(Values level) {
        return getResult(getFirstSavedResultElement(level));
    }

    @Step("User deletes first web search result")
    public WebSearchPageComponent deleteFirstSaved(Values level) {
        getFirstSavedResultElement(level)
                .$(byText(CommonButton.REMOVE.getTitle()))
                .parent()
                .as("button 'Remove'")
                .click();
        return this;
    }

    @Step("User opens first saved web search result from `Saved results` section for Financial crime relevance level `{level}`")
    public String openGetUrlFirstSavedResult(Values level) {
        getFirstSavedResultElement(level).$("div.name-source")
                .as("clickable Link element")
                .click();
        return switchGetUrlCloseTab();
    }

    @Step("User gets first saved web search result from `Saved results` section for Financial crime relevance level `{level}`")
    SelenideElement getFirstSavedResultElement(Values level) {
        return $x(format("//span[contains(text(), '%s') and (@class='value')]/ancestor::div[@class='result-row']",
                level.getStringValue()))
                .as("first result, by Finance crime relevance level from saved results section");
    }

    @Step("User refreshes page and opens `Web search` page")
    public WebSearchPageComponent getRefreshed() {
        Selenide.refresh();
        return new InvestigationPage().getLeftMenuComponent()
                .clickMenuItemByName(LeftFloatMenuInvestigationModalComponent.LeftMenuItemName.WEB_SEARCH);
    }

    @Step("User clicks 'Generated by AI Disclaimer' link")
    public GeneratedByAiModalComponent clickGeneratedByAiDisclaimerLink() {
        container
                .$(".disclaimer")
                .as("'Generated by AI Disclaimer' link")
                .click();
        log.info("Click 'Generated by AI Disclaimer' link");
        return new GeneratedByAiModalComponent();
    }

    @AllArgsConstructor
    @Getter
    public enum Names {
        SEARCH_RESULTS("Search results", ""),
        SEARCH_ENGINE("Search Engine", "searchEngine"),

        LINK_URL("", "linkUrl"),
        FINANCIAL_CRIME_RELEVANCE("Financial crime relevance", "financialCrimeRelevance"),
        COPILOT_SUMMARY("Copilot summary", "copilotSummary"),
        SAVED_RESULTS("Saved results", "");

        public final String name;
        public final String fieldName;
    }

    @AllArgsConstructor
    @Getter
    public enum Values {
        SEARCH_ENGINE("google"),
        LOW("LOW"),
        HIGH("HIGH"),
        LOW_RELEVANCE_QUERY("tornado"),
        HIGH_RELEVANCE_QUERY("hafiz money laundering"),
        SAVE_MESSAGE("Web search has been saved");

        private final String stringValue;
    }
}