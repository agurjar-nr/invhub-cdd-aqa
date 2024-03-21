package investigation.hub.test.core;

import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.core.util.Retry;
import investigation.hub.common.web.components.SubjectDetailsPageComponent;
import investigation.hub.common.web.components.modals.LeftFloatMenuInvestigationModalComponent;
import investigation.hub.common.web.components.tables.InvestigationsTablePageComponent;
import investigation.hub.common.web.pages.InvestigationPage;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Map;

import static com.codeborne.selenide.Selenide.open;

public class SubjectDetailsTest extends InvHubUiTest {

    InvestigationsTablePageComponent investigationsTablePageComponent;
    InvestigationPage investigationPage;
    SubjectDetailsPageComponent subjectDetailsPageComponent;

    @BeforeClass
    public void openPage() {
        open(apiProperties.getUrl() + apiProperties.getOpenInvestigations());
        openInvestigationsPage.waitForLoad();
        investigationsTablePageComponent = openInvestigationsPage
                .clickAllWorkButton()
                .getInvestigationsTable();
        String subjectId = "REF-CUSTEST-LIST-78-0107";
        investigationPage = investigationsTablePageComponent.clickRowBySubjectId(subjectId);
        subjectDetailsPageComponent = investigationPage.getLeftMenuComponent()
                .clickMenuItemByName(LeftFloatMenuInvestigationModalComponent.LeftMenuItemName.SUBJECT_DETAILS);
    }

    @TmsLink("INVHUB-1814")
    @Test(retryAnalyzer = Retry.class, description = "Verify Company Profile data is correct and not blank")
    public void checkCompanyProfileDataCorrectAndNotBlankTest() {
        SoftAssert softAssert = new SoftAssert();
        Map<String, String> companyProfileData = subjectDetailsPageComponent.getInfoByHeader(SubjectDetailsPageComponent
                .SubjectDetailsSections.COMPANY_PROFILE);

        softAssert.assertTrue(companyProfileData.equals(investigationPage.getInfoByHeader(
                InvestigationPage.InfoSectionType.COMPANY_PROFILE)), "Company profile data should be the same as in summary");
        softAssert.assertTrue(companyProfileData.entrySet()
                .stream()
                .noneMatch(it -> it.getValue().isBlank()), "Company profile data should be not blank");
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1815")
    @Test(retryAnalyzer = Retry.class, description = "Verify Financial Profile data is correct and not blank")
    public void checkFinancialProfileDataCorrectAndNotBlankTest() {
        SoftAssert softAssert = new SoftAssert();
        Map<String, String> financialProfileData = subjectDetailsPageComponent.getInfoByHeader(SubjectDetailsPageComponent
                .SubjectDetailsSections.FINANCIAL_PROFILE);
        softAssert.assertTrue(financialProfileData.equals(investigationPage.getInfoByHeader(
                InvestigationPage.InfoSectionType.FINANCIAL_PROFILE)), "Financial profile data should be the same as in summary");
        softAssert.assertTrue(financialProfileData.entrySet()
                .stream()
                .noneMatch(it -> it.getValue().isBlank()), "Financial profile data should be not blank");
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1816")
    @Test(retryAnalyzer = Retry.class, description = "Verify Contact Information data is correct and not blank")
    public void checkContactInformationDataCorrectAndNotBlankTest() {
        SoftAssert softAssert = new SoftAssert();
        Map<String, String> contactInformationData = subjectDetailsPageComponent
                .getInfoByHeader(SubjectDetailsPageComponent.SubjectDetailsSections.CONTACT_INFORMATION);

        softAssert.assertTrue(contactInformationData.equals(investigationPage.getInfoByHeader(
                        InvestigationPage.InfoSectionType.CONTACT_INFORMATION)),
                "Contact Information data should be the same as in summary");
        softAssert.assertTrue(contactInformationData.entrySet()
                .stream()
                .noneMatch(it -> it.getValue().isBlank()), "Contact Information data should be not blank");
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1817")
    @Test(retryAnalyzer = Retry.class, description = "Verify Relationship data is not blank")
    public void checkRelationshipDataNotBlankTest() {
        Map<String, String> relationshipData = subjectDetailsPageComponent
                .getInfoByHeader(SubjectDetailsPageComponent.SubjectDetailsSections.RELATIONSHIP);

        Assert.assertTrue(relationshipData.entrySet()
                .stream()
                .noneMatch(it -> it.getValue().isBlank()), "Relationship data should be not blank");
    }

    @TmsLink("INVHUB-1818")
    @Test(retryAnalyzer = Retry.class, description = "Verify Tax Details data is correct and not blank")
    public void checkTaxDetailsCorrectAndNotBlankTest() {
        SoftAssert softAssert = new SoftAssert();
        Map<String, String> taxDetailsData = subjectDetailsPageComponent
                .getInfoByHeader(SubjectDetailsPageComponent.SubjectDetailsSections.TAX_DETAILS);

        softAssert.assertTrue(taxDetailsData.equals(investigationPage.getInfoByHeader(
                InvestigationPage.InfoSectionType.TAX_DETAILS)), "Tax Details data should be the same as in summary");
        softAssert.assertTrue(taxDetailsData.entrySet()
                .stream()
                .noneMatch(it -> it.getValue().isBlank()), "Tax Details data should be not blank");
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1819")
    @Test(retryAnalyzer = Retry.class, description = "Verify Geographical Profile data is correct and not blank")
    public void checkGeographicalDataCorrectAndNotBlankTest() {
        SoftAssert softAssert = new SoftAssert();
        Map<String, String> geographicalProfileData = subjectDetailsPageComponent
                .getInfoByHeader(SubjectDetailsPageComponent.SubjectDetailsSections.GEOGRAPHICAL_PROFILE);

        softAssert.assertTrue(geographicalProfileData.equals(investigationPage.getInfoByHeader(
                        InvestigationPage.InfoSectionType.GEOGRAPHICAL_PROFILE)),
                "Geographical profile data should be the same as in summary");
        softAssert.assertTrue(geographicalProfileData.entrySet()
                .stream()
                .noneMatch(it -> it.getValue().isBlank()), "Geographical profile data should be not blank");
        softAssert.assertAll();
    }
}
