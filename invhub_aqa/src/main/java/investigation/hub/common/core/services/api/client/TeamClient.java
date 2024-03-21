package investigation.hub.common.core.services.api.client;

import static java.lang.Boolean.parseBoolean;

import com.fasterxml.jackson.databind.ObjectMapper;
import investigation.hub.common.core.services.api.model.provider.AssignUsersToTeamProvider;
import investigation.hub.common.core.services.api.model.provider.TeamInputProvider;
import investigation.hub.common.core.services.api.model.team.Team;
import investigation.hub.common.core.services.api.model.user.UserGraphQL;
import java.util.List;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class TeamClient extends ApiClient {

    public String createTeam(Team team) {
        action = "createTeam";
        query = getGraphQLQuery(action, new TeamInputProvider(team).provide());
        response = graphQlRequest(query);

        log.info("response: " + response.getBody().asString());
        return getResponseId(action,response);
    }

    public String addUsersToTeam(List<UserGraphQL> users, Team team) {
        action = "assignUsersToTeam";
        query = getGraphQLQuery(action, new AssignUsersToTeamProvider(users,team).provide());
        response = graphQlRequest(query);
        return response.jsonPath().getString("data.%s.assignedUsers.id".formatted(action));
    }

    public Boolean deleteTeam(String teamId) {
        action = "deleteTeam";
        response = getOrDeleteEntity(action, teamId);
        return parseBoolean(response.jsonPath().getString("data.%s.success".formatted(action)));
    }

    public String updateTeam(Team team) {
        action = "updateTeam";
        query = getGraphQLQuery(action, new TeamInputProvider(team, true).provide());
        response = graphQlRequest(query);
        return getResponseId(action,response);
    }

    public Team getTeam(String teamId) {
        action = "getTeamById";
        response = getOrDeleteEntity(action, teamId);
        objectMapper = new ObjectMapper();
        return objectMapper.convertValue(response.jsonPath().get("data.%s".formatted(action)), Team.class);
    }
}
