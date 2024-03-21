package investigation.hub.common.web.test.data.dtos;


import static investigation.hub.common.web.components.WebSearchPageComponent.Values.SEARCH_ENGINE;

import investigation.hub.common.web.enums.CommonButton;
import investigation.hub.common.web.components.WebSearchPageComponent;
import investigation.hub.common.core.services.api.model.provider.Provider;


public class ResultProvider implements Provider<Result> {

    private final Result result;

    public ResultProvider(WebSearchPageComponent.Values financialCrimeRelevance, boolean isSaved) {
        this.result = Result.builder()
                .searchEngine(SEARCH_ENGINE.getStringValue())
                .financialCrimeRelevance(financialCrimeRelevance.getStringValue())
                .copilotSummary(financialCrimeRelevance.getStringValue())
                .button(isSaved ? CommonButton.REMOVE.getTitle() : CommonButton.SAVE.getTitle())
                .build();
    }

    public ResultProvider(String searchEngine, String financialCrimeRelevance, String linkUrl, String copilotSummary,
                          String buttonName) {
        this.result = Result.builder()
                .searchEngine(searchEngine)
                .financialCrimeRelevance(financialCrimeRelevance)
                .linkUrl(linkUrl)
                .copilotSummary(copilotSummary)
                .button(buttonName)
                .build();
    }

    @Override
    public Result provide() {
        return result;
    }
}
