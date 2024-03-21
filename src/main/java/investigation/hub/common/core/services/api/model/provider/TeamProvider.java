package investigation.hub.common.core.services.api.model.provider;

import com.github.javafaker.Faker;
import groovy.util.logging.Log4j2;
import investigation.hub.common.core.services.api.model.team.Queue;
import investigation.hub.common.core.services.api.model.team.Team;
import investigation.hub.common.web.test.data.constants.AccessPolicy;
import investigation.hub.common.web.test.data.constants.Role;
import java.util.List;


@Log4j2
public class TeamProvider implements Provider<Team> {
    private final Team team;

    public TeamProvider(Role role, List<Queue> queues, AccessPolicy policy) {
        String prefix = "AQA_API_TEST_TEAM";
        Faker faker = new Faker();
        String uuid = faker.internet().uuid();
        team = Team.builder()
                .name("%s_%s_%s".formatted(prefix,faker.funnyName().name(),uuid))
                .description("%s_DESCRIPTION_%s".formatted(prefix,uuid))
                .accessPolicy(getPolicy(policy))
                .createdOn(new DateTimeProvider(faker,100).provide())
                .updatedOn(new DateTimeProvider(faker,10).provide())
                .membersCount(0)
                .roleIds(List.of(new RoleProvider(role).provide()))
                .queues(queues)
                .build();
    }

    private investigation.hub.common.core.services.api.model.team.AccessPolicy getPolicy(AccessPolicy policy) {
    return investigation.hub.common.core.services.api.model.team.AccessPolicy.builder()
            .id(policy.getId())
            .name(policy.getStringValue())
            .build();
    }

    @Override
    public Team provide() {
        return team;
    }
}