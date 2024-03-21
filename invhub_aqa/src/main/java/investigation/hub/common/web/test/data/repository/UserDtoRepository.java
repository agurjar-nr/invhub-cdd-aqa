package investigation.hub.common.web.test.data.repository;

import com.github.javafaker.Faker;
import investigation.hub.common.core.properties.ConfigProperties;
import investigation.hub.common.core.properties.loaders.ConfigLoader;
import investigation.hub.common.web.test.data.constants.TimeZone;
import investigation.hub.common.web.test.data.constants.Weekday;
import investigation.hub.common.web.test.data.dtos.UserDto;

public class UserDtoRepository {
    protected static final ConfigProperties apiProperties = ConfigLoader.getConfigProperties();

    public static UserDto createAdminUserInstance() {
        Faker faker = new Faker();
        UserDto user = UserDto.builder()
                .firstName(faker.name().firstName())
                .lastName("%s-%s-Test".formatted(faker.name().lastName(), faker.funnyName().name()))
                .photo("src/main/resources/gravatar.png")
                .email(faker.internet().emailAddress())
                .language("English")
                .weekStartsOn(Weekday.MONDAY)
                .timeZone(TimeZone.ALASKA)
                .build();
        user.setFullName();
        return user;
    }

    public static UserDto createTestUserInstance() {
        UserDto user = UserDto.builder()
                //TO_DO pick from userClient data when is ready
                .firstName("AutoTest_User_First")
                .lastName("AutoTest_User_Last")
                .email(apiProperties.getAutoTestUser())
                .build();
        user.setFullName();
        return user;
    }

    public static UserDto createGuestUserInstance() {
        Faker faker = new Faker();
        UserDto user = UserDto.builder()
                .firstName(faker.name().firstName())
                .lastName("%s-%s-Test".formatted(faker.name().lastName(), faker.funnyName().name()))
                .photo("src/main/resources/gravatar.png")
                .email(faker.internet().emailAddress())
                .language("English")
                .weekStartsOn(Weekday.MONDAY)
                .timeZone(TimeZone.ALASKA)
                .build();
        user.setFullName();
        return user;
    }
}
