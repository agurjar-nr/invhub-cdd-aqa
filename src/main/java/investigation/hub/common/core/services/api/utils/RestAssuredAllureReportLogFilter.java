package investigation.hub.common.core.services.api.utils;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class RestAssuredAllureReportLogFilter implements Filter {
    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                            FilterableResponseSpecification responseSpec,
                                FilterContext ctx) {
        AllureLifecycle lifecycle = Allure.getLifecycle();
        try {
            lifecycle.startStep(UUID.randomUUID().toString(), (new StepResult()).setStatus(Status.PASSED)
                    .setName(String.format("%s: %s", requestSpec.getMethod(), requestSpec.getURI())));
            lifecycle.addAttachment("REQUEST DATA", "text/plain", ".txt",
                    extractRequestData(requestSpec).getBytes(StandardCharsets.UTF_8));
            Response response = ctx.next(requestSpec, responseSpec);

            lifecycle.addAttachment("RESPONSE DATA", "text/plain", ".txt",
                    extractResponseData(response).getBytes(StandardCharsets.UTF_8));
            return response;
        } finally {
            lifecycle.stopStep();
        }
    }

    private String extractRequestData(FilterableRequestSpecification requestSpec) {
        StringBuilder sb = new StringBuilder();
        return sb.append("METHOD: '" + requestSpec.getMethod() + "'\n\n")
                .append("URI: '" + requestSpec.getURI() + "'\n\n")
                .append("COOKIES: '" + requestSpec.getCookies() + "'\n\n")
                .append("HEADERS: '" + requestSpec.getHeaders() + "'\n\n")
                .append("BODY: '" + requestSpec.getBody() + "'\n\n")
                .append("FORM DATA: '" + requestSpec.getFormParams() + "'\n\n")
                .append("MULTIPART PARAMS: '" + requestSpec.getMultiPartParams() + "'\n\n")
                .toString();
    }

    private String extractResponseData(Response response) {
        StringBuilder sb = new StringBuilder();
        return sb.append("STATUS: '" + response.getStatusLine() + "'\n\n")
                .append("HEADERS: '" + response.getHeaders() + "'\n\n")
                .append("COOKIES: '" + response.getCookies() + "'\n\n")
                .append("BODY: '" + response.getBody().asString() + "'\n\n")
                .toString();
    }
}
