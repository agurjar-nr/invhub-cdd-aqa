package investigation.hub.common.core.listeners;

import lombok.extern.log4j.Log4j2;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

@Log4j2
public class NameLoggerTestListener implements IInvokedMethodListener, ITestListener {

//    private final Marker testLog = MarkerManager.getMarker("TEST_LOG"); TODO uncomment after updating log4j.xml

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.getTestMethod().isTest()) {
            log.info(
//                    testLog, TODO uncomment after updating log4j.xml
                    """
                                            
                            ----------------------------------------
                            STARTING: %s()
                            ----------------------------------------"""
                            .formatted(method.getTestMethod().getQualifiedName()));
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.info(
                """
                                    
                        SKIPPED: %s()
                        """.formatted(result.getMethod().getMethodName()));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info(
                """
                                        
                        SUCCESS: %s()
                        """.formatted(result.getMethod().getMethodName()));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error(
                """
                                    
                        FAILED: %s()
                        """.formatted(result.getMethod().getMethodName()));
    }
}
