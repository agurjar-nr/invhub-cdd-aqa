package investigation.hub.common.web.test.data.dtos.transaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Builder()
@ToString()
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class Actor {
    @JsonProperty("role")
    private String role;
    @JsonProperty("address")
    private Address address;
    @JsonProperty("segment")
    private String segment;
    @JsonProperty("name")
    private String name;
    @JsonProperty("srcRefId")
    private String srcRefId;
    @JsonProperty("id")
    private String id;
    @JsonProperty("account")
    private Account account;
}