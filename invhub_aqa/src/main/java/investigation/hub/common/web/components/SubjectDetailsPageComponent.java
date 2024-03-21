package investigation.hub.common.web.components;

import static com.codeborne.selenide.Selenide.$$x;

import com.smile.components.PageComponent;
import investigation.hub.common.web.components.tables.AccountsTablePageComponent;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;

@Getter
@Log4j2
@PageComponent
public class SubjectDetailsPageComponent extends PageGeneralComponent {

    AccountsTablePageComponent accountsTable = new AccountsTablePageComponent();

    public Map<String, String> getInfoByHeader(SubjectDetailsSections detailsSections) {
        String sectionsId = detailsSections.getId();

        return $$x(".//div[@id='" + sectionsId + "']/parent::div//li")
                .as("Rows with information taken by name: " + detailsSections.getStringValue())
                .asDynamicIterable()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.find(By.xpath(".//div")).getText(),
                        e -> e.find(By.xpath(".//div/following-sibling::*")).getText()));
    }

    @Getter
    @AllArgsConstructor
    public enum SubjectDetailsSections {

        COMPANY_PROFILE("Company Profile", "company-profile-panel-header"),
        FINANCIAL_PROFILE("Financial Profile", "financial-profile-panel-header"),
        CONTACT_INFORMATION("Contact Information", "contact-information-panel-header"),
        RELATIONSHIP("Relationship", "relationship-panel-header"),
        TAX_DETAILS("Tax Details", "tax-details-panel-header"),
        GEOGRAPHICAL_PROFILE("Geographical Profile", "geographical-profile-panel-header");

        private final String stringValue;
        private final String id;
    }
}
