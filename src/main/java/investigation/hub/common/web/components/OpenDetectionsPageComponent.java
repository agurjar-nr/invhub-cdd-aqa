package investigation.hub.common.web.components;

import com.smile.components.PageComponent;
import investigation.hub.common.web.components.modals.GeneratedByAiModalComponent;
import investigation.hub.common.web.components.tables.OpenDetectionsTablePageComponent;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

@Log4j2
@PageComponent
@Getter
public class OpenDetectionsPageComponent {

    private final OpenDetectionsTablePageComponent openDetectionsTablePageComponent = new OpenDetectionsTablePageComponent();

    @Step("User clicks Unusual button")
    public OpenDetectionsPageComponent clickUnusualButton() {
        $("button[aria-label='Unusual']")
                .as("Unusual button")
                .click();
        log.info("Click Unusual button");
        return this;
    }

    @Step("User clicks Not Unusual button")
    public OpenDetectionsPageComponent clickNotUnusualButton() {
        $("button[aria-label='Not Unusual']")
                .as("Not Unusual button")
                .click();
        log.info("Click Not Unusual button");
        return this;
    }

    @Step("User clicks 'Generated by AI Disclaimer' link")
    public GeneratedByAiModalComponent clickGeneratedByAiDisclaimerLink() {
        $("[title='Generated by AI Disclaimer']")
                .as("'Generated by AI Disclaimer' link")
                .click();
        log.info("Click 'Generated by AI Disclaimer' link");
        return new GeneratedByAiModalComponent();
    }

    public boolean isSelectedDetectionMarkedAsUnusualMessageAppeared() {
        return $x("//p[contains(text(),'Selected Detections marked as Unusual')]")
                .as("Selected Detections marked as Unusual message")
                .isDisplayed();
    }

    public boolean isSelectedDetectionMarkedAsNotUnusualMessageAppeared() {
        return $x("//p[contains(text(),'Selected Detections marked as Not Unusual')]")
                .as("Selected Detections marked as Not Unusual message")
                .isDisplayed();
    }
}