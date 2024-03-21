package investigation.hub.common.web.pages;

import static com.codeborne.selenide.Selenide.$x;
import static investigation.hub.common.web.enums.CommonButton.UPDATE;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.Page;
import investigation.hub.common.web.components.OtherPreferencesPageComponent;
import investigation.hub.common.web.components.modals.LeftFloatMenuConfigurationModalComponent;
import investigation.hub.common.web.enums.CommonButton;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
@Page
public class SystemConfigurationPage {

    private final LeftFloatMenuConfigurationModalComponent leftMenuComponent = new LeftFloatMenuConfigurationModalComponent();
    private final OtherPreferencesPageComponent otherPreferencesPageComponent = new OtherPreferencesPageComponent();

    public boolean isButtonUpdateEnabled() {
       return getButton(UPDATE).isEnabled();
    }

    private SelenideElement getButton(CommonButton buttonName) {
        return  $x("//div[contains(text(),'%s')]/ancestor::button".formatted(buttonName.getTitle()))
                .as("Update button");
    }

}

