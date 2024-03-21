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
import java.util.Locale;

@Getter
@Log4j2
@PageComponent
public class EventLogTablePageComponent extends TableGeneralComponent<EventLogTablePageComponent.HeaderName> {

    private final FilterPageComponent filterPageComponent = new FilterPageComponent();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            InvHubDateFormat.MONTH_DAY_YEAR_TIME.getFormatterPattern()).withLocale(Locale.US);

    @Step("User sorts Event Log table by column")
    public EventLogTablePageComponent sortTable(EventLogTablePageComponent.HeaderName headerName, SortStatus sortStatus) {
        SelenideElement header = getHeaderElementByName(headerName.getStringValue());
        sortTableWithAriaLabel(header, sortStatus.getAriaLabel());
        return this;
    }

    @Getter
    @AllArgsConstructor
    public enum HeaderName implements GeneralTableHeaderName {

        DATE("Date"),
        EVENT("Event"),
        PERFORMED_BY("Performed By"),
        SUMMARY("Summary");

        public final String stringValue;
    }
}
