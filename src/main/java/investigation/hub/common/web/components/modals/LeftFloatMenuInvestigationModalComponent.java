package investigation.hub.common.web.components.modals;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.ModalComponent;
import investigation.hub.common.web.components.AmlPageComponent;
import investigation.hub.common.web.components.ContentPageComponent;
import investigation.hub.common.web.components.EventLogPageComponent;
import investigation.hub.common.web.components.MachineLearningPageComponent;
import investigation.hub.common.web.components.NarrativePageComponent;
import investigation.hub.common.web.components.PageGeneralComponent;
import investigation.hub.common.web.components.SubjectDetailsPageComponent;
import investigation.hub.common.web.components.TransactionsPageComponent;
import investigation.hub.common.web.components.WebSearchPageComponent;
import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ModalComponent
public class LeftFloatMenuInvestigationModalComponent {

    private final SelenideElement container = $("div.dn-section-navigator");

    @Step
    public <T extends PageGeneralComponent> T clickMenuItemByName(LeftMenuItemName menuName) {
        container
                .$("button[title$='" + menuName.getStringValue() + "']")
                .as("Button of left side menu with title " + menuName)
                .click();
        //move cursor from menu to close it
        $("a[href='/investigation/open-investigations']")
                .as("Open-investigation breadcrumb")
                .hover();

        log.info("Click left side menu item by its name: " + menuName);

        Class<? extends PageGeneralComponent> componentClass = getComponentClass(menuName);

        try {
            return (T) componentClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Component for menu " + menuName.getStringValue() + " couldn't be instantiated");
        }
    }

    private Class<? extends PageGeneralComponent> getComponentClass(LeftMenuItemName menuName) {
        return switch (menuName) {
            case DETECTIONS -> AmlPageComponent.class;
            case SUBJECT_DETAILS -> SubjectDetailsPageComponent.class;
            case TRANSACTIONS -> TransactionsPageComponent.class;
            case MACHINE_LEARNING -> MachineLearningPageComponent.class;
            case WEB_SEARCH -> WebSearchPageComponent.class;
            case CONTENT -> ContentPageComponent.class;
            case NARRATIVE -> NarrativePageComponent.class;
            case EVENT_LOG -> EventLogPageComponent.class;
        };
    }

    @Getter
    @AllArgsConstructor
    public enum LeftMenuItemName {

        DETECTIONS("Detections"),
        SUBJECT_DETAILS("Subject Details"),
        TRANSACTIONS("Transactions"),
        MACHINE_LEARNING("Machine Learning"),
        WEB_SEARCH("Web Search"),
        CONTENT("Content"),
        NARRATIVE("Narrative"),
        EVENT_LOG("Event Log");

        private final String stringValue;
    }
}