package investigation.hub.common.web.components;

import com.smile.components.PageComponent;
import investigation.hub.common.web.components.tables.EventLogTablePageComponent;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
@PageComponent
public class EventLogPageComponent extends PageGeneralComponent {

    private EventLogTablePageComponent eventLogTablePageComponent = new EventLogTablePageComponent();

}
