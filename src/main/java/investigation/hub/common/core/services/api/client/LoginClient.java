package investigation.hub.common.core.services.api.client;

import investigation.hub.common.core.services.api.query.Email;
import investigation.hub.common.core.services.api.query.GraphQLQuery;
import investigation.hub.common.core.services.api.requests.authorization.Authorizer;
import investigation.hub.common.core.services.api.requests.authorization.EmptyAuthorizer;
import investigation.hub.common.core.services.api.requests.services.HttpRequestService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LoginClient extends ApiClient {

    public String getLoginCodeForEmail(String email) {
        Authorizer authorizer = new EmptyAuthorizer();
        requestService = new HttpRequestService.For()
                .authType(authorizer)
                .build();
        createNewLoginCode(email);
        return getLoginCode(email);
    }

    private void createNewLoginCode(String email) {
        graphQlRequest(getEmailGraphQlQuery("createNewLoginCode", email));
    }

    private String getLoginCode(String email) {
        action = "getLoginCode";
        query = getEmailGraphQlQuery(action, email);
        response = graphQlRequest(query);
        return response.jsonPath().getString("data.%s.code".formatted(action));
    }

    private GraphQLQuery getEmailGraphQlQuery(String action, String email) {
               return getGraphQLQuery(action,
                Email.builder()
                        .email(email)
                        .build());
    }
}