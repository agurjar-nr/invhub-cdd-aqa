package investigation.hub.common.web.test.data.repository;

import investigation.hub.common.web.components.tables.LogHistoryTableComponent.HeaderName;
import investigation.hub.common.web.test.data.dtos.OrgUnitDto;
import investigation.hub.common.web.test.data.dtos.QueueDto;
import investigation.hub.common.web.test.data.dtos.TeamDto;
import investigation.hub.common.web.test.data.dtos.UserDto;
import java.util.HashMap;
import java.util.Map;

public class LogRepository {
    private final static String CREATED_TEAM_ATTRIBUTES = "Team Name\nTeam Description\nAccess Policy\nCreated On\nroles";
    private final static String CREATED_QUEUE_ATTRIBUTES = "Queue Name\nQueue Description\nCreated On";
    private final static String UPDATED_TEAM_ATTRIBUTES = "Team Name\nTeam Description\nAccess Policy\nUpdated On\nroles";
    private final static String UPDATED_QUEUE_ATTRIBUTES = "Queue Name\nQueue Description\nUpdated On";
    private final static String UPDATED_ORG_UNIT_ATTRIBUTES = "ID\nOrganisation Unit Name\nUpdated On";

    private LogRepository() {
        throw new IllegalStateException("LogRepository class is not expected to be instantiated");
    }

    public static Map<String, String> getCreateTeamLog(TeamDto team) {
        Map<String, String> log = new HashMap<>();
        log.put(HeaderName.ACTIVITY.getStringValue(), "Create Team");
        log.put(HeaderName.PERFORMED_BY.getStringValue(), UserDtoRepository.createTestUserInstance().getEmail());
        log.put(HeaderName.PERFORMED_ON.getStringValue(), "TEAM: %s".formatted(team.getName()));
        log.put(HeaderName.ATTRIBUTE_NAME.getStringValue(), CREATED_TEAM_ATTRIBUTES);
        log.put(HeaderName.NEW_VALUE.getStringValue(), getTeamValues(team));
        log.put(HeaderName.OLD_VALUE.getStringValue(), "");
        return log;
    }

    public static Map<String, String> getCreateQueueLog(QueueDto queue) {
        Map<String, String> log = new HashMap<>();
        log.put(HeaderName.ACTIVITY.getStringValue(), "Create Queue");
        log.put(HeaderName.PERFORMED_BY.getStringValue(), UserDtoRepository.createTestUserInstance().getEmail());
        log.put(HeaderName.PERFORMED_ON.getStringValue(), "Queue: %s".formatted(queue.getName()));
        log.put(HeaderName.ATTRIBUTE_NAME.getStringValue(), CREATED_QUEUE_ATTRIBUTES);
        return log;
    }

    public static Map<String, String> getDeleteTeamLog(TeamDto team) {
        Map<String, String> log = new HashMap<>();
        log.put(HeaderName.ACTIVITY.getStringValue(), "Delete Team");
        log.put(HeaderName.PERFORMED_BY.getStringValue(), UserDtoRepository.createTestUserInstance().getEmail());
        log.put(HeaderName.PERFORMED_ON.getStringValue(), "TEAM: %s".formatted(team.getName()));
        log.put(HeaderName.ATTRIBUTE_NAME.getStringValue(), UPDATED_TEAM_ATTRIBUTES);
        log.put(HeaderName.NEW_VALUE.getStringValue(), "");
        log.put(HeaderName.OLD_VALUE.getStringValue(), getTeamValues(team));
        return log;
    }

    public static Map<String, String> getUpdateTeamLog(TeamDto createdTeam, TeamDto updatedTeam) {
        Map<String, String> log = new HashMap<>();
        log.put(HeaderName.ACTIVITY.getStringValue(), "Update Team");
        log.put(HeaderName.PERFORMED_BY.getStringValue(), UserDtoRepository.createTestUserInstance().getEmail());
        log.put(HeaderName.PERFORMED_ON.getStringValue(), "TEAM: %s".formatted(createdTeam.getName()));
        log.put(HeaderName.ATTRIBUTE_NAME.getStringValue(), UPDATED_TEAM_ATTRIBUTES);
        log.put(HeaderName.NEW_VALUE.getStringValue(), getTeamValues(updatedTeam));
        log.put(HeaderName.OLD_VALUE.getStringValue(), getTeamValues(createdTeam));
        return log;
    }

    public static Map<String, String> getUpdateQueueLog(QueueDto updatedQueue) {
        Map<String, String> log = new HashMap<>();
        log.put(HeaderName.ACTIVITY.getStringValue(), "Update Queue");
        log.put(HeaderName.PERFORMED_BY.getStringValue(), UserDtoRepository.createTestUserInstance().getEmail());
        log.put(HeaderName.PERFORMED_ON.getStringValue(), "Queue: %s".formatted(updatedQueue.getName()));
        log.put(HeaderName.ATTRIBUTE_NAME.getStringValue(), UPDATED_QUEUE_ATTRIBUTES);
        return log;
    }

    public static Map<String, String> getAddTeamMembersLog(TeamDto team, UserDto member) {
        Map<String, String> log = new HashMap<>();
        log.put(HeaderName.ACTIVITY.getStringValue(), "User assigned to a team");
        log.put(HeaderName.PERFORMED_BY.getStringValue(), UserDtoRepository.createTestUserInstance().getEmail());
        log.put(HeaderName.PERFORMED_ON.getStringValue(), "TEAM: %s".formatted(team.getName()));
        log.put(HeaderName.ATTRIBUTE_NAME.getStringValue(), "Team Users");
        log.put(HeaderName.NEW_VALUE.getStringValue(), member.getEmail());
        log.put(HeaderName.OLD_VALUE.getStringValue(), "");
        return log;
    }

    public static Map<String, String> getRemoveTeamMembersLog(TeamDto team, UserDto member) {
        Map<String, String> log = new HashMap<>();
        log.put(HeaderName.ACTIVITY.getStringValue(), "Users removed from a team");
        log.put(HeaderName.PERFORMED_BY.getStringValue(), UserDtoRepository.createTestUserInstance().getEmail());
        log.put(HeaderName.PERFORMED_ON.getStringValue(), "TEAM: %s".formatted(team.getName()));
        log.put(HeaderName.ATTRIBUTE_NAME.getStringValue(), "Team Users");
        log.put(HeaderName.NEW_VALUE.getStringValue(), "");
        log.put(HeaderName.OLD_VALUE.getStringValue(), member.getEmail());
        return log;
    }

    private static String getTeamValues(TeamDto team) {
        return "%s\n%s\n%s\n%s\n%s".formatted(team.getName(), team.getDescription(),
                team.getAccessPolicy().getStringValue(), team.getCreatedOn(), team.getRole().getStringValue());
    }

    private static String getQueueValues(QueueDto queue) {
        return "%s\n%s".formatted(queue.getName(), queue.getDescription());
    }

    public static Map<String, String> getUpdateOrganisationUnitLog(OrgUnitDto createdOrgUnit, OrgUnitDto updatedOrgUnit) {
        Map<String, String> log = new HashMap<>();
        log.put(HeaderName.ACTIVITY.getStringValue(), "Update Organisation Unit");
        log.put(HeaderName.PERFORMED_BY.getStringValue(), UserDtoRepository.createTestUserInstance().getEmail());
        log.put(HeaderName.PERFORMED_ON.getStringValue(), "Organisation Unit: %s".formatted(createdOrgUnit.getLabel()));
        log.put(HeaderName.ATTRIBUTE_NAME.getStringValue(), UPDATED_ORG_UNIT_ATTRIBUTES);
        log.put(HeaderName.NEW_VALUE.getStringValue(), getOrgUnitValues(updatedOrgUnit));
        log.put(HeaderName.OLD_VALUE.getStringValue(), getOrgUnitValues(createdOrgUnit));
        return log;
    }

    private static String getOrgUnitValues(OrgUnitDto orgUnit) {
        return "%s\n%s\n%s".formatted(orgUnit.getCode(), orgUnit.getLabel(), orgUnit.getUpdatedOn());
    }
}
