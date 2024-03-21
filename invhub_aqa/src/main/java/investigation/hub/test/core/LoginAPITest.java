package investigation.hub.test.core;

import investigation.hub.common.core.services.api.GraphqlResponseValidator;
import investigation.hub.common.core.services.api.conditions.JsonConditions;
import investigation.hub.common.core.services.api.requests.authorization.Authorizer;
import investigation.hub.common.core.services.api.requests.authorization.EmptyAuthorizer;
import investigation.hub.common.core.services.api.requests.services.HttpRequestService;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Random;

public class LoginAPITest {

    private HttpRequestService requestService;
    private String fakeCode = generateFakeCode();
    private String email = "test.user@test.com";
    String loginQuery = """
            mutation Login($email: String!, $loginCode: Float!) {
                login(input: {email: $email, loginCode: $loginCode}) {
                    refreshToken
                    accessToken
                    id
                }
            }""";
    String newLoginCodeCreationQuery = """
            mutation createNewLoginCode($input: CreateLoginCodeInput!) {
                createNewLoginCode(input: $input) {
                    success
                    token
                    refreshToken
                    appMode
                    samlCallbackUrl
                }
            }""";

    @BeforeClass
    public void beforeClass() {
        Authorizer authorizer = new EmptyAuthorizer();
        requestService = new HttpRequestService.For()
                .authType(authorizer)
                .build();
    }

    @Test
    public void testCreateNewLoginCode() {
        String variables = """
                "input": {
                    "email": "%s"
                 }""".formatted(email);

        Response response = requestService.graphQlRequest(newLoginCodeCreationQuery, variables);
        GraphqlResponseValidator.forResponse(response)
                .should(JsonConditions.jsonNodePresents("$['data']['createNewLoginCode']"));
    }

    @Test
    public void loginWithEmptyEmailNegativeTest() {
        String variables = """
                "input": {
                    "email": ""
                 }""";

        Response response = requestService.graphQlRequest(newLoginCodeCreationQuery, variables);
        GraphqlResponseValidator.forResponse(response)
                .should(JsonConditions.jsonValueEquals("$['errors'][0]['message']", "Bad Request Exception"))
                .should(JsonConditions.jsonValueEquals("$['errors'][0]['extensions']['response']['statusCode']", "400"))
                .should(JsonConditions.jsonValueEquals("$['errors'][0]['extensions']['response']['message'][0]", "email should not be empty"))
                .should(JsonConditions.jsonValueEquals("$['errors'][0]['extensions']['code']", "BAD_USER_INPUT"));
    }

    @Test
    public void loginWithInvalidCodeNegativeTest() {
        String variables = """
                "email": "%s",
                "loginCode": %s""".formatted(email, fakeCode);

        Response response = requestService.graphQlRequest(loginQuery, variables);
        GraphqlResponseValidator.forResponse(response)
                .should(JsonConditions.jsonValueEquals("$['errors'][0]['message']", "Invalid login credentials."))
                .should(JsonConditions.jsonValueEquals("$['errors'][0]['extensions']['exception']['status']", "401"))
                .should(JsonConditions.jsonValueEquals("$['errors'][0]['extensions']['exception']['response']", "Invalid login credentials."));
    }

    @Test
    public void loginWithoutCodeNegativeTest() {
        String variables = """
                "email": "%s",
                "loginCode": %s""".formatted(email, null);

        Response response = requestService.graphQlRequest(loginQuery, variables);
        GraphqlResponseValidator.forResponse(response)
                .should(JsonConditions.jsonValueEquals("$['errors'][0]['message']", "Variable \"$loginCode\" of non-null type \"Float!\" must not be null."))
                .should(JsonConditions.jsonValueEquals("$['errors'][0]['extensions']['code']", "BAD_USER_INPUT"));
    }

    private String generateFakeCode() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }
}
