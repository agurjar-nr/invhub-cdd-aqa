package investigation.hub.common.web.components.tables;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.PageComponent;
import investigation.hub.common.web.pages.queues.QueuesDetailsPage;
import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Log4j2
@PageComponent
public class QueuesTablePageComponent
        extends TableGeneralComponent<QueuesTablePageComponent.HeaderName> {

    public List<String> getAllHeadersNames() {
        return super.getAllHeadersNames(By.cssSelector("th div span[role=button]"));
    }

    @Step
    public QueuesDetailsPage openQueueDetailsPage(String queueName) {
        SelenideElement rowBySubjectId = super
                .getRowByHeaderAndValue(HeaderName.NAME.getStringValue(), queueName);

        rowBySubjectId
                .$("a")
                .click();

        log.info("Open Queue Details page: " + queueName);
        return new QueuesDetailsPage();
    }

    /*
     * Returns map that represents table row and contains key - name of header, value - value of row cell
     * */
    public Map<HeaderName, String> getRowValuesByHeaderName(HeaderName headerName, String cellValue) {
        SelenideElement rowByHeaderValue = super.getRowByHeaderAndValue(headerName.getStringValue(), cellValue);

        List<String> cellValues = rowByHeaderValue
                .findAll("td")
                .asDynamicIterable()
                .stream()
                .map(SelenideElement::getText)
                .toList();

        List<HeaderName> headerValues = Arrays.asList(HeaderName.values());

        return IntStream
                .range(0, headerValues.size())
                .boxed()
                .collect(Collectors.toMap(headerValues::get, cellValues::get));
    }

    @Step("User sorts Investigations table by column")
    public void sortTable(HeaderName headerName, SortStatus sortStatus) {
        SelenideElement header = getHeaderElementByName(headerName.getStringValue());
        sortTableWithAriaLabel(header, sortStatus.getAriaLabel());
        log.info("Sort Queue Management table by column: %s and direction: %s".formatted(headerName.getStringValue(), sortStatus));
    }

    @Getter
    @AllArgsConstructor
    public enum HeaderName implements GeneralTableHeaderName {
        NAME("Name"),
        DESCRIPTION("Description"),
        LAST_UPDATE("Last Updated On"),
        CREATED_ON("Created On");

        public final String stringValue;
    }
}
