package investigation.hub.common.web.test.data.repository;

import com.github.javafaker.Country;
import com.github.javafaker.Faker;
import investigation.hub.common.web.components.tables.UnitsTablePageComponent;
import investigation.hub.common.web.enums.InvHubDateFormat;
import investigation.hub.common.web.test.data.dtos.OrgUnitDto;
import java.text.SimpleDateFormat;
import java.util.Date;


public class OrgUnitRepository {

    public static OrgUnitDto createOrgUnitInstance() {
        Country country = new Faker().country();
        UnitsTablePageComponent.Codes code = UnitsTablePageComponent.Codes.EUR;
        return OrgUnitDto.builder()
                //temporary solution till there is not possibility to API delete org units
                .code("ORG_UNIT_TO_UPDATE_" + country.countryCode2())
                .label(country.name())
                .parentOrgUnit(code)
                .childOrgUnit(code)
                .updatedOn(new SimpleDateFormat(InvHubDateFormat.MONTH_DAY_YEAR.getFormatterPattern()).format(new Date()))
                .build();
    }
}
