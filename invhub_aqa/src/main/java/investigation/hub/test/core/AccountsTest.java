package investigation.hub.test.core;

import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.core.util.Retry;
import investigation.hub.common.web.components.OtherPreferencesPageComponent;
import investigation.hub.common.web.components.SubjectDetailsPageComponent;
import investigation.hub.common.web.components.modals.LeftFloatMenuInvestigationModalComponent;
import investigation.hub.common.web.components.tables.AccountsTablePageComponent;
import investigation.hub.common.web.components.tables.InvestigationsTablePageComponent;
import investigation.hub.common.web.components.tables.TableGeneralComponent;
import investigation.hub.common.web.enums.InvHubDateFormat;
import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.List;
import java.util.Set;

import static com.codeborne.selenide.Selenide.open;
import static investigation.hub.common.web.components.tables.AccountsTablePageComponent.HeaderName.*;
import static investigation.hub.common.web.enums.InvHubDateFormat.MONTH_DAY_YEAR;

public class AccountsTest extends InvHubUiTest {

    private static final String SUBJECT_ID = "REF-CUSTEST-LIST-78-0107";
    private InvestigationsTablePageComponent investigationsTablePageComponent;
    private SubjectDetailsPageComponent subjectDetailsPageComponent;
    private AccountsTablePageComponent accountsTablePageComponent;
    private OtherPreferencesPageComponent otherPreferencesPageComponent;

    @BeforeMethod
    public void openPage() {
        open(apiProperties.getUrl() + apiProperties.getOpenInvestigations());
        openInvestigationsPage.waitForLoad();
        investigationsTablePageComponent = openInvestigationsPage
                .clickAllWorkButton()
                .getInvestigationsTable();

        subjectDetailsPageComponent = investigationsTablePageComponent
                .clickRowBySubjectId(SUBJECT_ID)
                .getLeftMenuComponent()
                .clickMenuItemByName(LeftFloatMenuInvestigationModalComponent.LeftMenuItemName.SUBJECT_DETAILS);

        accountsTablePageComponent = subjectDetailsPageComponent.getAccountsTable();
    }

    @TmsLink("INVHUB-2696")
    @Test(retryAnalyzer = Retry.class, description = "Verify user can see currencies in 'Balance Amount' column in correct format")
    public void checkUserCanSeeCorrectCurrencyTest() {
        List<String> actualCurrencies = accountsTablePageComponent.getCurrencyList();
        Set<Currency> expectedCurrencies = Currency.getAvailableCurrencies();

        SoftAssert softAssert = new SoftAssert();

        actualCurrencies.forEach(currencyCode ->
                softAssert.assertTrue(expectedCurrencies
                                .stream()
                                .anyMatch(currency -> currency.getCurrencyCode().equals(currencyCode)),
                        currencyCode + " is not valid according to ISO codes"));

        softAssert.assertAll();
    }

    @TmsLink("INVHUB-2727")
    @Test(retryAnalyzer = Retry.class, description = "Verify user can change date format for 'Opened On'/'Closed On' columns")
    public void checkUserChangeDateFormatTest() {
        InvHubDateFormat changedDatePattern = InvHubDateFormat.YEAR_MONTH_DAY;

        List<String> datesBeforeChange = accountsTablePageComponent
                .getColumnData(OPENED_ON.getStringValue());

        otherPreferencesPageComponent = openInvestigationsPage
                .getMainHeaderComponent()
                .openSystemConfiguration()
                .getOtherPreferencesPageComponent()
                .selectDateFormat(changedDatePattern)
                .clickUpdateButton();

        investigationsTablePageComponent = openInvestigationsPage
                .getMainHeaderComponent()
                .openAllOpenInvestigations()
                .clickAllWorkButton()
                .getInvestigationsTable();

        subjectDetailsPageComponent = investigationsTablePageComponent
                .clickRowBySubjectId(SUBJECT_ID)
                .getLeftMenuComponent()
                .clickMenuItemByName(LeftFloatMenuInvestigationModalComponent.LeftMenuItemName.SUBJECT_DETAILS);

        accountsTablePageComponent = subjectDetailsPageComponent.getAccountsTable();

        List<String> datesAfterChange = accountsTablePageComponent
                .getColumnData(OPENED_ON.getStringValue());

        SoftAssert softAssert = new SoftAssert();

        datesAfterChange.forEach(dateAfterChange ->
                softAssert.assertTrue(datesBeforeChange
                                .stream()
                                .noneMatch(dateBeforeChange -> dateBeforeChange.equals(dateAfterChange)),
                        "Date format should be changed!"));

        softAssert.assertTrue(datesAfterChange
                        .stream()
                        .allMatch(dateAfterChange -> doesDateFollowPattern(
                                dateAfterChange, changedDatePattern)),
                "Date " + datesAfterChange + " should match the pattern");

        softAssert.assertAll();
    }

    @TmsLink("INVHUB-2425")
    @Test(retryAnalyzer = Retry.class, description = "Verify accounts can be sorted by 'Account ID' column")
    public void checkAccountIdColumnIsSortableTest() {
        sortTableColumnsAndCheck(
                ACCOUNT_ID,
                TableGeneralComponent.SortStatus.ASCENDING);
    }

    @TmsLink("INVHUB-2426")
    @Test(retryAnalyzer = Retry.class, description = "Verify accounts can be sorted by 'Account Name' column")
    public void checkAccountNameColumnIsSortableTest() {
        sortTableColumnsAndCheck(
                ACCOUNT_NAME,
                TableGeneralComponent.SortStatus.DESCENDING);
    }

    @TmsLink("INVHUB-2427")
    @Test(retryAnalyzer = Retry.class, description = "Verify accounts can be sorted by 'Product Type' column")
    public void checkProductTypeColumnIsSortableTest() {
        sortTableColumnsAndCheck(
                PRODUCT_TYPE,
                TableGeneralComponent.SortStatus.ASCENDING);
    }

    @TmsLink("INVHUB-2428")
    @Test(retryAnalyzer = Retry.class, description = "Verify accounts can be sorted by 'Debit/Credit Balance' column")
    public void checkDebitCreditBalanceColumnIsSortableTest() {
        sortTableColumnsAndCheck(
                DEBIT_CREDIT_BALANCE,
                TableGeneralComponent.SortStatus.DESCENDING);
    }

    @TmsLink("INVHUB-2429")
    @Test(retryAnalyzer = Retry.class, description = "Verify accounts can be sorted by 'Balance Amount' column")
    public void checkBalanceAmountColumnIsSortableTest() {
        sortTableColumnsAndCheck(
                BALANCE_AMOUNT,
                TableGeneralComponent.SortStatus.ASCENDING);
    }

    @TmsLink("INVHUB-2430")
    @Test(retryAnalyzer = Retry.class, description = "Verify accounts can be sorted by 'Opened On' column")
    public void checkOpenedOnColumnIsSortableTest() {
        sortTableDateColumnsAndCheck(
                OPENED_ON,
                TableGeneralComponent.SortStatus.DESCENDING);
    }

    @TmsLink("INVHUB-2431")
    @Test(retryAnalyzer = Retry.class, description = "Verify accounts can be sorted by 'Channel' column")
    public void checkChannelColumnIsSortableTest() {
        if (!accountsTablePageComponent.getColumnData(
                CHANNEL.getStringValue()).isEmpty()) {
            sortTableColumnsAndCheck(
                    CHANNEL,
                    TableGeneralComponent.SortStatus.ASCENDING);
        }
    }

    @TmsLink("INVHUB-2432")
    @Test(retryAnalyzer = Retry.class, description = "Verify accounts can be sorted by 'Status' column")
    public void checkStatusColumnIsSortableTest() {
        sortTableColumnsAndCheck(
                STATUS,
                TableGeneralComponent.SortStatus.DESCENDING);
    }

    @TmsLink("INVHUB-2433")
    @Test(retryAnalyzer = Retry.class, description = "Verify accounts can be sorted by 'Closed On' column")
    public void checkClosedOnColumnIsSortableTest() {
        if (!accountsTablePageComponent.getColumnData(
                AccountsTablePageComponent.HeaderName.CLOSED_ON.getStringValue()).isEmpty()) {
            sortTableDateColumnsAndCheck(
                    AccountsTablePageComponent.HeaderName.CLOSED_ON,
                    TableGeneralComponent.SortStatus.ASCENDING);
        }
    }

    @Issue("INVHUB-2644")
    @TmsLink("INVHUB-2435")
    @Test(retryAnalyzer = Retry.class, description = "Verify accounts can be filtered by the 'Account ID' column")
    public void checkAccountsCanBeFilteredByAccountIdTest() {
        filterTableColumnAndCheck(ACCOUNT_ID);
    }

    @Issue("INVHUB-2644")
    @TmsLink("INVHUB-2436")
    @Test(retryAnalyzer = Retry.class, description = "Verify accounts can be filtered by 'Account Name' column")
    public void checkAccountsCanBeFilteredByAccountNameTest() {
        filterTableColumnAndCheck(ACCOUNT_NAME);
    }

    @Issue("INVHUB-2644")
    @TmsLink("INVHUB-2437")
    @Test(retryAnalyzer = Retry.class, description = "Verify accounts can be filtered by 'Product Type' column")
    public void checkAccountsCanBeFilteredByProductTypeTest() {
        filterTableColumnAndCheck(PRODUCT_TYPE);
    }

    @TmsLink("INVHUB-2438")
    @Test(retryAnalyzer = Retry.class, description = "Verify accounts can be filtered by 'Debit/Credit Balance' column")
    public void checkAccountsCanBeFilteredByDebitCreditBalanceTest() {
        final String headerName = DEBIT_CREDIT_BALANCE.getStringValue();
        final String randomColumnValue = accountsTablePageComponent.getAnyColumnValue(DEBIT_CREDIT_BALANCE);
        accountsTablePageComponent
                .getFilterPageComponent()
                .clickColumnFilterIcon(headerName)
                .enterFilterValue("balance", randomColumnValue);
        checkTableColumnFilter(headerName, randomColumnValue);
    }

    @Issue("INVHUB-2644")
    @TmsLink("INVHUB-2439")
    @Test(retryAnalyzer = Retry.class, description = "Verify accounts can be filtered by 'Balance Amount' column")
    public void checkAccountsCanBeFilteredByBalanceAmountTest() {
        filterTableColumnAndCheck(BALANCE_AMOUNT);
    }

    @Issue("INVHUB-2644")
    @TmsLink("INVHUB-2440")
    @Test(retryAnalyzer = Retry.class, description = "Verify accounts can be filtered by 'Opened On' column")
    public void checkAccountsCanBeFilteredByOpenedOnTest() {
        filterTableDateColumnAndCheck(OPENED_ON, MONTH_DAY_YEAR.getFormatter());
    }

    @Issue("INVHUB-2644")
    @TmsLink("INVHUB-2441")
    @Test(retryAnalyzer = Retry.class, description = "Verify accounts can be filtered by 'Channel' column")
    public void checkAccountsCanBeFilteredByChannelTest() {
        if (!accountsTablePageComponent.getColumnData(CHANNEL.getStringValue()).isEmpty()) {
            filterTableColumnAndCheck(CHANNEL);
        }
    }

    @Issue("INVHUB-2644")
    @TmsLink("INVHUB-2442")
    @Test(retryAnalyzer = Retry.class, description = "Verify accounts can be filtered by 'Status' column")
    public void checkAccountsCanBeFilteredByStatusTest() {
        final String headerName = STATUS.getStringValue();
        final String randomColumnValue = accountsTablePageComponent.getAnyColumnValue(STATUS);
        accountsTablePageComponent
                .getFilterPageComponent()
                .clickColumnFilterIcon(headerName)
                .enterFilterValue(headerName.toLowerCase(), randomColumnValue);
        checkTableColumnFilter(headerName, randomColumnValue);
    }

    @Issue("INVHUB-2644")
    @TmsLink("INVHUB-2443")
    @Test(retryAnalyzer = Retry.class, description = "Verify accounts can be filtered by 'Closed On' column")
    public void checkAccountsCanBeFilteredByClosedOnTest() {
        if (!accountsTablePageComponent.getColumnData(CLOSED_ON.getStringValue()).isEmpty()) {
            filterTableDateColumnAndCheck(CLOSED_ON, MONTH_DAY_YEAR.getFormatter());
        }
    }

    private static boolean doesDateFollowPattern(String date, InvHubDateFormat pattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern.getFormatterPattern());
            LocalDate.parse(date, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void sortTableColumnsAndCheck(AccountsTablePageComponent.HeaderName headerName,
                                          TableGeneralComponent.SortStatus sortType) {

        accountsTablePageComponent = accountsTablePageComponent.sortTable(headerName, sortType);

        List<String> actualColumnData = accountsTablePageComponent.getColumnData(headerName.getStringValue());
        Assert.assertEquals(actualColumnData,
                accountsTablePageComponent.getListByDirection(
                        headerName.getStringValue(),
                        sortType),
                headerName.getStringValue() + " сolumn should be sorted properly, sort type is: " + sortType);
    }

    private void sortTableDateColumnsAndCheck(AccountsTablePageComponent.HeaderName headerName,
                                              TableGeneralComponent.SortStatus sortType) {

        accountsTablePageComponent = accountsTablePageComponent.sortTable(headerName, sortType);

        List<String> actualColumnData = accountsTablePageComponent.getColumnData(headerName.getStringValue());
        Assert.assertEquals(actualColumnData,
                accountsTablePageComponent.getDateListByDirection(
                        accountsTablePageComponent.getFormatter(),
                        headerName.getStringValue(),
                        sortType),
                headerName.getStringValue() + " сolumn should be sorted properly, sort type is: " + sortType);
    }

    private void filterTableColumnAndCheck(AccountsTablePageComponent.HeaderName header) {
        final String headerName = header.getStringValue();
        final String randomColumnValue = accountsTablePageComponent.getAnyColumnValue(header);
        filterTableColumn(headerName, randomColumnValue);
        checkTableColumnFilter(headerName, randomColumnValue);
    }

    private void checkTableColumnFilter(String headerName, String randomColumnValue) {
        accountsTablePageComponent
                .getColumnData(headerName)
                .forEach(it -> Assert.assertEquals(it, randomColumnValue,
                        "Accounts should be filtered by '%s' correctly".formatted(headerName)));
    }

    private void filterTableColumn(String headerName, String randomColumnValue) {
        accountsTablePageComponent
                .getFilterPageComponent()
                .clickColumnFilterIcon(headerName)
                .enterFilterValue(headerName, randomColumnValue);
    }

    private void filterTableDateColumnAndCheck(AccountsTablePageComponent.HeaderName header,
                                               DateTimeFormatter formatter) {
        final String headerName = header.getStringValue();
        final String randomColumnValue = accountsTablePageComponent.getAnyColumnValue(header);
        accountsTablePageComponent.getFilterPageComponent()
                .clickColumnFilterIcon(headerName)
                .enterFilterDateValue(randomColumnValue, randomColumnValue, formatter);

        boolean dateColumnFiltered = accountsTablePageComponent
                .getFilterPageComponent()
                .isDateColumnFiltered(
                        accountsTablePageComponent.getColumnData(headerName),
                        randomColumnValue,
                        randomColumnValue,
                        formatter);
        Assert.assertTrue(
                dateColumnFiltered,
                "Accounts should be filtered by '%s' column correctly".formatted(headerName));
    }

    @AfterClass
    public void revertDateFormatBack() {
        otherPreferencesPageComponent = openInvestigationsPage
                .getMainHeaderComponent()
                .openSystemConfiguration()
                .getOtherPreferencesPageComponent()
                .selectDateFormat(MONTH_DAY_YEAR)
                .clickUpdateButton();
    }
}