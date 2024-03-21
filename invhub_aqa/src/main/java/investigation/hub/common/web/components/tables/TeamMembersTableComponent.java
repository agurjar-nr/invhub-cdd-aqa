package investigation.hub.common.web.components.tables;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.PageComponent;
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
public class TeamMembersTableComponent extends TableGeneralComponent<TeamMembersTableComponent.HeaderName> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            InvHubDateFormat.MONTH_DAY_YEAR.getFormatterPattern()).withLocale(Locale.US);

    @Step("User sorts Team Members table by column")
    public TeamMembersTableComponent sortTable(HeaderName headerName, SortStatus sortStatus) {
        SelenideElement header = getHeaderElementByName(headerName.getStringValue());
        log.info("Sort Team Members table by column: " + headerName.getStringValue());
        sortTableWithAriaLabel(header, sortStatus.getAriaLabel());
        return this;
    }

    @Step("User selects checkbox in Team Members by row data")
    public TeamMembersTableComponent selectCheckboxByRowData(Map<String, String> rowToFind) {
        waitForTableContent();
        getRowByContent(rowToFind).$("td[class='checkboxColumn'] span")
                .as("Select row icon")
                .click();
        return new TeamMembersTableComponent();
    }

    @Getter
    @AllArgsConstructor
    public enum HeaderName implements GeneralTableHeaderName {

        MEMBER_NAME("Member Name"),
        ADDED_ON("Added On");

        public final String stringValue;
    }
}
