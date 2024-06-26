package investigation.hub.test.core;

import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.core.util.Retry;
import investigation.hub.common.web.components.TransactionsPageComponent;
import investigation.hub.common.web.components.modals.AICopilotExplorationModalComponent;
import investigation.hub.common.web.components.modals.GeneratedByAiModalComponent;
import investigation.hub.common.web.components.modals.LeftFloatMenuInvestigationModalComponent;
import investigation.hub.common.web.components.tables.InvestigationsTablePageComponent;
import investigation.hub.common.web.components.tables.TableGeneralComponent;
import investigation.hub.common.web.components.tables.TransactionsTablePageComponent;
import investigation.hub.common.web.pages.HelpPage;
import investigation.hub.common.web.pages.InvestigationPage;
import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import org.assertj.core.api.SoftAssertions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.*;

public class TransactionsTest extends InvHubUiTest {

    InvestigationPage investigationPage;
    private final HelpPage helpPage = new HelpPage();
    InvestigationsTablePageComponent investigationsTablePageComponent;
    TransactionsPageComponent transactionsPageComponent;
    TransactionsTablePageComponent transactionsTablePageComponent;
    GeneratedByAiModalComponent generatedByAiModalComponent = new GeneratedByAiModalComponent();
    public static final String SUBJECT_ID = "REF-CUSTEST-LIST-87-0107";
    public static final String SEARCH_QUERY = "Show the last 10 transactions for Subject ID " + SUBJECT_ID;

    @BeforeMethod
    public void openPage() {
        open(apiProperties.getUrl() + apiProperties.getOpenInvestigations());
        openInvestigationsPage.waitForLoad();
        investigationsTablePageComponent = openInvestigationsPage
                .clickAllWorkButton()
                .getInvestigationsTable();
        InvestigationsTablePageComponent.HeaderName header = InvestigationsTablePageComponent.HeaderName.SUBJECT_ID;
        openInvestigationsPage.getInvestigationsTable()
                .getFilterPageComponent()
                .clickColumnFilterIcon(header.getStringValue())
                .enterFilterValue(header.getStringValue(), SUBJECT_ID);
        investigationPage = investigationsTablePageComponent.clickRowBySubjectId(SUBJECT_ID);

        transactionsPageComponent = investigationPage
                .getLeftMenuComponent()
                .clickMenuItemByName(LeftFloatMenuInvestigationModalComponent.LeftMenuItemName.TRANSACTIONS);
    }

    @TmsLink("INVHUB-2001")
    @Test(retryAnalyzer = Retry.class, description = "Verify user can ask Copilot for the last transactions and see them in the table")
    public void checkCopilotGeneratesResultTableTest() {
        AICopilotExplorationModalComponent aiCopilotExplorationModalComponent =
                transactionsPageComponent.getAiCopilotExplorationModalComponent();

        aiCopilotExplorationModalComponent.sendSearchQuery(SEARCH_QUERY);
        transactionsTablePageComponent = transactionsPageComponent.getTransactionsTablePageComponent();

        transactionsTablePageComponent.waitForTable(true);
        Assert.assertTrue(transactionsTablePageComponent.isTableWithDataVisible(),
                "The Transactions table with results after Copilot triggering should be shown");

        List<String> expectedHeadersList = TransactionsTablePageComponent.HeaderName.getAllStringValues();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(aiCopilotExplorationModalComponent.getCopilotMessage(),
                "InvHub screen has been updated with the data & charts !!",
                "The Copilot search success notification hasn't been displayed");

        softAssert.assertTrue(transactionsTablePageComponent.getAllHeadersNames().containsAll(expectedHeadersList),
                "Headers list in Transactions table should be as expected");

        softAssert.assertAll();
    }

    @TmsLink("INVHUB-2085")
    @Test(retryAnalyzer = Retry.class, description = "Verify user can clear table results generated by Copilot")
    public void checkUserCanClearTableResultsTest() {
        AICopilotExplorationModalComponent aiCopilotExplorationModalComponent = new AICopilotExplorationModalComponent();

        aiCopilotExplorationModalComponent.sendSearchQuery(SEARCH_QUERY);

        transactionsTablePageComponent = transactionsPageComponent.getTransactionsTablePageComponent();

        transactionsTablePageComponent.waitForTable(true);
        Assert.assertTrue(transactionsTablePageComponent.isTableWithDataVisible(),
                ("Table with data probably hasn't been generated by Copilot, check your search query"));

        aiCopilotExplorationModalComponent.clearSearchResults();
        transactionsTablePageComponent.waitForTable(false);

        Assert.assertFalse(transactionsTablePageComponent.isTableWithDataVisible(),
                "The Transactions table with results after clearing should not be shown");
    }

    @TmsLink("INVHUB-2002")
    @Test(retryAnalyzer = Retry.class, description = "Verify user can include transaction in Narrative")
    public void checkTransactionCanBeIncludedToNarrativeTest() {
        enterSearchQueryAndCheckResultTable();

        transactionsTablePageComponent = transactionsPageComponent.getTransactionsTablePageComponent();

        Map<String, String> randomTransaction = Map.of(
                TransactionsTablePageComponent.HeaderName.TRANSACTION_ID.getStringValue(),
                transactionsTablePageComponent
                        .getAnyColumnValue(TransactionsTablePageComponent.HeaderName.TRANSACTION_ID)
        );
        transactionsTablePageComponent
                .selectCheckboxByRowData(randomTransaction)
                .clickAddToNarrativeButton();

        Assert.assertTrue(transactionsPageComponent.isTransactionIncludedInNarrativeMessageAppeared(),
                "1 transaction included in narrative message should appear");
    }

    @TmsLink("INVHUB-2003")
    @Test(retryAnalyzer = Retry.class, description = "Verify user can exclude transaction from Narrative")
    public void checkTransactionCanBeExcludedFromNarrativeTest() {
        enterSearchQueryAndCheckResultTable();

        transactionsTablePageComponent = transactionsPageComponent.getTransactionsTablePageComponent();

        Map<String, String> randomTransaction = Map.of(
                TransactionsTablePageComponent.HeaderName.TRANSACTION_ID.getStringValue(),
                transactionsTablePageComponent
                        .getAnyColumnValue(TransactionsTablePageComponent.HeaderName.TRANSACTION_ID)
        );
        transactionsTablePageComponent
                .selectCheckboxByRowData(randomTransaction)
                .clickRemoveFromNarrativeButton();

        Assert.assertTrue(transactionsPageComponent.isTransactionExcludedFromNarrativeMessageAppeared(),
                "1 transaction excluded from narrative message should appear");
    }

    @TmsLink("INVHUB-2005")
    @Test(retryAnalyzer = Retry.class, description = "Verify user can include all transaction in Narrative by bulk")
    public void checkAllTransactionCanBeIncludedToNarrativeTest() {
        enterSearchQueryAndCheckResultTable();

        transactionsTablePageComponent = transactionsPageComponent.getTransactionsTablePageComponent();
        int numberOfResults = transactionsTablePageComponent.getAllRowsNumber();

        transactionsTablePageComponent
                .selectBulkCheckbox()
                .clickAddToNarrativeButton();

        Assert.assertTrue(transactionsPageComponent.areTransactionsIncludedInNarrativeMessageAppeared(numberOfResults),
                "%s transactions included in narrative message should appear".formatted(numberOfResults));
    }

    @TmsLink("INVHUB-2004")
    @Test(retryAnalyzer = Retry.class, description = "Verify user can exclude transaction from Narrative by bulk")
    public void checkAllTransactionCanBeExcludedFromNarrativeTest() {
        enterSearchQueryAndCheckResultTable();

        transactionsTablePageComponent = transactionsPageComponent
                .getTransactionsTablePageComponent();
        int numberOfResults = transactionsTablePageComponent.getAllRowsNumber();

        transactionsTablePageComponent
                .selectBulkCheckbox()
                .clickRemoveFromNarrativeButton();

        Assert.assertTrue(transactionsPageComponent.areTransactionsExcludedFromNarrativeMessageAppeared(numberOfResults),
                "%s transactions included in narrative message should appear".formatted(numberOfResults));
    }

    @TmsLink("INVHUB-2006")
    @Test(retryAnalyzer = Retry.class, description = "Verify Transactions can be sorted by 'Transaction ID' column")
    public void checkTransactionIdColumnIsSortableTest() {
        enterSearchQueryAndCheckResultTable();

        sortTableColumnsAndCheck(
                TransactionsTablePageComponent.HeaderName.TRANSACTION_ID,
                TableGeneralComponent.SortStatus.ASCENDING);
    }

    @TmsLink("INVHUB-2007")
    @Test(retryAnalyzer = Retry.class, description = "Verify Transactions can be sorted by 'Transaction Date' column")
    public void checkTransactionDateColumnIsSortableTest() {
        enterSearchQueryAndCheckResultTable();

        sortTableColumnsAndCheck(
                TransactionsTablePageComponent.HeaderName.TRANSACTION_DATE,
                TableGeneralComponent.SortStatus.DESCENDING);
    }

    @TmsLink("INVHUB-2008")
    @Test(retryAnalyzer = Retry.class, description = "Verify Transactions can be sorted by 'Debit/Credit' column")
    public void checkDebitCreditColumnIsSortableTest() {
        enterSearchQueryAndCheckResultTable();

        sortTableColumnsAndCheck(
                TransactionsTablePageComponent.HeaderName.DEBIT_CREDIT,
                TableGeneralComponent.SortStatus.ASCENDING);
    }

    @Issue("INVHUB-2644")
    @TmsLink("INVHUB-2009")
    @Test(retryAnalyzer = Retry.class, description = "Verify Transactions can be sorted by 'Base amount' column")
    public void checkBaseAmountColumnIsSortableTest() {
        enterSearchQueryAndCheckResultTable();

        sortTableColumnsAndCheck(
                TransactionsTablePageComponent.HeaderName.BASE_AMOUNT,
                TableGeneralComponent.SortStatus.DESCENDING);
    }

    @TmsLink("INVHUB-2010")
    @Test(retryAnalyzer = Retry.class, description = "Verify Transactions can be sorted by 'Transaction Type' column")
    public void checkTransactionTypeColumnIsSortableTest() {
        enterSearchQueryAndCheckResultTable();

        sortTableColumnsAndCheck(
                TransactionsTablePageComponent.HeaderName.TRANSACTION_TYPE,
                TableGeneralComponent.SortStatus.ASCENDING);
    }

    @TmsLink("INVHUB-2011")
    @Test(retryAnalyzer = Retry.class, description = "Verify Transactions can be sorted by 'Beneficiary Name' column")
    public void checkBeneficiaryNameColumnIsSortableTest() {
        enterSearchQueryAndCheckResultTable();

        sortTableColumnsAndCheck(
                TransactionsTablePageComponent.HeaderName.BENEFICIARY_NAME,
                TableGeneralComponent.SortStatus.DESCENDING);
    }

    @TmsLink("INVHUB-2012")
    @Test(retryAnalyzer = Retry.class, description = "Verify Transactions can be sorted by 'Beneficiary Country' column")
    public void checkBeneficiaryCountryColumnIsSortableTest() {
        enterSearchQueryAndCheckResultTable();

        sortTableColumnsAndCheck(
                TransactionsTablePageComponent.HeaderName.BENEFICIARY_COUNTRY,
                TableGeneralComponent.SortStatus.ASCENDING);
    }

    @TmsLink("INVHUB-2013")
    @Test(retryAnalyzer = Retry.class, description = "Verify Transactions can be sorted by 'Originator Name' column")
    public void checkOriginatorNameColumnIsSortableTest() {
        enterSearchQueryAndCheckResultTable();

        sortTableColumnsAndCheck(
                TransactionsTablePageComponent.HeaderName.ORIGINATOR_NAME,
                TableGeneralComponent.SortStatus.DESCENDING);
    }

    @TmsLink("INVHUB-2014")
    @Test(retryAnalyzer = Retry.class, description = "Verify Transactions can be sorted by 'Originator Country' column")
    public void checkOriginatorCountryColumnIsSortableTest() {
        enterSearchQueryAndCheckResultTable();

        sortTableColumnsAndCheck(
                TransactionsTablePageComponent.HeaderName.ORIGINATOR_COUNTRY,
                TableGeneralComponent.SortStatus.ASCENDING);
    }

    @TmsLink("INVHUB-2015")
    @Test(retryAnalyzer = Retry.class, description = "Verify Transactions can be sorted by 'Account ID' column")
    public void checkAccountIdColumnIsSortableTest() {
        enterSearchQueryAndCheckResultTable();

        sortTableColumnsAndCheck(
                TransactionsTablePageComponent.HeaderName.ACCOUNT_ID,
                TableGeneralComponent.SortStatus.DESCENDING);
    }

    @TmsLink("INVHUB-3709")
    @Test(retryAnalyzer = Retry.class, description = "Verify 'Show AI' provides info about the Copilot work on the Transactions page")
    public void checkShowAiProvidesInfoAboutCopilotWorkForTransactionsTest() {
        enterSearchQueryAndCheckResultTable();
        transactionsPageComponent
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

    private void enterSearchQueryAndCheckResultTable() {
        AICopilotExplorationModalComponent aiCopilotExplorationModalComponent = new AICopilotExplorationModalComponent();

        aiCopilotExplorationModalComponent.sendSearchQuery(SEARCH_QUERY);
        transactionsTablePageComponent = transactionsPageComponent.getTransactionsTablePageComponent();

        transactionsTablePageComponent.waitForTable(true);
        Assert.assertTrue(transactionsTablePageComponent.isTableWithDataVisible(),
                "The Transactions table with results after Copilot triggering should be shown");

        aiCopilotExplorationModalComponent.collapseAIModalWindow();
    }

    private void sortTableColumnsAndCheck(TransactionsTablePageComponent.HeaderName headerName,
                                          TableGeneralComponent.SortStatus sortType) {
        transactionsTablePageComponent = transactionsPageComponent.getTransactionsTablePageComponent();

        transactionsTablePageComponent.sortTable(
                headerName,
                sortType);

        List<String> columnData = transactionsTablePageComponent.getColumnData(headerName.getStringValue());
        Assert.assertEquals(columnData,
                transactionsTablePageComponent.getListByDirection(headerName.getStringValue(), sortType),
                headerName.getStringValue() + " сolumn should be sorted properly, sort type is: " + sortType);
    }
}
