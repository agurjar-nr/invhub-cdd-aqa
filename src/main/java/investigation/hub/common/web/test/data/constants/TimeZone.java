package investigation.hub.common.web.test.data.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeZone {
    ALASKA("(GMT-09:00) Alaska", "US/Alaska"),
    DUBLIN("(GMT+00:00) Greenwich Mean Time : Dublin, Edinburgh, Lisbon, London", "Europe/Dublin"),
    HELSINKI("(GMT+02:00) Helsinki, Kyiv, Riga, Sofia, Tallinn, Vilnius", "Europe/Helsinki");
    private final String timeZone;
    private final String timeZoneName;
}
