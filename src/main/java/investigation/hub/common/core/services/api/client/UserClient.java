package investigation.hub.common.core.services.api.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import investigation.hub.common.core.services.api.model.user.UserGraphQL;
import java.util.List;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class UserClient extends ApiClient {

    public String createUser(UserGraphQL user) {
        action = "createUser";
        query = getGraphQLQuery(action, user);
        response = graphQlRequest(query);
        log.info("response: `%s`".formatted(response.body().asString()));
        return getResponseId(action,response);
    }

    public String deleteUser(String userId) {
        action = "deleteUser";
        response = getOrDeleteEntity(action, userId);
        return getResponseId(action,response);
    }

    public UserGraphQL getUser(String userId)  {
        action = "getUser";
        response = getOrDeleteEntity(action, userId);
        objectMapper = new ObjectMapper();
        return objectMapper.convertValue(response.jsonPath().get("data.%s".formatted(action)),UserGraphQL.class);
    }

    public List<UserGraphQL> getUsersByName(String name) {
        action = "getUsersByName";
        response = queryEntity(action, name);
        objectMapper = new ObjectMapper();
        return objectMapper.convertValue(response.jsonPath().get("data.%s".formatted(action)), new TypeReference<List<UserGraphQL>>() { });
    }
}
