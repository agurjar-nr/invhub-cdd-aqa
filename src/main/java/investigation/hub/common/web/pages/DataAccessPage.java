package investigation.hub.common.web.pages;

import static com.codeborne.selenide.Selenide.$x;

import com.smile.components.Page;
import investigation.hub.common.web.components.AccessPoliciesPageComponent;
import investigation.hub.common.web.components.OrganisationUnitsPageComponent;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
@Page
public class DataAccessPage {

    private final OrganisationUnitsPageComponent organisationUnitsPageComponent = new OrganisationUnitsPageComponent();
    private final AccessPoliciesPageComponent accessPoliciesPageComponent = new AccessPoliciesPageComponent();

    @Step("User clicks Organisation Units Tab")
    public OrganisationUnitsPageComponent clickOrganisationUnitsTab() {
        $x("//p[text()='Organisation Units']")
                .as("Organisation Units Tab")
                .click();
        log.info("Click Organisation Units Tab");
        return new OrganisationUnitsPageComponent();
    }
}
