package investigation.hub.common.core.services.api.model.provider;

import groovy.util.logging.Log4j2;
import investigation.hub.common.core.services.api.model.team.Role;


@Log4j2
public class RoleProvider implements Provider<Role> {
    private final Role role;

    public RoleProvider(investigation.hub.common.web.test.data.constants.Role role) {
        this.role = Role.builder()
                .id(role.getId())
                .name(role.getStringValue())
                .build();
    }

    @Override
    public Role provide() {
        return role;
    }
}