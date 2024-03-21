package investigation.hub.common.core.services.api.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserGraphQL {
	@JsonProperty("createdOn")
	private String createdOn;
	@JsonProperty("id")
	private String id;
	@JsonProperty("imageId")
	private String imageId;
	@JsonProperty("role")
	private String role;
	@JsonProperty("permissions")
	private Permissions permissions;
	@JsonProperty("last_name")
	private String lastName;
	@JsonProperty("language")
	private String language;
	@JsonProperty("updatedOn")
	private String updatedOn;

	@JsonProperty("time_zone")
	private String timeZone;
	@JsonProperty("timeZoneName")
	private String timeZoneName;

	@JsonProperty("first_name")
	private String firstName;
	@JsonProperty("weekStartsOn")
	private String weekStartsOn;
	@JsonProperty("email")
	private String email;
	@JsonProperty("enabled")
	private Boolean enabled;
	@JsonProperty("username")
	private String username;

	public String getFullName() {
		return "%s %s".formatted(getFirstName(), getLastName());
	}
}