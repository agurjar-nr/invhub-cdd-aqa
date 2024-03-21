package investigation.hub.common.web.components.tables;

import com.smile.components.PageComponent;
import investigation.hub.common.web.enums.InvHubDateFormat;
import investigation.hub.common.web.pages.TeamDetailsPage;
import io.qameta.allure.Step;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
@PageComponent
public class TeamsManagementTablePageComponent
        extends TableGeneralComponent<TeamsManagementTablePageComponent.HeaderName> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            InvHubDateFormat.MONTH_DAY_YEAR.getFormatterPattern()).withLocale(Locale.US);

    @Step("User clicks on Team by name")
    public TeamDetailsPage clickRowByTeamName(String teamName) {
        waitForTableContent();
        super.getRowByHeaderAndValue(HeaderName.NAME.getStringValue(), teamName)
                .$("a")
                .click();
        log.info("Click Team Details by team name: " + teamName);
        return new TeamDetailsPage();
    }

    @Step("User sorts Teams Managements table by column")
    public TeamsManagementTablePageComponent sortTable(HeaderName headerName, SortStatus sortStatus) {
        log.info(
                "Sort Teams Management table by column: %s and direction: %s".formatted(headerName.getStringValue(), sortStatus));
        sortTableWithAriaLabel(getHeaderElementByName(headerName.getStringValue()), sortStatus.getAriaLabel());
        return this;
    }

    @Getter
    @AllArgsConstructor
    public enum HeaderName implements GeneralTableHeaderName {

        NAME("Name"),
        DESCRIPTION("Description"),
        LAST_UPDATE("Last Update"),
        CREATED_ON("Created On"),
        MEMBERS("# of Members");

        public final String stringValue;
    }
}
