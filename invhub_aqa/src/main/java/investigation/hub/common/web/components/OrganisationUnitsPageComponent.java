package investigation.hub.common.web.components;

import static com.codeborne.selenide.Selenide.$x;

import com.smile.components.PageComponent;
import investigation.hub.common.web.components.tables.UnitsTablePageComponent;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@PageComponent
@Getter
public class OrganisationUnitsPageComponent {
    UnitsTablePageComponent unitsTablePageComponent = new UnitsTablePageComponent();

    public boolean isHeaderSectionVisible() {
        return $x("//div/span[contains(text(), 'Organisation Units')]")
                .as("Organisation Units page header")
                .isDisplayed();
    }
}
