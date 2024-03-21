package investigation.hub.common.web.test.data.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccessPolicy {
    ALL_AML_ACCESS( "allAmlAccess","All AML Access"),
    EUR_AML_ACCESS( "eurAmlAccess","Eur AML Access");

    public final String id;
    public final String stringValue;

}
