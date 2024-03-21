package investigation.hub.common.web.test.data.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Weekday {
    SATURDAY("Saturday"),
    SUNDAY("Sunday"),
    MONDAY("Monday"),
    TUESDAY("Tuesday");
    public final String weekday;
}
