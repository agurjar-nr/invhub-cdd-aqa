package investigation.hub.common.web.components.tables;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.PageComponent;
import investigation.hub.common.web.components.FilterPageComponent;
import investigation.hub.common.web.components.OpenDetectionsPageComponent;
import investigation.hub.common.web.enums.InvHubDateFormat;
import io.qameta.allure.Step;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
@PageComponent
public class OpenDetectionsTablePageComponent extends DetectionsTablePageComponent<OpenDetectionsTablePageComponent.HeaderName> {

    private final FilterPageComponent filterPageComponent = new FilterPageComponent();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            InvHubDateFormat.MONTH_DAY_YEAR.getFormatterPattern()).withLocale(Locale.US);

    public OpenDetectionsPageComponent selectRowByRowData(Map<String, String> rowToFind) {
        getRowByContent(rowToFind).$("td[class='checkboxColumn']")
                .as("Select row icon")
                .click();
        return new OpenDetectionsPageComponent();
    }

    @Step("User sorts Open AML Detections table by column")
    public OpenDetectionsTablePageComponent sortTable(OpenDetectionsTablePageComponent.HeaderName headerName,
                                                      SortStatus sortStatus) {
        SelenideElement header = getHeaderElementByName(headerName.getStringValue());
        log.info("Sort Open AML Detections table by column: " + headerName.getStringValue());
        sortTableWithAriaLabel(header, sortStatus.getAriaLabel());
        return this;
    }

    @Getter
    @AllArgsConstructor
    public enum HeaderName implements GeneralTableHeaderName {

        TIME_PERIOD("Time Period"),
        SCENARIO_NAME("Scenario Name"),
        EVENT_DATE("Event Date"),
        DUE_DATE("Due Date");

        public final String stringValue;
    }
}