package investigation.hub.common.core.services.api.utils;

import com.jayway.jsonpath.PathNotFoundException;
import investigation.hub.common.core.services.api.utils.conditions.JsonCondition;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.restassured.response.Response;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.function.Consumer;

/**
 * Class for validating responses from GraphQl endpoint
 * and placing results of validation to AllureReport
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GraphqlResponseValidator {

    private Response response;

    public static GraphqlResponseValidator forResponse(Response response) {
        return new GraphqlResponseValidator(response);
    }

    public GraphqlResponseValidator should(JsonCondition... conditions) {
        processValidation(JsonCondition::doDirectValidation, conditions);
        return this;
    }

    public GraphqlResponseValidator shouldNot(JsonCondition... conditions) {
        processValidation(JsonCondition::doInvertedValidation, conditions);
        return this;
    }

    private void processValidation(Consumer<JsonCondition> consumer, JsonCondition... conditions) {
        for (JsonCondition condition : conditions) {
            condition.setResponse(response);
            if (condition.provideDescription().isEmpty()) {
                throw new RuntimeException("Provide description! Allure step can't have empty name!");
            }
            try {
                consumer.accept(condition);
                if (!condition.provideReportData().isEmpty()) {
                }
            } catch (AssertionError ae) {
                throw ae;
            } catch (PathNotFoundException pnfe) {
                Allure.step("Failed get value from JSON by provided path!", Status.BROKEN);
                throw pnfe;
            } finally {
                Allure.getLifecycle().stopStep();
            }
        }
    }

}
