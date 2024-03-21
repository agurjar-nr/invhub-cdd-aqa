package investigation.hub.common.web.components.tables;

import com.smile.components.PageComponent;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
@PageComponent
public class OrganisationUnitsTablePageComponent extends TableGeneralComponent<TeamMembersTableComponent.HeaderName> {

    public List<String> getCodesColumnData() {
        return getColumnData(HeaderName.CODE.getStringValue());
    }

    @Getter
    @AllArgsConstructor
    public enum HeaderName implements GeneralTableHeaderName {

        CODE("Code"),
        LABEL("Label"),
        PARENT_ORG_UNITS("Parent Organisation Units"),
        CHILD_ORG_UNITS("Child Organisation Units");

        public final String stringValue;
    }

    @Getter
    @AllArgsConstructor
    public enum Codes implements GeneralTableHeaderName {

        GROUP("GROUP"),
        EUR("EUR"),
        NA("NA"),
        APAC("APAC");

        private final String stringValue;

        public static List<String> getAllStringValues() {
            return Arrays.stream(values())
                    .map(Codes::getStringValue)
                    .toList();
        }
    }
}
