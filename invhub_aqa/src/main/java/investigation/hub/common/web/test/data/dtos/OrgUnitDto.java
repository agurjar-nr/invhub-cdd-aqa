package investigation.hub.common.web.test.data.dtos;

import investigation.hub.common.web.components.tables.UnitsTablePageComponent.Codes;
import investigation.hub.common.web.components.tables.UnitsTablePageComponent.HeaderName;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrgUnitDto {
    private String code;
    private String label;
    private Codes parentOrgUnit;
    private Codes childOrgUnit;
    private String updatedOn;

    public Map<String, String> getStringMap() {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put(HeaderName.CODE.getStringValue(), getCode());
        stringMap.put(HeaderName.LABEL.getStringValue(), getLabel());
        stringMap.put(HeaderName.PARENT_ORG_UNITS.getStringValue(), getParentOrgUnit().getStringValue());
        return stringMap;
    }
}
