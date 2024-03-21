package investigation.hub.common.web.test.data.dtos;

import investigation.hub.common.web.test.data.constants.AccessPolicy;
import investigation.hub.common.web.test.data.constants.Role;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TeamDto {
    private String name;
    private String description;
    private Role role;
    private AccessPolicy accessPolicy;
    private String createdOn;
}
