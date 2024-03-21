package investigation.hub.common.web.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.smile.components.Page;
import investigation.hub.common.web.components.MainHeaderComponent;
import investigation.hub.common.web.components.PaginationComponent;
import investigation.hub.common.web.components.modals.AssignInvestigationModalComponent;
import investigation.hub.common.web.components.tables.InvestigationsTablePageComponent;
import investigation.hub.common.web.pages.base.BasePage;
import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static investigation.hub.common.web.pages.AllOpenInvestigationsPage.InvestigationWorkListTabs.ALL_WORK;
import static investigation.hub.common.web.pages.AllOpenInvestigationsPage.InvestigationWorkListTabs.MY_WORK;

@Getter
@Log4j2
@Page
public class

AllOpenInvestigationsPage extends BasePage {

    private final InvestigationsTablePageComponent investigationsTable = new InvestigationsTablePageComponent();

    private final MainHeaderComponent mainHeaderComponent = new MainHeaderComponent();

    private final PaginationComponent paginationComponent = new PaginationComponent();

    public AllOpenInvestigationsPage() {
        waitForLoad();
    }

    public void waitForLoad() {
        $("#current-screen-subjectsUnderReview")
                .as("Main page window container")
                .shouldBe(Condition.visible);
        log.info("Main page window container should be visible");
    }


    @Step("User clicks `My work` button")
    public AllOpenInvestigationsPage clickMyWorkButton() {
        return selectInvestigationTab(MY_WORK);
    }

    @Step("User clicks `All work` button")
    public AllOpenInvestigationsPage clickAllWorkButton() {
        return selectInvestigationTab(ALL_WORK);
    }

    public AllOpenInvestigationsPage selectInvestigationTab(InvestigationWorkListTabs tab) {
        SelenideElement button = $x("//p[text()='%s']/ancestor::button".formatted(tab.getStringValue()));
        if ("false".equals(button.getAttribute("aria-selected"))) {
            button
                    .as("tab button")
                    .click();
        }
        log.info("Click `%s` button".formatted(tab.getStringValue()));
        return this;
    }

    @Step("User clicks Assign button")
    public AssignInvestigationModalComponent clickAssignButton() {
        $x("//span[.//text()='Assign']/parent::button")
                .as("Assign button")
                .scrollIntoView(false)
                .click();
        log.info("Click Assign button");
        return new AssignInvestigationModalComponent();
    }

    public boolean isUserAssignedToOpenInvestigationMessageAppeared() {
        return $x("//p[contains(text(),'User assigned to open investigation.')]")
                .as("'User assigned to open investigation.' message")
                .isDisplayed();
    }

    public boolean isAllWorkButtonShown() {
        return $x("//p[text()='%s']/ancestor::button".formatted(ALL_WORK.getStringValue())).isDisplayed();
    }

    @Getter
    @AllArgsConstructor
    public enum InvestigationWorkListTabs {
        ALL_WORK("All work"),
        MY_WORK("My work");

        private final String stringValue;
    }
}
