package investigation.hub.common.core.services.api.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Permissions{

	@JsonProperty("adminRoles")
	private String adminRoles;
	@JsonProperty("adminUsers")
	private String adminUsers;

	@JsonProperty("adminSystemConfiguration")
	private String adminSystemConfiguration;
	@JsonProperty("adminSmtp")
	private String adminSmtp;

	@JsonProperty("adminTeamsManagement")
	private String adminTeamsManagement;

	@JsonProperty("adminQueuesManagement")
	private String adminQueuesManagement;

	@JsonProperty("adminDataAccessManagement")
	private String adminDataAccessManagement;
	@JsonProperty("openInvestigations")
	private String openInvestigations;

	@JsonProperty("userPreferences")
	private String userPreferences;

	@JsonProperty("taskTypeManagement")
	private String taskTypeManagement;

	@JsonProperty("infrastructureManagement")
	private String infrastructureManagement;

	@JsonProperty("investigationTemporaryDetectionSuppression")
	private String investigationTemporaryDetectionSuppression;

}