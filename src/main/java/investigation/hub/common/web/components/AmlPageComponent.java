package investigation.hub.common.web.components;

import static com.codeborne.selenide.Selenide.$x;

import com.smile.components.PageComponent;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@PageComponent
@Getter
public class AmlPageComponent extends TransactionCommonComponent {

    private final OpenDetectionsPageComponent openDetectionsPageComponent = new OpenDetectionsPageComponent();
    private final HistoricalDetectionsPageComponent historicalDetectionsPageComponent =
            new HistoricalDetectionsPageComponent();
    private final WorkSummaryPageComponent workSummaryPageComponent = new WorkSummaryPageComponent();

    @Step("User clicks Open Detections button")
    public OpenDetectionsPageComponent clickOpenDetectionsButton() {
        $x("//p[contains(text(),'Open Detections')]")
                .as("Open Detections button")
                .click();
        log.info("Click Open Detections button");
        return new OpenDetectionsPageComponent();
    }

    @Step("User clicks Historical Detections button")
    public HistoricalDetectionsPageComponent clickHistoricalDetectionsButton() {
        $x("//p[contains(text(),'Historical Detections')]")
                .as("Historical Detections button")
                .scrollIntoView(false)
                .click();
        log.info("Click Historical Detections button");
        return new HistoricalDetectionsPageComponent();
    }

    @Step("User clicks Work Summary button")
    public WorkSummaryPageComponent clickWorkSummaryButton() {
        $x("//p[contains(text(),'Work Summary')]")
                .as("Work Summary button")
                .click();
        log.info("Click Work Summary button");
        return new WorkSummaryPageComponent();
    }

    public String getAIInvestigationSummary() {
        return $x(".//h3[contains(text(), 'AI Investigation Summary')]/ancestor::div//span[@class='pt-2']")
                .as("AI Investigation Summary")
                .getText();
    }
}
