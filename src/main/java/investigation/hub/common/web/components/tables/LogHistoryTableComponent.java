package investigation.hub.common.web.components.tables;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.PageComponent;
import investigation.hub.common.web.components.FilterPageComponent;
import investigation.hub.common.web.enums.InvHubDateFormat;
import io.qameta.allure.Step;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

@Getter
@Log4j2
@PageComponent
public class LogHistoryTableComponent extends TableGeneralComponent<LogHistoryTableComponent.HeaderName> {

    private final FilterPageComponent filterPageComponent = new FilterPageComponent();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            InvHubDateFormat.YEAR_MONTH_DAY_TIME.getFormatterPattern()).withLocale(Locale.US);
    private final DateTimeFormatter teamLogTimeFormatter = DateTimeFormatter.ofPattern(
            InvHubDateFormat.ISO_DATE_TIME.getFormatterPattern()).withLocale(Locale.US);

    public List<String> getTimeColumnData() {
        return getColumnData(LogHistoryTableComponent.HeaderName.TIME.stringValue);
    }

    public String getAnyTimeColumnValue() {
        List<String> values = new ArrayList<>(getTimeColumnData());
        Collections.shuffle(values);
        return values
                .stream()
                .findAny()
                .orElseThrow(() -> new RuntimeException("No values are found for datetime column!"));
    }

    @Step("User sorts Log History table by column")
    public void sortTable(HeaderName headerName, SortStatus sortStatus) {
        SelenideElement header = getHeaderElementByName(headerName.getStringValue());
        log.info("Sort Log History table by column: %s and direction: %s".formatted(headerName.getStringValue(), sortStatus));
        sortTableWithDataTestid(header, sortStatus.getDataTestId());
    }

    public Map<String, String> getRecentAuditLogData() {
        List<String> columns = header.$$("th").asDynamicIterable()
                .stream()
                .map(it -> it.getText().trim())
                .toList();
        String regex = "\n";
        List<String> cells = body.$("tr").$$("td").asDynamicIterable()
                .stream()
                .map(it -> it.$("div").getAttribute("innerText")).filter(Objects::nonNull)
                .map(it -> StringUtils.join(Arrays.stream(it.split(regex)).map(this::isValidFormat).toList(), regex))
                .toList();
        log.info("Get recent Audit Log data from the table");
        return IntStream.range(0, columns.size())
                .boxed()
                .collect(Collectors.toMap(columns::get, cells::get));
    }

    /*
     * Define th date and change format or return the value
     */
    private String isValidFormat(String input) {
        try {
            return DateTimeFormatter.ofPattern(InvHubDateFormat.MONTH_DAY_YEAR.getFormatterPattern())
                    .format(teamLogTimeFormatter.parse(input));
        } catch (DateTimeParseException e) {
            return input;
        }
    }

    @Getter
    @AllArgsConstructor
    public enum HeaderName implements GeneralTableHeaderName {
        TIME("Time"),
        ACTIVITY("Activity"),
        PERFORMED_ON("Performed On"),
        PERFORMED_BY("Performed By"),
        ATTRIBUTE_NAME("Attribute Name"),
        NEW_VALUE("New Value"),
        OLD_VALUE("Old Value");

        private final String stringValue;
    }
}
