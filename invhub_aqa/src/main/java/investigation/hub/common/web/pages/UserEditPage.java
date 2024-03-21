package investigation.hub.common.web.pages;

import static com.codeborne.selenide.Selenide.$x;

import com.smile.components.Page;
import investigation.hub.common.web.components.CreateUserPageComponent;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Page
@Getter
public class UserEditPage {
    private final CreateUserPageComponent personalDetails = new CreateUserPageComponent();

    @Step("User clicks Save button")
    public UserProfilePage clickSaveButton() {
        $x("//div[contains(text(),'Save')]")
                .as("Save button")
                .click();
        log.info("Click 'Save' button");
        return new UserProfilePage();
    }
}
