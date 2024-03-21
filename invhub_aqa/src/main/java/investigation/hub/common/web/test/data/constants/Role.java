package investigation.hub.common.web.test.data.constants;

import static investigation.hub.common.web.test.data.constants.AccessLevel.*;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ADMIN("admin", "Admin", FULL, FULL, FULL, FULL, FULL, FULL, FULL, FULL, FULL, FULL, FULL, FULL),
    L1_INVESTIGATOR("l1Investigator", "L1 Investigator", NONE, NONE, NONE, FULL, FULL, NONE, NONE, NONE, NONE, NONE, NONE, FULL),
    L2_INVESTIGATOR("l2Investigator", "L2 Investigator",NONE,  NONE, NONE, FULL, FULL, NONE, NONE, NONE, NONE, NONE, NONE, NONE),
    COMPLIANCE_MANAGER("complianceManager", "Compliance Manager",NONE,  NONE, NONE, FULL, FULL, FULL, FULL, FULL, NONE, NONE, FULL, NONE),
    READ_ONLY("readOnly", "Read Only",READ, READ, READ, FULL, READ, READ, READ, READ, READ, READ, READ, NONE),
    GUEST("guest", "Guest", NONE,NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE, NONE);

    private final String id;
    private final String stringValue;

    private final AccessLevel adminSmtp;
    private final AccessLevel adminUsers;
    private final AccessLevel adminRoles;
    private final AccessLevel userPreferences;
    private final AccessLevel openInvestigations;
    private final AccessLevel taskTypeManagement;
    private final AccessLevel adminTeamsManagement;
    private final AccessLevel adminQueuesManagement;
    private final AccessLevel adminSystemConfiguration;
    private final AccessLevel infrastructureManagement;
    private final AccessLevel adminDataAccessManagement;
    private final AccessLevel investigationTemporaryDetectionSuppression;
}