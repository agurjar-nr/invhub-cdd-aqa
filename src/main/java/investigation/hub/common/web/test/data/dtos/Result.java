package investigation.hub.common.web.test.data.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder()
@ToString()
public class Result {
    private String searchEngine;
    private String financialCrimeRelevance;
    private String linkUrl;
    private String copilotSummary;
    private String button;
}
