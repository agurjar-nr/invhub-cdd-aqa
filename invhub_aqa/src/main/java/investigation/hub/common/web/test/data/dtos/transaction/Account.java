package investigation.hub.common.web.test.data.dtos.transaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString()
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @JsonProperty("srcRefId")
    private String srcRefId;
    @JsonProperty("id")
    private String id;
    @JsonProperty("iban")
    private String iban;
    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;
    @JsonProperty("bic")
    private String bic;
    @JsonProperty("typeCode")
    private String typeCode;
    @JsonProperty("bank")
    private Bank bank;
    @JsonProperty("branchDetail")
    private List<BranchDetail> branchDetail;
}