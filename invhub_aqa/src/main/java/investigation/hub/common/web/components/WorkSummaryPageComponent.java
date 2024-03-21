package investigation.hub.common.web.components;

import static com.codeborne.selenide.Selenide.$x;

import com.smile.components.PageComponent;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@PageComponent
public class WorkSummaryPageComponent extends PageGeneralComponent {

    public Map<AMLDetectionsGroups, Integer> getAMLDetectionsGroupsCounts() {
        return Arrays.stream(AMLDetectionsGroups.values())
                .collect(Collectors.toMap(
                        name -> name,
                        name -> Integer.parseInt($x(".//span[contains(text(),'%s')]/parent::div//div"
                                .formatted(name.getStringValue()))
                                .as("AML Detection Group %s count".formatted(name)).getText())));
    }

    @Getter
    @AllArgsConstructor
    public enum AMLDetectionsGroups {
        OPEN("Open"),
        CLOSED("Closed"),
        NOT_UNUSUAL("*  Not Unusual"),
        UNUSUAL("*  Unusual"),
        SKIPPED("Skipped");

        public final String stringValue;
    }
}
