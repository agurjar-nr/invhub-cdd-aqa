package investigation.hub.common.web.test.data.dtos;

import investigation.hub.common.web.test.data.constants.TimeZone;
import investigation.hub.common.web.test.data.constants.Weekday;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class UserDto {
    private String firstName;
    private String lastName;
    private String fullName;
    private String photo;
    private String email;
    private String language;
    private Weekday weekStartsOn;
    private TimeZone timeZone;

    public void setFullName() {
        this.fullName = "%s %s".formatted(getFirstName(), getLastName());
    }

}
