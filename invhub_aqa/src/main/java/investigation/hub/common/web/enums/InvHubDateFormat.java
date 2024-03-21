package investigation.hub.common.web.enums;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InvHubDateFormat {
    DAY_MONTH_YEAR("dd/MM/yyyy", "DD/MM/YYYYY"),
    MONTH_DAY_YEAR("MM/dd/yyyy", "MM/DD/YYYYY"),
    YEAR_MONTH_DAY("yyyy/MM/dd", "YYYYY/MM/DD"),
    MONTH_DAY_YEAR_TIME("MM/dd/yyyy, HH:mm:ss", ""), //there is no UI value to choose by user for such format
    YEAR_MONTH_DAY_TIME("yyyy-MM-dd HH:mm:ss", ""), //there is no UI value to choose by user for such format
    ISO_DATE_TIME("yyyy-MM-dd'T'HH:mm:ss.SSSX", ""), //there is no UI value to choose by user for such format
    YEAR_MONTH_DAY_TIME_STAMP("yyyy-MM-dd'T'HH:mm:ss", ""); //there is no UI value to choose by user for such format
    private final String formatterPattern;
    private final String uiValue;

    public DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofPattern(formatterPattern).withLocale(Locale.US);
    }
}
