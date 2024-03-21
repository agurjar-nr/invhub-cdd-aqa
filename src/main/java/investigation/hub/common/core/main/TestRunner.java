package investigation.hub.common.core.main;

import org.testng.TestNG;

public class TestRunner {

    public static void main(String[] args) {
        System.setProperty("log4j.configurationFile", "configurations/log4j2.xml");
        System.setProperty("allure.report.enable", "true");
        TestNG.privateMain(args, null);
    }

}
