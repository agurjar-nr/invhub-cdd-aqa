package investigation.hub.common.web.components.tables;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.PageComponent;
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
public class DataAccessTablePageComponent extends TableGeneralComponent<DataAccessTablePageComponent.HeaderName> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            InvHubDateFormat.MONTH_DAY_YEAR.getFormatterPattern()).withLocale(Locale.US);

    @Step("User sorts Data Access table by column")
    public DataAccessTablePageComponent sortTable(HeaderName headerName, SortStatus sortStatus) {
        SelenideElement header = getHeaderElementByName(headerName.getStringValue());
        sortTableWithAriaLabel(header, sortStatus.getAriaLabel());
        log.info("Sort Data Access table by column: " + headerName.getStringValue());
        return this;
    }

    @Getter
    @AllArgsConstructor
    public enum HeaderName implements GeneralTableHeaderName {

        NAME("Name"),
        ORG_UNIT_FC_TYPE("Org Unit & FC Type"),
        DESCRIPTION("Description"),
        LAST_UPDATE("Last Update"),
        CREATED_ON("Created On");

        private final String stringValue;
    }
}
