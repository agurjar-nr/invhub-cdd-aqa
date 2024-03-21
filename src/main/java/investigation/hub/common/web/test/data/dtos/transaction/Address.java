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
public class Address {
    @JsonProperty("city")
    private String city;
    @JsonProperty("zone")
    private String zone;
    @JsonProperty("countryCode")
    private String countryCode;
    @JsonProperty("postalCode")
    private String postalCode;
    @JsonProperty("addressLine")
    private String addressLine;
}