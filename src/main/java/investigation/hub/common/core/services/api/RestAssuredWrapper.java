package investigation.hub.common.core.services.api;

import static io.restassured.RestAssured.given;

import io.restassured.specification.RequestSpecification;

public class RestAssuredWrapper {

    public static RequestSpecification request() {
        return given().filter(new RestAssuredAllureReportLogFilter());
    }

}
