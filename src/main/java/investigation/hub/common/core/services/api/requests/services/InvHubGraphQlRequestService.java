package investigation.hub.common.core.services.api.requests.services;

import investigation.hub.common.core.properties.ConfigProperties;
import investigation.hub.common.core.services.api.query.GraphQLQuery;
import investigation.hub.common.core.services.api.utils.RestAssuredWrapper;
import investigation.hub.common.core.services.api.requests.authorization.Authorizer;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;
import org.aeonbits.owner.ConfigFactory;

/**
* Service for interaction with GraphQl endpoint
* Provides possibility to send GraphQl queries and variables as authenticated user
* */
@Log4j2
public class InvHubGraphQlRequestService extends AbstractRequestService {

    private static final ConfigProperties config = ConfigFactory.create(ConfigProperties.class);

    private static final String SERVICE_PATH = "/graphql";

    public static final String VARIABLES_EMPTY = """
            "1":1
            """;

    public InvHubGraphQlRequestService(Authorizer authorizer) {
        super(authorizer);
    }

    /**
    * Sends request to GraphQl endpoint
    * query - value of 'query' json field
    * variables - value of 'variables' json field
    * */
    public Response requestQuery(String query, String variables) {
        String queryWithVariables = "{ \"query\": \"" + query + "\", \"variables\": {" + variables +  "}}";

        RequestSpecification reqSpec = authorizer.produceRequestWithAuthData()
                .setBasePath(config.getGraphQLPath())
                .setContentType(ContentType.JSON)
                .setBody(cleanUpRedundantNewLinesInRequest(queryWithVariables))
                .build();

        return RestAssuredWrapper.request().spec(reqSpec).post();
    }

    /**
     * Sends request to GraphQl endpoint
     * queryWithVariables - common string (json) with query and variables fields
     * */
    public Response requestQuery(String queryWithVariables) {
        RequestSpecification reqSpec = authorizer.produceRequestWithAuthData()
                .setBasePath(config.getGraphQLPath())
                .setContentType(ContentType.JSON)
                .setBody(cleanUpRedundantNewLinesInRequest(queryWithVariables))
                .build();

        return RestAssuredWrapper.request().spec(reqSpec).post();
    }

    public Response requestQueryWithToken(String query, String variables, String token) {
        String queryWithVariables = "{ \"query\": \"" + query + "\", \"variables\": {" + variables +  "}}";
        RequestSpecification reqSpec = authorizer.produceRequest()
                .addHeader("Authorization", "Bearer " + token)
                .setBasePath(config.getGraphQLPath())
                .setContentType(ContentType.JSON)
                .setBody(cleanUpRedundantNewLinesInRequest(queryWithVariables))
                .build();

        return RestAssuredWrapper.request().spec(reqSpec).post();
    }

    public Response mutationRequest(GraphQLQuery body, String token) {
     RequestSpecification reqSpec = authorizer.produceRequest()
                .addHeader("Authorization", "Bearer " + token)
                .setBasePath(config.getGraphQLPath())
                .setContentType(ContentType.JSON)
                .setBody(body)
                .build();
        return RestAssuredWrapper.request().spec(reqSpec).post();
    }
}
