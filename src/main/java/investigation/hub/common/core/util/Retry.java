package investigation.hub.common.core.util;

import static java.lang.Integer.parseInt;

import investigation.hub.common.core.properties.ConfigProperties;
import org.aeonbits.owner.ConfigFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {
    private int retryCount = 0;
    private static final ConfigProperties config = ConfigFactory.create(ConfigProperties.class);

    @Override
    public boolean retry(ITestResult result) {
        try {
            if (!result.getThrowable().getCause().getMessage().contains("java.lang.AssertionError"))
                if (retryCount < parseInt(config.getRetryCount())) {
                    retryCount++;
                    return true;
                }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
