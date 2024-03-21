package investigation.hub.common.core.services.api.model.orgUnit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class OrganisationUnit {
    @JsonProperty("id")
    private String code;
    @JsonProperty("name")
    private String name;
    @JsonProperty("parentId")
    private String parentCode;

}
