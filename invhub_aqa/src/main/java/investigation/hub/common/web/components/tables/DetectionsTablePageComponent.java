package investigation.hub.common.web.components.tables;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.smile.components.PageComponent;
import investigation.hub.common.web.test.data.dtos.transaction.Transaction;
import investigation.hub.common.web.components.TransactionsPageComponent;
import investigation.hub.common.web.test.data.dtos.TransactionProvider;
import io.qameta.allure.Step;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
@PageComponent
public class DetectionsTablePageComponent<T extends Enum<T> & TableGeneralComponent.GeneralTableHeaderName>
        extends TableGeneralComponent<T> {

    public TransactionsPageComponent clickExpandRowIconByRowData(Map<String, String> rowToFind) {
        SelenideElement row = getRowByContent(rowToFind);
        if (!Objects.equals(row.getAttribute("class"), "expandCollapseRow expanded")) {
            row.$("td")
                    .as("Expand/Collapse row icon")
                    .click();
        }
        waitOnLoading();
        return new TransactionsPageComponent();
    }

    @Step("User gets top transaction from table")
    public Transaction getTransaction() {
        ElementsCollection values = $$("tr tr:first-child td:not(.checkboxColumn):not(.narrowColumn)")
                .as("first row values");
        return new TransactionProvider(values.texts()).provide();
    }


    public void waitOnLoading() {
        $("div[class='dn-loader ']")
                .as("Loading spinner")
                .shouldBe(visible)
                .shouldBe(disappear, Duration.ofSeconds(8));
    }
}