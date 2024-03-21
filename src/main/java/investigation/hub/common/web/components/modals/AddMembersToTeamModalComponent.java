package investigation.hub.common.web.components.modals;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.ModalComponent;
import investigation.hub.common.web.pages.TeamDetailsPage;
import investigation.hub.common.web.test.data.repository.UserDtoRepository;
import io.qameta.allure.Step;
import java.util.List;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ModalComponent
public class AddMembersToTeamModalComponent {
    private final SelenideElement container = $(".dn-modal");

    @Step("User selects users")
    public AddMembersToTeamModalComponent selectUsers(List<String> users) {
        users.forEach(user -> {
            container
                    .$x(".//div[contains(@class, 'container')]")
                    .as("Arrow to expand filter dropdown list")
                    .click();
            container
                    .$x(".//div[contains(@class, 'control')]//input")
                    .as("Textarea for input")
                    .sendKeys(user);
            if (UserDtoRepository.createTestUserInstance().getFullName().equals(user)) {
                user = "%s (Me)".formatted(user);
            }
            container
                    .$x(".//*[text()='%s']".formatted(user))
                    .as("Dropdown option by text: " + user)
                    .click();
        });
        log.info("Choose users from select menu");
        return this;
    }

    @Step("User clicks Add button")
    public TeamDetailsPage clickAddButton() {
        $x("//div[text()='Add']")
                .as("Add button")
                .click();
        log.info("Click 'Add' button");
        return new TeamDetailsPage();
    }
}
