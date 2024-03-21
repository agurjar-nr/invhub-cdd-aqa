package investigation.hub.common.core.services.api.client;

import investigation.hub.common.core.services.api.model.orgUnit.OrganisationUnit;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class OrganisationUnitClient extends ApiClient {

    public String createOrganisationUnit(OrganisationUnit organisationUnit) {
        action = "createOrganisationUnit";
        query = getGraphQLQuery(action, organisationUnit);
        response = graphQlRequest(query);
        log.info("response: " + response.getBody().asString());
        return getResponseId(action, response);
    }
}
