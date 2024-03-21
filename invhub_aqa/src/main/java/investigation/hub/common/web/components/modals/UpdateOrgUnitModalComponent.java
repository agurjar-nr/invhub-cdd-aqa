package investigation.hub.common.web.components.modals;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.ModalComponent;
import investigation.hub.common.web.components.OrganisationUnitsPageComponent;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Keys;

@Log4j2
@ModalComponent
public class UpdateOrgUnitModalComponent {
    private final SelenideElement container = $(".dn-modal");

    @Step("User updates Org Unit Name")
    public UpdateOrgUnitModalComponent updateCode(String code) {
        container
                .$("#code")
                .as("Org Unit code field")
                .sendKeys(Keys.chord(Keys.CONTROL, "a"), code);
        log.info("Update Org Unit code: " + code);
        return this;
    }

    @Step("User updates Org Unit label")
    public UpdateOrgUnitModalComponent updateLabel(String label) {
        container
                .$("#label")
                .as("Org Unit label field")
                .sendKeys(Keys.chord(Keys.CONTROL, "a"), label);
        log.info("Update Org Unit label: " + label);
        return this;
    }

    @Step("User clicks Update button")
    public OrganisationUnitsPageComponent clickUpdateButton() {
        container.$x(".//div[text()='Update']")
                .as("Update button")
                .click();
        log.info("Click 'Update' button");
        return new OrganisationUnitsPageComponent();
    }
}
