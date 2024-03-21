package investigation.hub.common.web.test.data.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccessLevel {
    FULL("Full"),
    READ("Read"),
    NONE("");
    private final String accessLevel;

}