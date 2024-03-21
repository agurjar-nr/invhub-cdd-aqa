package investigation.hub.common.web.components.tables;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.PageComponent;
import investigation.hub.common.web.components.FilterPageComponent;
import investigation.hub.common.web.enums.InvHubDateFormat;
import io.qameta.allure.Step;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
@PageComponent
public class HistoricalDetectionsTablePageComponent
        extends DetectionsTablePageComponent<HistoricalDetectionsTablePageComponent.HeaderName> {

    private final FilterPageComponent filterPageComponent = new FilterPageComponent();

    private final DateTimeFormatter eventDateColumnFormatter = DateTimeFormatter.ofPattern(
            InvHubDateFormat.MONTH_DAY_YEAR.getFormatterPattern()).withLocale(Locale.US);
    private final DateTimeFormatter closedOnColumnFormatter = DateTimeFormatter.ofPattern(
            InvHubDateFormat.MONTH_DAY_YEAR_TIME.getFormatterPattern()).withLocale(Locale.US);

    @Step("User sorts Previous AML Detections table by column")
    public HistoricalDetectionsTablePageComponent sortTable(HeaderName headerName,
                                                            SortStatus sortStatus) {
        SelenideElement header = getHeaderElementByName(headerName.getStringValue());
        log.info("Sort Previous AML Detections table by column: " + headerName.getStringValue());
        sortTableWithAriaLabel(header, sortStatus.getAriaLabel());
        return this;
    }

    public int getUnusualAMLDetectionCount() {
        return getColumnData(HeaderName.OUTCOME.getStringValue()).stream().filter(it -> it.equals("UNUSUAL")).toList().size();
    }

    public int getNotUnusualAMLDetectionCount() {
        return getColumnData(HeaderName.OUTCOME.getStringValue()).stream().filter(it -> it.equals("NOT UNUSUAL")).toList().size();
    }

    @Getter
    @AllArgsConstructor
    public enum HeaderName implements GeneralTableHeaderName {

        TIME_PERIOD("Time Period"),
        SCENARIO_NAME("Scenario Name"),
        EVENT_DATE("Event Date"),
        OUTCOME("Outcome"),
        CLOSED_ON("Closed On");

        public final String stringValue;
    }
}
