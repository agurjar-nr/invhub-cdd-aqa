package investigation.hub.common.web.components.tables;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.PageComponent;
import investigation.hub.common.web.components.FilterPageComponent;
import investigation.hub.common.web.enums.InvHubDateFormat;
import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Getter
@Log4j2
@PageComponent
public class AccountsTablePageComponent extends TableGeneralComponent<AccountsTablePageComponent.HeaderName> {

    private final FilterPageComponent filterPageComponent = new FilterPageComponent();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            InvHubDateFormat.MONTH_DAY_YEAR.getFormatterPattern()).withLocale(Locale.US);

    @Step("User sorts Accounts table by column")
    public AccountsTablePageComponent sortTable(AccountsTablePageComponent.HeaderName headerName, SortStatus sortStatus) {
        SelenideElement header = getHeaderElementByName(headerName.getStringValue());
        sortTableWithAriaLabel(header, sortStatus.getAriaLabel());
        log.info("Sort Accounts table by column: " + headerName.getStringValue());
        return this;
    }

    public List<String> getCurrencyList() {
        return getColumnData(HeaderName.BALANCE_AMOUNT.getStringValue())
                .stream()
                .map(s -> s.substring(0, s.indexOf(" ")).trim())
                .toList();
    }

    @Getter
    @AllArgsConstructor
    public enum HeaderName implements GeneralTableHeaderName {

        ACCOUNT_ID("Account ID"),
        ACCOUNT_NAME("Account Name"),
        PRODUCT_TYPE("Product Type"),
        DEBIT_CREDIT_BALANCE("Debit/Credit Balance"),
        BALANCE_AMOUNT("Balance Amount"),
        OPENED_ON("Opened On"),
        CHANNEL("Channel"),
        STATUS("Status"),
        CLOSED_ON("Closed On");

        private final String stringValue;
    }
}
