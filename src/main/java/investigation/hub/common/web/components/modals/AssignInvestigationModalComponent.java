package investigation.hub.common.web.components.modals;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.ModalComponent;
import investigation.hub.common.web.pages.InvestigationPage;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ModalComponent
public class AssignInvestigationModalComponent {

    private final SelenideElement container = $(".dn-modal");

    @Step("User clicks Save button")
    public InvestigationPage clickSaveButton() {
        container
                .$x(".//button[.//text()='Save']")
                .as("Save button")
                .click();
        log.info("Click Save button");
        return new InvestigationPage();
    }

    @Step("User assigns investigation to user")
    public AssignInvestigationModalComponent assignInvestigationToUser(String user) {
        container
                .$x(".//label[@for ='userToAssignId']/parent::div//div")
                .as("Arrow to expand filter dropdown list")
                .click();
        container
                .$x(".//label[@for ='userToAssignId']/parent::div//div[contains(text(),'" + user + "')]")
                .as("Filter dropdown option by text: " + user)
                .click();
        log.info("Filter column data by value");
        return this;
    }
}

