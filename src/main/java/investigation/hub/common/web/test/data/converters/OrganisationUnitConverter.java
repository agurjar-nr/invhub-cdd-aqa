package investigation.hub.common.web.test.data.converters;

import investigation.hub.common.core.services.api.model.orgUnit.OrganisationUnit;
import investigation.hub.common.web.test.data.dtos.OrgUnitDto;

public class OrganisationUnitConverter {

    private OrganisationUnitConverter() {
        throw new IllegalStateException("OrganisationUnitConverter class is not expected to be instantiated");
    }

    /*
     * Converts Organisation Unit UI Entity to OrganisationUnitGraphQL Entity
     *
     * @param orgUnitDto to convert
     * @return converted GraphQL entity
     */
    public static OrganisationUnit UIToGraphQL(OrgUnitDto orgUnitDto) {
        return OrganisationUnit.builder()
                .code(orgUnitDto.getCode())
                .name(orgUnitDto.getLabel())
                .parentCode(orgUnitDto.getParentOrgUnit().getStringValue())
                .build();
    }
}
