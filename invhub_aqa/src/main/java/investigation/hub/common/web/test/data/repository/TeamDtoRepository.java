package investigation.hub.common.web.test.data.repository;

import com.github.javafaker.Faker;
import investigation.hub.common.web.enums.InvHubDateFormat;
import investigation.hub.common.web.test.data.constants.AccessPolicy;
import investigation.hub.common.web.test.data.constants.Role;
import investigation.hub.common.web.test.data.dtos.TeamDto;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TeamDtoRepository {
    public static TeamDto createAdminAllAMLAccessTeamInstance() {
        Faker faker = new Faker();
        return TeamDto.builder()
                .name("Test " + faker.team().name())
                .description(faker.team().state())
                .role(Role.ADMIN)
                .accessPolicy(AccessPolicy.ALL_AML_ACCESS)
                .createdOn(new SimpleDateFormat(InvHubDateFormat.MONTH_DAY_YEAR.getFormatterPattern())
                        .format(new Date()))
                .build();
    }

    public static TeamDto createGuestEurAMLAccessTeamInstance() {
        Faker faker = new Faker();
        return TeamDto.builder()
                .name("Test " + faker.team().name())
                .description(faker.team().state())
                .role(Role.GUEST)
                .accessPolicy(AccessPolicy.EUR_AML_ACCESS)
                .createdOn(new SimpleDateFormat(InvHubDateFormat.MONTH_DAY_YEAR.getFormatterPattern())
                        .format(new Date()))
                .build();
    }
}
