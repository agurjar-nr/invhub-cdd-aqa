package investigation.hub.common.core.services.api.model.provider;

import groovy.util.logging.Log4j2;
import investigation.hub.common.core.services.api.model.user.UserGraphQL;
import investigation.hub.common.core.services.api.model.team.AssignUsersToTeam;
import investigation.hub.common.core.services.api.model.team.Team;
import java.util.List;


@Log4j2
public class AssignUsersToTeamProvider implements Provider<AssignUsersToTeam> {
    private final AssignUsersToTeam usersToTeam;

    public AssignUsersToTeamProvider(List<UserGraphQL> users , Team team) {
        usersToTeam = AssignUsersToTeam.builder()
                .teamId(team.getId())
                .userIds(users.stream().map(UserGraphQL::getId).toList())
                .build();
    }
    @Override
    public AssignUsersToTeam provide() {
        return usersToTeam;
    }
}