package investigation.hub.common.core.services.api.model.provider;

import groovy.util.logging.Log4j2;
import investigation.hub.common.core.services.api.model.team.AssignQueuesToTeamInput;
import investigation.hub.common.core.services.api.model.team.Role;
import investigation.hub.common.core.services.api.model.team.Team;
import investigation.hub.common.core.services.api.model.team.TeamInput;

import java.util.List;
import java.util.stream.IntStream;


@Log4j2
public class TeamInputProvider implements Provider<TeamInput> {
    private final TeamInput teamInput;

    public TeamInputProvider(Team team, boolean isUpdateOperation) {
        teamInput = TeamInput.builder()
                .name(team.getName())
                .description(team.getDescription())
                .id(isUpdateOperation ? team.getId() : null)
                .roleIds(getRoleIds(team))
                .accessPolicyId(team.getAccessPolicy().getId())
                .build();
    }

    public TeamInputProvider(Team team) {
        teamInput = TeamInput.builder()
                .name(team.getName())
                .description(team.getDescription())
                .roleIds(getRoleIds(team))
                .accessPolicyId(team.getAccessPolicy().getId())
                .queues(getAssignQueuesToTeamInput(team))
                .build();
    }

    private List<String> getRoleIds(Team team) {
        return team.getRoleIds().stream().map(Role::getId).toList();
    }

    private List<AssignQueuesToTeamInput> getAssignQueuesToTeamInput(Team team) {
        return IntStream.range(0, team.getQueues().size())
                .boxed()
                .map(i -> AssignQueuesToTeamInput
                        .builder()
                        .id(team.getQueues().get(i).getId())
                        .priority(i + 1)
                        .build())
                .toList();
    }

    @Override
    public TeamInput provide() {
        return teamInput;
    }
}