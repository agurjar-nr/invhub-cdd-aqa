package investigation.hub.common.web.pages;

import static com.codeborne.selenide.Selenide.$x;

import com.smile.components.Page;
import investigation.hub.common.web.components.tables.OrganisationUnitsTablePageComponent;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
@Page
public class OrganisationUnitsPage {

    OrganisationUnitsTablePageComponent organisationUnitsTablePageComponent = new OrganisationUnitsTablePageComponent();

    public boolean isHeaderSectionVisible() {
        return $x("//div/span[contains(text(), 'Organisation Units')]")
                .as("Organisation Units page header")
                .isDisplayed();
    }
}
