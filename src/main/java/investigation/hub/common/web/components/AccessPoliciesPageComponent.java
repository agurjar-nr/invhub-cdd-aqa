package investigation.hub.common.web.components;

import com.smile.components.PageComponent;
import investigation.hub.common.web.components.tables.DataAccessTablePageComponent;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@PageComponent
@Getter
public class AccessPoliciesPageComponent {
    DataAccessTablePageComponent dataAccessTablePageComponent = new DataAccessTablePageComponent();
}