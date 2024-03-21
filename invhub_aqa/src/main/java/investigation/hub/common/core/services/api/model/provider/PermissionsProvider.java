package investigation.hub.common.core.services.api.model.provider;


import groovy.util.logging.Log4j2;
import investigation.hub.common.core.services.api.model.user.Permissions;
import investigation.hub.common.web.test.data.constants.Role;


@Log4j2
public class PermissionsProvider implements Provider<Permissions> {
    private final Permissions permissions;

    public PermissionsProvider(Role role) {

        permissions = Permissions.builder()
                    .adminSmtp(role.getAdminSmtp().getAccessLevel())
                    .adminUsers(role.getAdminRoles().getAccessLevel())
                    .adminRoles(role.getAdminUsers().getAccessLevel())
                    .userPreferences(role.getUserPreferences().getAccessLevel())
                    .openInvestigations(role.getOpenInvestigations().getAccessLevel())
                    .taskTypeManagement(role.getTaskTypeManagement().getAccessLevel())
                    .adminTeamsManagement(role.getAdminTeamsManagement().getAccessLevel())
                    .adminQueuesManagement(role.getAdminQueuesManagement().getAccessLevel())
                    .adminSystemConfiguration(role.getAdminSystemConfiguration().getAccessLevel())
                    .infrastructureManagement(role.getInfrastructureManagement().getAccessLevel())
                    .adminDataAccessManagement(role.getAdminDataAccessManagement().getAccessLevel())
                    .investigationTemporaryDetectionSuppression(role.getInvestigationTemporaryDetectionSuppression().getAccessLevel())
                    .build();
    }

    @Override
    public Permissions provide() {
        return permissions;
    }
}