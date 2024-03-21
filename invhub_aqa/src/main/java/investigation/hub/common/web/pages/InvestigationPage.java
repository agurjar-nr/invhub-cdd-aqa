package investigation.hub.common.web.pages;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.Page;
import investigation.hub.common.web.components.ContentPageComponent;
import investigation.hub.common.web.components.MainHeaderComponent;
import investigation.hub.common.web.components.modals.AddNoteModalComponent;
import investigation.hub.common.web.components.modals.LeftFloatMenuInvestigationModalComponent;
import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;

import java.util.Map;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

@Getter
@Log4j2
@Page
public class InvestigationPage {

    private final SelenideElement header = $(".dn-summary-title");
    private final SelenideElement subjectIdLabel = $x("//h2/..//strong");
    private final SelenideElement infoBox = $("div.dn-summary-ribbon");
    private final LeftFloatMenuInvestigationModalComponent leftMenuComponent = new LeftFloatMenuInvestigationModalComponent();
    private final MainHeaderComponent mainHeaderComponent = new MainHeaderComponent();

    /*
     * Temporary solution for INVHUB-854 (page refresh if error alert appears)
     */
    public InvestigationPage() {
        int retry = 0;
        while (retry++ <= 3 && $("div[role='alert']")
                .as("Something went wrong alert")
                .isDisplayed()) {
            refresh();
        }
    }

    public boolean isInfoSectionVisible() {
        return !infoBox
                .getAttribute("class")
                .contains("hidden");
    }

    public String getInvestigationTitle() {
        return header
                .$("h2")
                .as("Investigation title")
                .getText();
    }

    public String getSubjectId() {
        String subjectId = subjectIdLabel.getText();
        log.info("Subject id is: " + subjectId);
        return subjectId;
    }

    public String getNumbersOfPersonalSubjectId() {
        return header
                .$("cite strong")
                .as("Personal Subject ID")
                .getText();
    }

    public String getSubjectRisk() {
        return header
                .$("cite:nth-child(4) div")
                .as("Subject Risk")
                .getText();
    }

    public Map<String, String> getInfoByHeader(InfoSectionType infoType) {
        String infoSectionTypeName = infoType.getStringValue();

        return infoBox
                .$x(".//div[@title='" + infoSectionTypeName + "']")
                .$$("li")
                .as("Rows with information taken by name: " + infoSectionTypeName)
                .asDynamicIterable()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.find(By.xpath(".//*")).getText(),
                        e -> e.find(By.xpath(".//*/following-sibling::*")).getText()));
    }

    @Step("User clicks Close Investigation button")
    public InvestigationPage clickCloseInvestigationButton() {
        $(byText("Close Investigation"))
                .as("Close Investigation button")
                .parent()
                .click();
        log.info("Click Close Investigation button");
        return this; //TODO: in case we are redirected to another page, change this
    }

    @Step("User clicks Add File button")
    public ContentPageComponent clickAddFileButton() {
        $("button[title='Add File']")
                .as("Add File button")
                .click();
        log.info("Click Add File button");
        return new ContentPageComponent();
    }

    @Step("User clicks Add Note button")
    public AddNoteModalComponent clickAddNoteButton() {
        $("button[title='Add Note']")
                .as("Add Note button")
                .click();
        log.info("Click Add Note button");
        return new AddNoteModalComponent();
    }

    public boolean isNewNoteAddedSuccessfullyMessageAppeared() {
        return $x("//p[contains(text(),'New note added successfully')]")
                .as("New note added successfully message")
                .isDisplayed();
    }

    public boolean isNewFileUploadingMessageAppeared(String fileName) {
        return $x("//p[contains(text(),'%s is uploading')]".formatted(fileName))
                .as("New file is uploading message")
                .isDisplayed();
    }

    public boolean isNewFileUploadedSuccessfullyMessageAppeared(String fileName) {
        return $x("//p[contains(text(),'%s uploaded successfully')]".formatted(fileName))
                .as("New file uploaded message")
                .isDisplayed();
    }

    @Step("User switches Subject Overview checkbox")
    public InvestigationPage switchSubjectOverviewCheckbox() {
        header
                .$(".toggle-switch")
                .as("Switch checkbox")
                .click();
        log.info("Switch Subject Overview checkbox");
        return this;
    }

    @Getter
    @AllArgsConstructor
    public enum InfoSectionType {

        COMPANY_PROFILE("Company Profile"),
        FINANCIAL_PROFILE("Financial Profile"),
        CONTACT_INFORMATION("Contact Information"),
        TAX_DETAILS("Tax Details"),
        GEOGRAPHICAL_PROFILE("Geographical Profile");

        private final String stringValue;
    }

}
