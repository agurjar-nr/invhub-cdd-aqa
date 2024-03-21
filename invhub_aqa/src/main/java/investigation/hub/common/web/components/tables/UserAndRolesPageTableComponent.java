package investigation.hub.common.web.components.tables;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.PageComponent;
import investigation.hub.common.web.pages.UserProfilePage;
import io.qameta.allure.Step;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@PageComponent
public class UserAndRolesPageTableComponent extends TableGeneralComponent<UserAndRolesPageTableComponent.HeaderName> {

    public List<String> getUserColumnData() {
        return waitForTableContent().getColumnData(HeaderName.USER.stringValue);
    }

    @Step("User clicks on row")
    public UserProfilePage clickOnRowByUserHeader(String fullName) {
        waitForTableContent();
        super.getRowByHeaderAndValue(HeaderName.USER.getStringValue(), fullName).click();
        log.info("Click on user row by full name: " + fullName);
        return new UserProfilePage();
    }

    @Step("User sorts Users and Roles table by column")
    public void sortTable(HeaderName headerName, SortStatus sortStatus) {
        SelenideElement header = getHeaderElementByName(headerName.getStringValue());
        sortTableWithDataTestid(header, sortStatus.getDataTestId());
    }

    @Getter
    @AllArgsConstructor
    public enum HeaderName implements GeneralTableHeaderName {
        USER("User"),
        ROLES("Roles"),
        LAST_ACTIVITY("Last Activity");

        public final String stringValue;
    }
}
