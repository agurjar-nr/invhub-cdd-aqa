package investigation.hub.common.core.services.api.requests.authorization;

import investigation.hub.common.core.properties.ConfigProperties;
import investigation.hub.common.core.services.api.utils.RestAssuredWrapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.aeonbits.owner.ConfigFactory;

import java.net.URI;
import java.util.Objects;

/**
 * Implementation of the authorization process with email and confirmation code.
 * */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailAndCodeAuthorizer implements Authorizer {

    private LoginDataDto loginDataDto;
    private static final ConfigProperties config = ConfigFactory.create(ConfigProperties.class);

    /**
     * Creates a 'Login Code' in database for the specific user (email)
     * Equivalent of entering email on login form and press ‘Submit’ button
     * Method is 'synchronized' to avoid login issue on application side
     * */
    private static synchronized void createNewLoginCode(String userEmail, URI url) {
        String mutationCreateNewLoginCode =
                """
                    {"query":"mutation createNewLoginCode($input: CreateLoginCodeInput!) {
                        createNewLoginCode(input: $input) {
                            success
                            token
                            refreshToken
                            appMode
                            samlCallbackUrl
                        }
                    }",
                    "variables":{"input":{"email":"%s"}}}""";

        mutationCreateNewLoginCode = cleanUpRedundantNewLinesInRequest(String.
                format(mutationCreateNewLoginCode, userEmail)) ;

        RestAssuredWrapper.request()
                .contentType(ContentType.JSON)
                .request()
                .body(mutationCreateNewLoginCode)
                .post(url);
    }

    private static String cleanUpRedundantNewLinesInRequest(String inoutRequest) {
        return inoutRequest.replaceAll("\\n","");
    }

    @Override
    public RequestSpecBuilder produceRequestWithAuthData() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder
                .addHeader("Authorization", "Bearer " + loginDataDto.getAccessToken())
                .setBaseUri(config.getUrl());
        return requestSpecBuilder;
    }

    @Override
    public RequestSpecBuilder produceRequest() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        return requestSpecBuilder
                .setBaseUri(config.getUrl());
    }

    @Override
    public void authorize() {
        String graphQLPath = config.getGraphQLPath();
        URI endPoint = config.getUrl().resolve(graphQLPath);
        if (Objects.isNull(loginDataDto)) {
            createNewLoginCode(config.getTestUser(), endPoint);
            //loginDataDto = requestLoginData(credentialsData.getUser(), endPoint);
        }
    }

    @Getter
    public static class LoginDataDto {
        private String id;
        private String refreshToken;
        private String accessToken;
    }
}
