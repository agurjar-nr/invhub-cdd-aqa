package investigation.hub.common.web.components.tables;

import com.codeborne.selenide.SelenideElement;
import investigation.hub.common.web.components.TransactionsPageComponent;
import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

/**
 * This table is functionally the same with EmbeddedTablePageComponent, but located separately
 * Left side menu -> Transactions
 */
@Log4j2
public class TransactionsTablePageComponent extends TableGeneralComponent<TransactionsTablePageComponent.HeaderName> {

    private final SelenideElement container = $(".dn-data-table-wrapper");

    public boolean isTableWithDataVisible() {
        return container.exists();
    }

    public TransactionsPageComponent waitForTable(boolean shouldBeVisible) {
        if (shouldBeVisible) {
            container.shouldBe(visible, Duration.ofSeconds(15));
        } else {
            container.shouldNotBe(visible, Duration.ofSeconds(15));
        }
        return new TransactionsPageComponent();
    }

    @Step("User selects checkbox in Transactions table by row data")
    public TransactionsPageComponent selectCheckboxByRowData(Map<String, String> rowToFind) {
        getRowByContent(rowToFind).$(By.tagName("td"))
                .as("Select row icon")
                .click();
        return new TransactionsPageComponent();
    }

    @Step("User selects bulk checkbox in Transactions table")
    public TransactionsPageComponent selectBulkCheckbox() {
        SelenideElement bulkCheckbox = $("th.checkboxColumn").shouldBe(visible);
        if ("false".equals(bulkCheckbox.$("input").getAttribute("aria-checked"))) {
            bulkCheckbox
                    .as("Bulk checkbox")
                    .click();
        }
        return new TransactionsPageComponent();
    }

    @Step("User sorts Transactions table by column")
    public TransactionsTablePageComponent sortTable(TransactionsTablePageComponent.HeaderName headerName,
                                                    SortStatus sortStatus) {
        SelenideElement header = getHeaderElementByName(headerName.getStringValue());
        log.info("Sort Transactions table by column: " + headerName.getStringValue());
        sortTableWithAriaLabel(header, sortStatus.getAriaLabel());
        return this;
    }

    @Getter
    @AllArgsConstructor
    public enum HeaderName implements GeneralTableHeaderName {

        TRANSACTION_ID("Transaction ID"),
        TRANSACTION_DATE("Transaction Date"),
        DEBIT_CREDIT("Debit/Credit"),
        BASE_AMOUNT("Base amount"),
        TRANSACTION_TYPE("Transaction Type"),
        BENEFICIARY_NAME("Beneficiary Name"),
        BENEFICIARY_COUNTRY("Beneficiary Country"),
        ORIGINATOR_NAME("Originator Name"),
        ORIGINATOR_COUNTRY("Originator Country"),
        ACCOUNT_ID("Account ID");

        public final String stringValue;

        //TODO: this method should be moved to the common interface in case it's possible
        public static List<String> getAllStringValues() {
            return Arrays.stream(values())
                    .map(HeaderName::getStringValue)
                    .toList();
        }
    }

    /**
     * This table is functionally the same with TransactionsTablePageComponent, but located inside each row of PreviousAmlDetectionsTablePageComponent
     * Left side menu -> AML Detections -> Previous AML Detections -> expanded row
     */
    public static class EmbeddedTablePageComponent extends TableGeneralComponent<TransactionsTablePageComponent.HeaderName> {

        public static final SelenideElement transactionsEmbeddedTable =
                $x("//div[@id='aml-detections-contributing-transactions-panel-header']/parent::div//table");
        public static final SelenideElement THEAD = transactionsEmbeddedTable.$("thead");
        public static final SelenideElement BODY = transactionsEmbeddedTable.$("tbody");

        public EmbeddedTablePageComponent() {
            super(THEAD, BODY);
        }

        /**
         * If transaction was added to Narrative successfully narrow column icon changes its opacity to brighter
         */
        public boolean isTransactionRowAddedToNarrative(Map<String, String> rowToFind) {
            return "[&_svg]:fill-gray-500 ".equals(getRowByContent(rowToFind)
                    .$("td[class='narrowColumn'] div")
                    .getAttribute("class"));
        }

        /**
         * If transaction was removed from Narrative successfully narrow column icon changes its opacity to more transparent
         */
        public boolean isTransactionRowRemovedFromNarrative(Map<String, String> rowToFind) {
            return "[&_svg]:fill-gray-500 opacity-30".equals(getRowByContent(rowToFind)
                    .$("td[class='narrowColumn'] div")
                    .getAttribute("class"));
        }
    }

}
