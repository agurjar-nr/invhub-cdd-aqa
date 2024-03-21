package investigation.hub.common.web.components.modals;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@Log4j2
public class AICopilotExplorationModalComponent {

    private final SelenideElement container = $("div.dn-copilot");
    private final SelenideElement searchBox = container.$("div.search-box");
    private final SelenideElement searchInput = searchBox.$("input");

    @Step("User enters search query and sends it to AI engine")
    public AICopilotExplorationModalComponent sendSearchQuery(String query) {
        searchInput.clear();
        searchInput.sendKeys(query);
        log.info("Enter text into AI modal dialog");

        searchBox
                .$("button")
                .as("Search button")
                .click();

        log.info("Waiting while AI is streaming");
        waitForAIToStopStreaming();
        return this;
    }

    @Step("User collapses AI engine modal")
    public AICopilotExplorationModalComponent collapseAIModalWindow() {
        if (isAIModalExpanded()) {
            container.$("div.close-icon button")
                    .as("Close AI button arrow")
                    .click();
        }
        log.info("Collapse AI engine modal");
        return this;
    }

    @Step("User clears search results by clicking 'plus' button")
    public AICopilotExplorationModalComponent clearSearchResults() {
        container.$("div.new-icon button")
                .as("Clear search results ('plus') button")
                .click();
        log.info("Clear search results");
        return this;
    }

    public boolean isAIModalExpanded() {
        return container
                .$("div.hds-chat-body")
                .as("Expanded AI chat modal body")
                .exists();
    }

    private void waitForAIToStopStreaming() {
        container.$("div.loading-animation")
                .as("Loading AI response animation")
                .shouldNotBe(visible, Duration.ofSeconds(120));
    }

    public String getCopilotMessage() {
        return container
                .$x(".//div[contains(@class, 'hds-message-body')]/span")
                .getText();
    }
}
