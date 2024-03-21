package investigation.hub.common.core.services.api.model.team;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Team {
    private int membersCount;
    private List<Role> roleIds;
    private String name;
    private String description;
    private String id;
    private AccessPolicy accessPolicy;
    private String updatedOn;
    private String createdOn;
    private List<Queue> queues;

}