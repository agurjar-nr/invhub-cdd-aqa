package investigation.hub.common.core.services.api;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class AllureCommonActions {

    public static String allureStepStart(String name, Status status) {
        String stepId = UUID.randomUUID().toString();
        AllureLifecycle lifecycle = Allure.getLifecycle();
        lifecycle.startStep(stepId, (new StepResult()).setStatus(status)
                .setName(name));
        return stepId;
    }

    public static void allureAttachTxt(String name, String attachment) {
        Allure.getLifecycle().addAttachment(name, "text/plain", ".txt",
                attachment.getBytes(StandardCharsets.UTF_8));
    }

}
