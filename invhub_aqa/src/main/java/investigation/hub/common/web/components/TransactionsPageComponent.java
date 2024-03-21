package investigation.hub.common.web.components;

import com.codeborne.selenide.ElementsCollection;
import com.smile.components.PageComponent;
import investigation.hub.common.web.components.modals.AICopilotExplorationModalComponent;
import investigation.hub.common.web.components.modals.GeneratedByAiModalComponent;
import investigation.hub.common.web.components.tables.TransactionsTablePageComponent;
import investigation.hub.common.web.test.data.dtos.TransactionProvider;
import investigation.hub.common.web.test.data.dtos.transaction.Transaction;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Log4j2
@PageComponent
@Getter
public class TransactionsPageComponent extends TransactionCommonComponent {
    TransactionsTablePageComponent transactionsTablePageComponent = new TransactionsTablePageComponent();
    TransactionsTablePageComponent.EmbeddedTablePageComponent embeddedTransactionsTablePageComponent
            = new TransactionsTablePageComponent.EmbeddedTablePageComponent();
    AICopilotExplorationModalComponent aiCopilotExplorationModalComponent = new AICopilotExplorationModalComponent();

    @Step("User clicks Add to Narrative button")
    public TransactionsPageComponent clickAddToNarrativeButton() {
        $("button[aria-label='Add to Narrative']")
                .as("Add to Narrative button")
                .click();
        log.info("Click Add to Narrative button");
        return this;
    }

    @Step("User clicks Remove from Narrative button")
    public TransactionsPageComponent clickRemoveFromNarrativeButton() {
        $("button[aria-label='Remove from Narrative']")
                .as("Remove from Narrative button")
                .click();
        log.info("Click Remove from Narrative button");
        return this;
    }

    @Step("User gets top transaction from table")
    public Transaction getTransaction() {
        ElementsCollection values = $$("tr tr:first-child td:not(.checkboxColumn):not(.narrowColumn)")
                .as("first row values");
        log.info("top transaction from table:"+values.texts());
        return new TransactionProvider(values.texts()).provide();
    }

    @Step("User clicks 'Generated by AI Disclaimer' link")
    public GeneratedByAiModalComponent clickGeneratedByAiDisclaimerLink() {
        $("[title='Generated by AI Disclaimer']")
                .as("'Generated by AI Disclaimer' link")
                .click();
        log.info("Click 'Generated by AI Disclaimer' link");
        return new GeneratedByAiModalComponent();
    }
}
