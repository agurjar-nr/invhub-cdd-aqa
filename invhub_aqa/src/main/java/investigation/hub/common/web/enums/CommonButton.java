package investigation.hub.common.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommonButton {
    SEARCH("Search"),
    SAVE("Save"),
    REMOVE("Remove"),
    CANCEL("Cancel"),
    UPDATE("Update");

    private final String title;
}
