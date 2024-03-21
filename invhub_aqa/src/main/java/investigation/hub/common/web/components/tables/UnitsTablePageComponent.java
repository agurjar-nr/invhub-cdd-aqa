package investigation.hub.common.web.components.tables;

import com.smile.components.PageComponent;
import investigation.hub.common.web.components.modals.UpdateOrgUnitModalComponent;
import io.qameta.allure.Step;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
@PageComponent
public class UnitsTablePageComponent extends TableGeneralComponent<UnitsTablePageComponent.HeaderName> {

    public List<String> getCodesColumnData() {
        return getColumnData(HeaderName.CODE.getStringValue());
    }

    public String getOrgUnitMatchesValue(String orgUnitRegex) {
        return getColumnData(HeaderName.CODE.getStringValue())
                .stream()
                .filter(it -> it.matches(orgUnitRegex)).findFirst()
                .orElseThrow(() -> new RuntimeException("No values are found for the '%s' regex value!".formatted(orgUnitRegex)));
    }

    @Step("User clicks on Org Unit by code")
    public UpdateOrgUnitModalComponent clickRowByOrgUnitCode(String orgUnitCode) {
        super.getRowByHeaderAndValue(HeaderName.CODE.getStringValue(), orgUnitCode)
                .$("td span")
                .click();
        log.info("Click on Org Unit code: " + orgUnitCode);
        return new UpdateOrgUnitModalComponent();
    }

    public Map<String, String> getRowContentByOrgUnitCode(String orgUnitCode) {
        log.info("Get row content of Org Unit by code: " + orgUnitCode);
        return rowToMap(HeaderName.getAllStringValues(),  super.getRowByHeaderAndValue(HeaderName.CODE.getStringValue(),
                orgUnitCode));
    }

    @Getter
    @AllArgsConstructor
    public enum HeaderName implements GeneralTableHeaderName {

        CODE("Code"),
        LABEL("Label"),
        PARENT_ORG_UNITS("Parent Organisation Units"),
        CHILD_ORG_UNITS("Child Organisation Units");

        public final String stringValue;

        public static List<String> getAllStringValues() {
            return Arrays.stream(values())
                    .map(HeaderName::getStringValue)
                    .toList();
        }
    }

    @Getter
    @AllArgsConstructor
    public enum Codes implements GeneralTableHeaderName {

        GROUP("GROUP", "GROUP - Group"),
        EUR("EUR", "EUR - Europe"),
        NA("NA", "Na - North America"),
        APAC("APAC", "APAC - Asia Pacific");

        private final String stringValue;
        private final String codeLabelValue;

        public static List<String> getAllCodesValue() {
            return Arrays.stream(values())
                    .map(Codes::getStringValue)
                    .toList();
        }
    }
}
