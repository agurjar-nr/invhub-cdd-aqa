package investigation.hub.common.core.services.api.utils;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RestAssuredWrapper {

    public static RequestSpecification request() {
        return given().filter(new RestAssuredAllureReportLogFilter());
    }

}
