package investigation.hub.common.core.services.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import investigation.hub.common.core.properties.ConfigProperties;
import investigation.hub.common.core.services.api.RestAssuredWrapper;
import investigation.hub.common.core.services.api.query.*;
import investigation.hub.common.core.services.api.requests.authorization.EmptyAuthorizer;
import investigation.hub.common.core.services.api.requests.services.HttpRequestService;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import org.aeonbits.owner.ConfigFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Log4j2
public class ApiClient {
    String action;
    ObjectMapper objectMapper;

    protected static final ConfigProperties config = ConfigFactory.create(ConfigProperties.class);
    protected HttpRequestService requestService = new HttpRequestService.For()
            .authType(new EmptyAuthorizer())
            .build();
    protected Response response;
    protected GraphQLQuery query;
    private String accessToken;

    static String readGraphqlFile(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            throw new RuntimeException("Not possible to read defied graphql file: " + e);
        }
    }

    public String getAccessToken() {
        if (ofNullable(accessToken).isPresent()) {
            return accessToken;
        }
        Map<String, String> apiLoginInfo = new HashMap<>();
        apiLoginInfo.put("email", config.getApiUser());
        apiLoginInfo.put("apiKey", config.getApiKey());

        GraphQLQuery query = getGraphQLQuery(config.getApiLoginGraphql(), apiLoginInfo);

        Response response = RestAssuredWrapper.request()
                .contentType(ContentType.JSON)
                .request()
                .body(query)
                .post(config.getUrl() + config.getGraphQLPath());
        accessToken = response.jsonPath().getString("data.apiLogin.accessToken");
        return accessToken;
    }

    protected Response graphQlRequest(GraphQLQuery body) {
        return requestService.graphQlRequest(body, getAccessToken());
    }

    protected GraphQLQuery getGraphQLQuery(String action, Object variables) {
        return GraphQLQuery.builder()
                .query(readGraphqlFile(config.getGraphqlRequestPath().formatted(action)))
                .variables(variables)
                .build();
    }

    public Response getOrDeleteEntity(String graphqlFileName, String id) {
        query = getGraphQLQuery(graphqlFileName,
                VariableId.builder()
                        .id(id)
                        .build()
        );
        response = graphQlRequest(query);
        log.info("action:`%s`, query: `%s`, response:`%s` ".formatted(action, query, response.getBody().asString()));
        return response;
    }

    public Response queryEntity(String graphqlFileName, String value) {
        query = getGraphQLQuery(graphqlFileName,
                Query.builder()
                        .query(value)
                        .build()
        );
        response = graphQlRequest(query);
        log.info("action:`%s`, query: `%s`, response:`%s` ".formatted(action, query, response.getBody().asString()));
        return response;
    }

    public String getResponseId(String action, Response response) {
        return response.jsonPath().getString("data.%s.id".formatted(action));
    }

    public String resetCache() {
        String action = "invalidateCache";
        InvalidateCacheInput invalidateCacheInput = InvalidateCacheInput
                .builder()
                .input(Pattern
                        .builder()
                        .pattern("")
                        .build())
                .build();
        query = getGraphQLQuery(action, invalidateCacheInput);
        response = graphQlRequest(query);
        log.info("action:`%s`, query: `%s`, response:`%s` ".formatted(action, query, response.getBody().asString()));
        return response.jsonPath().getString("data.%s".formatted(action));
    }
}