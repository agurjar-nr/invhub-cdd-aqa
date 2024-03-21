package investigation.hub.common.web.components.modals;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.ModalComponent;
import investigation.hub.common.web.pages.UsersPage;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

import static com.codeborne.selenide.Selenide.$;

/**
 * Choose Columns modal component is a component on Users and Roles page
 * on the right side above the table to hide and move columns
 */
@Log4j2
@ModalComponent
public class ChooseColumnsModalComponent {

    private final SelenideElement container = $(".flex-col.divide-y");

    @Step("User clicks Apply button")
    public UsersPage clickApplyButton() {
        container
                .$x(".//div[contains(text(),'Apply')]")
                .as("Apply button")
                .click();
        log.info("Click 'Apply' button");
        return new UsersPage();
    }

    @Step("User searches column value")
    public ChooseColumnsModalComponent searchColumnValue(String columnName) {
        container
                .$x(".//input[@placeholder='Search']")
                .as("Search field")
                .sendKeys(columnName);
        log.info("Search user by: " + columnName);
        return this;
    }

    @Step("User click column checkbox")
    public ChooseColumnsModalComponent clickColumnCheckbox(String columnName) {
        container
                .$x(".//div[@role='button' and contains(.//text(),'%s')]"
                .formatted(
                columnName))
                .as("Column checkbox")
                .click();
        log.info("Click 'Apply' button");
        return this;
    }
}
