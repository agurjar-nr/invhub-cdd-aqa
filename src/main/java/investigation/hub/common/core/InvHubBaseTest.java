package investigation.hub.common.core;

import com.smile.testng.executors.BaseTest;
import com.smile.testng.listeners.ExtendedReportPortalUiTestsListener;
import investigation.hub.common.core.listeners.NameLoggerTestListener;
import investigation.hub.common.core.properties.loaders.ConfigLoader;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Listeners;

@Log4j2
@Listeners({ExtendedReportPortalUiTestsListener.class, NameLoggerTestListener.class, ConfigLoader.class})
public class InvHubBaseTest extends BaseTest {

}
