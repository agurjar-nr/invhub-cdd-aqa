package investigation.hub.common.core.properties.loaders;

import com.codeborne.selenide.Configuration;
import investigation.hub.common.core.properties.ConfigProperties;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.aeonbits.owner.ConfigFactory;
import org.testng.ITestListener;

@Log4j2
public class ConfigLoader implements ITestListener {

    @Getter
    private static ConfigProperties configProperties;

    static {
        configProperties = ConfigFactory.create(ConfigProperties.class);
        log.info("Configurations are loaded");
    }

    static {
        applySelenideConfigurations();
    }

    private static void applySelenideConfigurations() {
        if (configProperties.getUseRemoteBrowsersProvider()) {
            Configuration.remote = configProperties.getRemoteBrowsersProviderUrl();
            log.info("Remote browser configured: " + Configuration.remote);
        }
    }

}
