package investigation.hub.common.core.services.api.model.provider;

import com.github.javafaker.Faker;
import groovy.util.logging.Log4j2;
import investigation.hub.common.core.services.api.model.user.UserGraphQL;
import investigation.hub.common.web.test.data.constants.Role;
import investigation.hub.common.web.test.data.constants.TimeZone;
import investigation.hub.common.web.test.data.constants.Weekday;


@Log4j2
public class UserGraphQLProvider implements Provider<UserGraphQL> {
    private final UserGraphQL user;

    public UserGraphQLProvider(Role role) {
        this();
        user.setRole(role.getId());
        user.setPermissions(new PermissionsProvider(role).provide());
    }

    public UserGraphQLProvider() {
        String prefix = "AQA_API_Test_USER";
        Faker faker = new Faker();
        String uuid = faker.internet().uuid();
        String domain = "fseng.net";
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = "%s_%s_%s@%s".formatted(firstName, lastName, uuid, domain).toLowerCase().replaceAll("'", "");

        user = UserGraphQL.builder()
                .createdOn(new DateTimeProvider(faker, 100).provide())
                .updatedOn(new DateTimeProvider(faker, 10).provide())
                .email(email)
                .username("%s_%s".formatted(firstName, lastName))
                .enabled(true)
                .firstName("%s_first_%s_%s".formatted(prefix, firstName, uuid))
                .lastName("%s_last_%s_%s_%s".formatted(prefix, lastName, faker.funnyName().name(), uuid))
                .language("en")
                .weekStartsOn(Weekday.MONDAY.getWeekday())
                .build();
    }

    public UserGraphQLProvider(Role role, Weekday weekday, TimeZone timeZone) {
        this(role);
        user.setWeekStartsOn(weekday.getWeekday());
        user.setTimeZone(timeZone.getTimeZone());
        user.setTimeZoneName(timeZone.getTimeZoneName());
    }

    @Override
    public UserGraphQL provide() {
        return user;
    }
}