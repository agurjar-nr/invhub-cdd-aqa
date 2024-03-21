package investigation.hub.common.core.services.api.conditions;

import io.restassured.response.Response;

public interface JsonCondition {

    void setResponse(Response response);

    /**
     * Description in this context is the mane of Allure step
     * Mandatory data, don't leave it empty
     * */
    default String provideDescription() {
        return "";
    }
    /**
     * Contains direct validation logic
     * direct validation - will be invoked on should() validator method
     * */
    default void doDirectValidation() {
        throw new UnsupportedOperationException("Please, provide direct validation logic!");
    }

    /**
     * Contains inverted validation logic
     * inverted validation - will be invoked on shouldNot() validator method
     * */
    default void doInvertedValidation() {
        throw new UnsupportedOperationException("Please, provide inverted validation logic!");
    }

    /**
     * Provide here the logic of extracting the data that should be added as TXT to
     * allure report.
     * Empty string will be ignored
     * */
    default String provideReportData() {
        return "";
    }
}
