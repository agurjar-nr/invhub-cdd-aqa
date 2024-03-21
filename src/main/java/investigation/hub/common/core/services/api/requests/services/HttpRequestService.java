package investigation.hub.common.core.services.api.requests.services;

import investigation.hub.common.core.services.api.query.GraphQLQuery;
import investigation.hub.common.core.services.api.requests.authorization.Authorizer;
import io.restassured.response.Response;
import java.io.File;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Facade for all services that interact with any endpoints.
 * Created with builder
 * Entry point for any requests operations
 * */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpRequestService extends AbstractRequestService {

    private InvHubGraphQlRequestService graphQlRequestService;

    private EndpointsRestRequestService endpointsRestRequestService;

    public Response graphQlRequest(String query, String variables) {
        return graphQlRequestService.requestQuery(query, variables);
    }

    public Response graphQlRequest(String queryVariables) {
        return graphQlRequestService.requestQuery(queryVariables);
    }

    public Response graphQlRequest(String queryVariables, String variables, String token) {
        return graphQlRequestService.requestQueryWithToken(queryVariables, variables, token);
    }

    public Response graphQlRequest(GraphQLQuery body, String token) {
        return graphQlRequestService.mutationRequest(body, token);
    }


    public Response sendFileToEndpointV1UploadFile(File file) {
        return endpointsRestRequestService.sendFileToEndpointV1UploadFile(file);
    }

    /**
     *Executes authorization actions defined in the specific representation of the
     * Authorizer interface. Usually invoked once per class in @BeforeClass section.
     * Multiple invocations could increase the time of execution.
     * */
    public HttpRequestService authenticate() {
        authorizer.authorize();
        return this;
    }

    public static class For {
        private HttpRequestService requestService = new HttpRequestService();

        public For authType(Authorizer authorizer) {
             requestService.authorizer = authorizer;
            return this;
        }

        public HttpRequestService build() {
            requestService.graphQlRequestService = new InvHubGraphQlRequestService(requestService.authorizer);
            requestService.endpointsRestRequestService = new EndpointsRestRequestService(requestService.authorizer);
            return requestService;
        }
    }
}
