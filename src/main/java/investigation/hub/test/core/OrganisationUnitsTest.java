package investigation.hub.test.core;

import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.core.util.Retry;
import investigation.hub.common.web.components.OrganisationUnitsPageComponent;
import investigation.hub.common.web.components.tables.UnitsTablePageComponent;
import investigation.hub.common.web.pages.AllOpenInvestigationsPage;
import investigation.hub.common.web.pages.LogHistoryPage;
import investigation.hub.common.web.test.data.dtos.OrgUnitDto;
import investigation.hub.common.web.test.data.repository.LogRepository;
import investigation.hub.common.web.test.data.repository.OrgUnitRepository;
import investigation.hub.common.web.test.data.repository.UserDtoRepository;
import io.qameta.allure.TmsLink;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Map;

import static com.codeborne.selenide.Selenide.open;

public class OrganisationUnitsTest extends InvHubUiTest {
    SoftAssert softAssert = new SoftAssert();
    AllOpenInvestigationsPage openInvestigationsPage = new AllOpenInvestigationsPage();
    OrganisationUnitsPageComponent organisationUnitsPage = new OrganisationUnitsPageComponent();
    UnitsTablePageComponent unitsTablePageComponent = organisationUnitsPage
            .getUnitsTablePageComponent();

    @BeforeClass
    public void openPage() {
        open(apiProperties.getUrl() + apiProperties.getOpenInvestigations());
        openInvestigationsPage.waitForLoad();
    }

    @TmsLink("INVHUB-2794")
    @Test(retryAnalyzer = Retry.class,
            description = "Verify user can view a list of all Org Units that align to company standards.")
    public void checkUserCanViewOrganisationUnitsTest() {
        softAssert = new SoftAssert();
        organisationUnitsPage = openInvestigationsPage
                .getMainHeaderComponent()
                .openDataAccess()
                .clickOrganisationUnitsTab();
        softAssert.assertTrue(organisationUnitsPage.isHeaderSectionVisible(),
                "Organisation Units page header should be displayed");
        softAssert.assertTrue(unitsTablePageComponent.getCodesColumnData()
                        .containsAll(UnitsTablePageComponent.Codes.getAllCodesValue()),
                "User should have ability to view Org Units that align to company standards");
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-3039")
    @Test(retryAnalyzer = Retry.class, description = "Verify user can edit Org Unit")
    public void checkUserCanEditOrganisationUnitTest() {
        softAssert = new SoftAssert();
        OrgUnitDto createdOrgUnit = OrgUnitRepository.createOrgUnitInstance();
        /*
        Do not create through API till there is no API call to delete
        new OrganisationUnitClient().createOrganisationUnit(OrganisationUnitConverter.UIToGraphQL(createdOrgUnit));
         */
        OrgUnitDto updatedOrgUnit = OrgUnitRepository.createOrgUnitInstance();
        organisationUnitsPage = openInvestigationsPage
                .getMainHeaderComponent()
                .openDataAccess()
                .clickOrganisationUnitsTab();

        createdOrgUnit.setCode(unitsTablePageComponent
                .getOrgUnitMatchesValue("^ORG_UNIT_TO_UPDATE.*$"));
        createdOrgUnit.setLabel(unitsTablePageComponent.getRowContentByOrgUnitCode(createdOrgUnit.getCode())
                .get(UnitsTablePageComponent.HeaderName.LABEL.getStringValue()));

        unitsTablePageComponent
                .clickRowByOrgUnitCode(createdOrgUnit.getCode())
                .updateCode(updatedOrgUnit.getCode())
                .updateLabel(updatedOrgUnit.getLabel())
                .clickUpdateButton();
        softAssert.assertNotNull(unitsTablePageComponent.getRowByContent(updatedOrgUnit.getStringMap()),
                "Org Unit with updated values is present in the table");
        checkLog(LogRepository.getUpdateOrganisationUnitLog(createdOrgUnit, updatedOrgUnit));
        softAssert.assertAll();
    }

    private void checkLog(Map<String, String> logToFind) {
        String userFullName = UserDtoRepository.createTestUserInstance().getFullName();
        LogHistoryPage logHistoryPage = openInvestigationsPage.getMainHeaderComponent()
                .openUsers()
                .searchUser(UserDtoRepository.createTestUserInstance().getFirstName())//known bug
                .openUserProfile(userFullName)
                .clickLogHistoryButton();
        Map<String, String> recentAuditLogData = logHistoryPage.getLogHistoryTable().getRecentAuditLogData();
        softAssert.assertTrue(recentAuditLogData.entrySet().containsAll(logToFind.entrySet()),
                "Log: %s should be present".formatted(logToFind.entrySet()));
    }
}
