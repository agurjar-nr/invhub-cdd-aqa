package investigation.hub.test.kyccdd;

import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.core.util.Retry;
import investigation.hub.common.web.components.KYCCDDPanelPageComponent;
import investigation.hub.common.web.components.tables.InvestigationsTablePageComponent;
import investigation.hub.common.web.components.tables.InvestigationsTablePageComponent.HeaderName;
import investigation.hub.common.web.components.tables.KYCCDDPanelTablePageComponent;
import investigation.hub.common.web.components.tables.OpenDetectionsTablePageComponent;
import investigation.hub.common.web.components.tables.TableGeneralComponent;
import investigation.hub.common.web.pages.InvestigationPage;
import investigation.hub.test.core.AllOpenInvestigationsTest;
import io.qameta.allure.TmsLink;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static com.codeborne.selenide.Selenide.localStorage;
import static com.codeborne.selenide.Selenide.open;

@Log4j2
public class KYCCDDLatestScoreCardTest extends InvHubUiTest {
    InvestigationsTablePageComponent investigationsTablePageComponent;
    KYCCDDPanelPageComponent kyccddPanelPageComponent = new KYCCDDPanelPageComponent();

    String finCrimeType = "sys:CDD";

    @BeforeMethod
    public void openPage() {
        localStorage().clear();
        open(apiProperties.getUrl() + apiProperties.getOpenInvestigations());
        openInvestigationsPage.waitForLoad();
        HeaderName header = HeaderName.FINANCIAL_CRIME_TYPES;
        investigationsTablePageComponent = openInvestigationsPage
                .getInvestigationsTable();
        investigationsTablePageComponent
                .getFilterPageComponent()
                .clickColumnFilterIcon(header.getStringValue());
        selectCDDFilter();
    }

    public void selectCDDFilter() {
        if (kyccddPanelPageComponent.checkElementNoOption()) {
            openPage();
        }
        investigationsTablePageComponent
                .getFilterPageComponent().selectFilterQuery(finCrimeType);
        kyccddPanelPageComponent = kyccddPanelPageComponent
                .clickColumnFilterquery(finCrimeType);
        kyccddPanelPageComponent.toggleSwitch();

    }


    @TmsLink("INVHUB-3958")
    @Test(retryAnalyzer = Retry.class, description = "Verify KYC/CDD panel in Risk screen")
    public void validateKycCDDLabel() {
        Assert.assertTrue(kyccddPanelPageComponent.isElementPresent(kyccddPanelPageComponent.getKYC_CDD()),
                "KYC/CDD should appear");
    }

    @TmsLink("INVHUB-3959")
    @Test(retryAnalyzer = Retry.class, description = "Verify Score Details Component")
    public void validateScoreDetailsComponent() {
        Assert.assertTrue(kyccddPanelPageComponent.isElementPresent(kyccddPanelPageComponent.getCDD_Risk()), "CDD_Risk should appear");
        Assert.assertTrue(kyccddPanelPageComponent.isElementPresent(kyccddPanelPageComponent.getCDD_Score()), "CDD_Score should appear");
        Assert.assertTrue(kyccddPanelPageComponent.isElementPresent(kyccddPanelPageComponent.getScore_Date()), "Score_Date should appear");
        Assert.assertTrue(kyccddPanelPageComponent.isElementPresent(kyccddPanelPageComponent.getOutcome()), "Outcome should appear");

    }

    @TmsLink("INVHUB-3965")
    @Test(retryAnalyzer = Retry.class, description = "Verify ScoreDate field is not blank")
    public void checkScoreDateValueNotBlankTest() {
        Assert.assertFalse(kyccddPanelPageComponent.getScore_DateValue().getText().isBlank(),
                "ScoreDate field should not be blank");
    }

    @TmsLink("INVHUB-3965")
    @Test(retryAnalyzer = Retry.class, description = "Verify CDDRisk field is not blank")
    public void checkCddRiskValueNotBlankTest() {
        Assert.assertFalse(kyccddPanelPageComponent.getCDD_RiskValue().getText().isBlank(),
                "CDDRisk field should not be blank");
    }

    @TmsLink("INVHUB-3965")
    @Test(retryAnalyzer = Retry.class, description = "Verify CddScore field is not blank")
    public void checkCddScoreValueNotBlankTest() {
        Assert.assertFalse(kyccddPanelPageComponent.getCDD_ScoreValue().getText().isBlank(),
                "CddScore field should not be blank");
    }

    @TmsLink("INVHUB-3959")
    @Test(retryAnalyzer = Retry.class, description = "Verify the risk level color")
    public void checkRiskLevelColorTest() {
        Assert.assertTrue(kyccddPanelPageComponent.checkRiskLevelColor(),
                "Risk level color is not showing correctly");
    }

    @TmsLink("INVHUB-3717")
    @Test(retryAnalyzer = Retry.class, description = "Verify the successful renaming of the panel")
    public void checkRiskComponentTest() {
        Assert.assertTrue(kyccddPanelPageComponent.checkComponent(),
                "Risk Component doesn't match");

    }

    @TmsLink("INVHUB-3977")
    @Test(retryAnalyzer = Retry.class, description = "Verify the scoring group sequence")
    public void checkPanelSequence() {
        Assert.assertTrue(kyccddPanelPageComponent.panelIndex(),
                "Panels are not arranged in proper sequence");
    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Geography can be sorted by 'Name' column")
    public void checkGeographyNameColumnIsSortableTest() {
        sortTableColumnsAndCheck(
                KYCCDDPanelTablePageComponent.PanelName.GEOGRAPHY,
                KYCCDDPanelTablePageComponent.HeaderName.GEOGRAPHY_NAME,
                TableGeneralComponent.SortStatus.ASCENDING);

        sortTableColumnsAndCheck(
                KYCCDDPanelTablePageComponent.PanelName.GEOGRAPHY,
                KYCCDDPanelTablePageComponent.HeaderName.GEOGRAPHY_NAME,
                TableGeneralComponent.SortStatus.DESCENDING);
    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Geography can be sorted by 'Score' column")
    public void checkGeographyScoreColumnIsSortableTest() {
        sortTableColumnsAndCheck(
                KYCCDDPanelTablePageComponent.PanelName.GEOGRAPHY,
                KYCCDDPanelTablePageComponent.HeaderName.GEOGRAPHY_SCORE,
                TableGeneralComponent.SortStatus.DESCENDING);

        sortTableColumnsAndCheck(
                KYCCDDPanelTablePageComponent.PanelName.GEOGRAPHY,
                KYCCDDPanelTablePageComponent.HeaderName.GEOGRAPHY_SCORE,
                TableGeneralComponent.SortStatus.ASCENDING);
    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Geography can be sorted by 'Information' column")
    public void checkGeographyInformationColumnIsSortableTest() {
        sortTableColumnsAndCheck(
                KYCCDDPanelTablePageComponent.PanelName.GEOGRAPHY,
                KYCCDDPanelTablePageComponent.HeaderName.GEOGRAPHY_INFORMATION,
                TableGeneralComponent.SortStatus.DESCENDING);

        sortTableColumnsAndCheck(
                KYCCDDPanelTablePageComponent.PanelName.GEOGRAPHY,
                KYCCDDPanelTablePageComponent.HeaderName.GEOGRAPHY_INFORMATION,
                TableGeneralComponent.SortStatus.ASCENDING);
    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Customer can be sorted by 'Name' column")
    public void checkCustomerNameColumnIsSortableTest() {
        sortTableColumnsAndCheck(
                KYCCDDPanelTablePageComponent.PanelName.CUSTOMER,
                KYCCDDPanelTablePageComponent.HeaderName.CUSTOMER_NAME,
                TableGeneralComponent.SortStatus.DESCENDING);

        sortTableColumnsAndCheck(
                KYCCDDPanelTablePageComponent.PanelName.CUSTOMER,
                KYCCDDPanelTablePageComponent.HeaderName.CUSTOMER_NAME,
                TableGeneralComponent.SortStatus.ASCENDING);
    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Customer can be sorted by 'Score' column")
    public void checkCustomerScoreColumnIsSortableTest() {
        sortTableColumnsAndCheck(
                KYCCDDPanelTablePageComponent.PanelName.CUSTOMER,
                KYCCDDPanelTablePageComponent.HeaderName.CUSTOMER_SCORE,
                TableGeneralComponent.SortStatus.DESCENDING);

        sortTableColumnsAndCheck(
                KYCCDDPanelTablePageComponent.PanelName.CUSTOMER,
                KYCCDDPanelTablePageComponent.HeaderName.CUSTOMER_SCORE,
                TableGeneralComponent.SortStatus.ASCENDING);
    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Customer can be sorted by 'Information' column")
    public void checkCustomerColumnIsSortableTest() {
        sortTableColumnsAndCheck(
                KYCCDDPanelTablePageComponent.PanelName.CUSTOMER,
                KYCCDDPanelTablePageComponent.HeaderName.CUSTOMER_INFORMATION,
                TableGeneralComponent.SortStatus.ASCENDING);
        sortTableColumnsAndCheck(
                KYCCDDPanelTablePageComponent.PanelName.CUSTOMER,
                KYCCDDPanelTablePageComponent.HeaderName.CUSTOMER_INFORMATION,
                TableGeneralComponent.SortStatus.DESCENDING);
    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Channel can be sorted by 'Name' column")
    public void checkChannelNameColumnIsSortableTest() {
        sortTableColumnsAndCheck(
                KYCCDDPanelTablePageComponent.PanelName.CHANNEL,
                KYCCDDPanelTablePageComponent.HeaderName.CHANNEL_NAME,
                TableGeneralComponent.SortStatus.ASCENDING);

        sortTableColumnsAndCheck(
                KYCCDDPanelTablePageComponent.PanelName.CHANNEL,
                KYCCDDPanelTablePageComponent.HeaderName.CHANNEL_NAME,
                TableGeneralComponent.SortStatus.DESCENDING);
    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Channel can be sorted by 'Score' column")
    public void checkChannelScoreColumnIsSortableTest() {
        sortTableColumnsAndCheck(
                KYCCDDPanelTablePageComponent.PanelName.CHANNEL,
                KYCCDDPanelTablePageComponent.HeaderName.CHANNEL_SCORE,
                TableGeneralComponent.SortStatus.ASCENDING);

        sortTableColumnsAndCheck(
                KYCCDDPanelTablePageComponent.PanelName.CHANNEL,
                KYCCDDPanelTablePageComponent.HeaderName.CHANNEL_SCORE,
                TableGeneralComponent.SortStatus.DESCENDING);
    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Override can be sorted by 'Name' column")
    public void checkOverrideNameColumnIsSortableTest() {
        sortTableColumnsAndCheck(
                KYCCDDPanelTablePageComponent.PanelName.OVERRIDE,
                KYCCDDPanelTablePageComponent.HeaderName.OVERRIDE_NAME,
                TableGeneralComponent.SortStatus.ASCENDING);

        sortTableColumnsAndCheck(
                KYCCDDPanelTablePageComponent.PanelName.OVERRIDE,
                KYCCDDPanelTablePageComponent.HeaderName.OVERRIDE_NAME,
                TableGeneralComponent.SortStatus.DESCENDING);
    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Override can be sorted by 'Score' column")
    public void checkOverrideScoreColumnIsSortableTest() {
        sortTableColumnsAndCheck(KYCCDDPanelTablePageComponent.PanelName.OVERRIDE, KYCCDDPanelTablePageComponent.HeaderName.OVERRIDE_SCORE, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableColumnsAndCheck(KYCCDDPanelTablePageComponent.PanelName.OVERRIDE, KYCCDDPanelTablePageComponent.HeaderName.OVERRIDE_SCORE, TableGeneralComponent.SortStatus.DESCENDING);
    }



    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Override can be sorted by 'Information' column")
    public void checkOverrideInformationColumnIsSortableTest() {
        sortTableColumnsAndCheck( KYCCDDPanelTablePageComponent.PanelName.OVERRIDE,
                KYCCDDPanelTablePageComponent.HeaderName.OVERRIDE_INFORMATION,
                TableGeneralComponent.SortStatus.ASCENDING);

        sortTableColumnsAndCheck( KYCCDDPanelTablePageComponent.PanelName.OVERRIDE,
                KYCCDDPanelTablePageComponent.HeaderName.OVERRIDE_INFORMATION,
                TableGeneralComponent.SortStatus.DESCENDING);

    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Product can be sorted by 'Name' column")
    public void checkProductNameColumnIsSortableTest() {
        sortTableColumnsAndCheck(
                KYCCDDPanelTablePageComponent.PanelName.PRODUCT,
                KYCCDDPanelTablePageComponent.HeaderName.PRODUCT_NAME,
                TableGeneralComponent.SortStatus.ASCENDING);

        sortTableColumnsAndCheck(
                KYCCDDPanelTablePageComponent.PanelName.PRODUCT,
                KYCCDDPanelTablePageComponent.HeaderName.PRODUCT_NAME,
                TableGeneralComponent.SortStatus.DESCENDING);
    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Product can be sorted by 'Information' column")
    public void checkProductInformationColumnIsSortableTest() {
        sortTableColumnsAndCheck(
                KYCCDDPanelTablePageComponent.PanelName.PRODUCT,
                KYCCDDPanelTablePageComponent.HeaderName.PRODUCT_INFORMATION,
                TableGeneralComponent.SortStatus.ASCENDING);
        sortTableColumnsAndCheck(
                KYCCDDPanelTablePageComponent.PanelName.PRODUCT,
                KYCCDDPanelTablePageComponent.HeaderName.PRODUCT_INFORMATION,
                TableGeneralComponent.SortStatus.DESCENDING);
    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Product can be sorted by 'Score' column")
    public void checkProductScoreColumnIsSortableTest() {
        sortTableColumnsAndCheck(
                KYCCDDPanelTablePageComponent.PanelName.PRODUCT,
                KYCCDDPanelTablePageComponent.HeaderName.PRODUCT_INFORMATION,
                TableGeneralComponent.SortStatus.ASCENDING);
        sortTableColumnsAndCheck(
                KYCCDDPanelTablePageComponent.PanelName.PRODUCT,
                KYCCDDPanelTablePageComponent.HeaderName.PRODUCT_INFORMATION,
                TableGeneralComponent.SortStatus.DESCENDING);
    }

    @TmsLink("INVHUB-3979")
    @Test(description = "Verify Geography can be filtered by 'Name' column")
    public void checkGeographyCanBeFilteredByNameTest() {
        KYCCDDPanelTablePageComponent.HeaderName header = KYCCDDPanelTablePageComponent.HeaderName.GEOGRAPHY_NAME;
        KYCCDDPanelTablePageComponent.PanelName panel = KYCCDDPanelTablePageComponent.PanelName.GEOGRAPHY;
        KYCCDDPanelTablePageComponent kyccddPanelTablePageComponent = kyccddPanelPageComponent
                .getKyccddPanelTablePageComponent();
        filterTableColumnsAndCheck(panel, header);

    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Geography can be filtered by 'Score' column")
    public void checkGeographyCanBeFilteredByScoreTest() {
        KYCCDDPanelTablePageComponent.HeaderName header = KYCCDDPanelTablePageComponent.HeaderName.GEOGRAPHY_SCORE;
        KYCCDDPanelTablePageComponent.PanelName panel = KYCCDDPanelTablePageComponent.PanelName.GEOGRAPHY;
        KYCCDDPanelTablePageComponent kyccddPanelTablePageComponent = kyccddPanelPageComponent
                .getKyccddPanelTablePageComponent();
        filterTableColumnsAndCheck(panel, header);

    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Geography can be filtered by 'Information' column")
    public void checkGeographyCanBeFilteredByInformationTest() {
        KYCCDDPanelTablePageComponent.HeaderName header = KYCCDDPanelTablePageComponent.HeaderName.GEOGRAPHY_INFORMATION;
        KYCCDDPanelTablePageComponent.PanelName panel = KYCCDDPanelTablePageComponent.PanelName.GEOGRAPHY;
        KYCCDDPanelTablePageComponent kyccddPanelTablePageComponent = kyccddPanelPageComponent
                .getKyccddPanelTablePageComponent();
        filterTableColumnsAndCheck(panel, header);

    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Channel can be filtered by 'Name' column")
    public void checkChannelcanBeFilteredByNameTest() {
        KYCCDDPanelTablePageComponent.HeaderName header = KYCCDDPanelTablePageComponent.HeaderName.CHANNEL_NAME;
        KYCCDDPanelTablePageComponent.PanelName panel = KYCCDDPanelTablePageComponent.PanelName.CHANNEL;
        KYCCDDPanelTablePageComponent kyccddPanelTablePageComponent = kyccddPanelPageComponent
                .getKyccddPanelTablePageComponent();
        filterTableColumnsAndCheck(panel, header);
    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Channel can be filtered by 'Information' column")
    public void checkChannelCanBeFilteredByInformationTest() {
        KYCCDDPanelTablePageComponent.HeaderName header = KYCCDDPanelTablePageComponent.HeaderName.CHANNEL_INFORMATION;
        KYCCDDPanelTablePageComponent.PanelName panel = KYCCDDPanelTablePageComponent.PanelName.CHANNEL;
        KYCCDDPanelTablePageComponent kyccddPanelTablePageComponent = kyccddPanelPageComponent
                .getKyccddPanelTablePageComponent();
        filterTableColumnsAndCheck(panel, header);
    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Channel can be filtered by 'Score' column")
    public void checkChannelCanBeFilteredByScoreTest() {
        KYCCDDPanelTablePageComponent.HeaderName header = KYCCDDPanelTablePageComponent.HeaderName.CHANNEL_SCORE;
        KYCCDDPanelTablePageComponent.PanelName panel = KYCCDDPanelTablePageComponent.PanelName.CHANNEL;
        KYCCDDPanelTablePageComponent kyccddPanelTablePageComponent = kyccddPanelPageComponent
                .getKyccddPanelTablePageComponent();
        filterTableColumnsAndCheck(panel, header);
    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Customer can be filtered by 'Name' column")
    public void checkCustomerCanBeFilteredByNameTest() {
        KYCCDDPanelTablePageComponent.HeaderName header = KYCCDDPanelTablePageComponent.HeaderName.CUSTOMER_NAME;
        KYCCDDPanelTablePageComponent.PanelName panel = KYCCDDPanelTablePageComponent.PanelName.CUSTOMER;
        KYCCDDPanelTablePageComponent kyccddPanelTablePageComponent = kyccddPanelPageComponent
                .getKyccddPanelTablePageComponent();
        filterTableColumnsAndCheck(panel, header);
    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Customer can be filtered by 'Score' column")
    public void checkCustomerCanBeFilteredByScoreTest() {
        KYCCDDPanelTablePageComponent.HeaderName header = KYCCDDPanelTablePageComponent.HeaderName.CUSTOMER_SCORE;
        KYCCDDPanelTablePageComponent.PanelName panel = KYCCDDPanelTablePageComponent.PanelName.CUSTOMER;
        KYCCDDPanelTablePageComponent kyccddPanelTablePageComponent = kyccddPanelPageComponent
                .getKyccddPanelTablePageComponent();
        filterTableColumnsAndCheck(panel, header);
    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Customer can be filtered by 'Information' column")
    public void checkCustomerCanBeFilteredByInformationTest() {
        KYCCDDPanelTablePageComponent.HeaderName header = KYCCDDPanelTablePageComponent.HeaderName.CUSTOMER_INFORMATION;
        KYCCDDPanelTablePageComponent.PanelName panel = KYCCDDPanelTablePageComponent.PanelName.CUSTOMER;
        KYCCDDPanelTablePageComponent kyccddPanelTablePageComponent = kyccddPanelPageComponent
                .getKyccddPanelTablePageComponent();
        filterTableColumnsAndCheck(panel, header);
    }


    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Product can be filtered by 'Name' column")
    public void checkProductCanBeFilteredByNameTest() {
        KYCCDDPanelTablePageComponent.HeaderName header = KYCCDDPanelTablePageComponent.HeaderName.PRODUCT_NAME;
        KYCCDDPanelTablePageComponent.PanelName panel = KYCCDDPanelTablePageComponent.PanelName.PRODUCT;
        KYCCDDPanelTablePageComponent kyccddPanelTablePageComponent = kyccddPanelPageComponent
                .getKyccddPanelTablePageComponent();
        filterTableColumnsAndCheck(panel, header);

    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Product can be filtered by 'Score' column")
    public void checkProductCanBeFilteredByScoreTest() {
        KYCCDDPanelTablePageComponent.HeaderName header = KYCCDDPanelTablePageComponent.HeaderName.PRODUCT_SCORE;
        KYCCDDPanelTablePageComponent.PanelName panel = KYCCDDPanelTablePageComponent.PanelName.PRODUCT;
        KYCCDDPanelTablePageComponent kyccddPanelTablePageComponent = kyccddPanelPageComponent
                .getKyccddPanelTablePageComponent();
        filterTableColumnsAndCheck(panel, header);

    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Product can be filtered by 'Information' column")
    public void checkProductCanBeFilteredByInformationTest() {
        KYCCDDPanelTablePageComponent.HeaderName header = KYCCDDPanelTablePageComponent.HeaderName.PRODUCT_INFORMATION;
        KYCCDDPanelTablePageComponent.PanelName panel = KYCCDDPanelTablePageComponent.PanelName.PRODUCT;
        KYCCDDPanelTablePageComponent kyccddPanelTablePageComponent = kyccddPanelPageComponent
                .getKyccddPanelTablePageComponent();
        filterTableColumnsAndCheck(panel, header);

    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Override can be filtered by 'Name' column")
    public void checkOverrideCanBeFilteredByNameTest() {
        KYCCDDPanelTablePageComponent.HeaderName header = KYCCDDPanelTablePageComponent.HeaderName.OVERRIDE_NAME;
        KYCCDDPanelTablePageComponent.PanelName panel = KYCCDDPanelTablePageComponent.PanelName.OVERRIDE;
        KYCCDDPanelTablePageComponent kyccddPanelTablePageComponent = kyccddPanelPageComponent
                .getKyccddPanelTablePageComponent();
        filterTableColumnsAndCheck(panel, header);

    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Override can be filtered by 'Score' column")
    public void checkOverrideCanBeFilteredByScoreTest() {
        KYCCDDPanelTablePageComponent.HeaderName header = KYCCDDPanelTablePageComponent.HeaderName.OVERRIDE_SCORE;
        KYCCDDPanelTablePageComponent.PanelName panel = KYCCDDPanelTablePageComponent.PanelName.OVERRIDE;
        KYCCDDPanelTablePageComponent kyccddPanelTablePageComponent = kyccddPanelPageComponent
                .getKyccddPanelTablePageComponent();
        filterTableColumnsAndCheck(panel, header);

    }

    @TmsLink("INVHUB-3979")
    @Test(retryAnalyzer = Retry.class, description = "Verify Override can be filtered by 'Information' column")
    public void checkOverrideCanBeFilteredByInformationTest() {
        KYCCDDPanelTablePageComponent.HeaderName header = KYCCDDPanelTablePageComponent.HeaderName.OVERRIDE_INFORMATION;
        KYCCDDPanelTablePageComponent.PanelName panel = KYCCDDPanelTablePageComponent.PanelName.OVERRIDE;
        KYCCDDPanelTablePageComponent kyccddPanelTablePageComponent = kyccddPanelPageComponent
                .getKyccddPanelTablePageComponent();
        filterTableColumnsAndCheck(panel, header);

    }


    private void sortTableColumnsAndCheck(KYCCDDPanelTablePageComponent.PanelName panelName, KYCCDDPanelTablePageComponent.HeaderName headerName,
                                          TableGeneralComponent.SortStatus sortType) {
        KYCCDDPanelTablePageComponent kyccddPanelTablePageComponent = new KYCCDDPanelTablePageComponent();
        if (kyccddPanelTablePageComponent.checkPanelAvailability(panelName)) {
            kyccddPanelPageComponent
                    .getKyccddPanelTablePageComponent()
                    .sortTable(panelName, headerName, sortType);

            List<String> actualColumnData = kyccddPanelTablePageComponent.getColumnData(panelName, headerName.getStringValue());
            Assert.assertEquals(actualColumnData,
                    kyccddPanelTablePageComponent.getListByDirection(
                            headerName.getStringValue(),
                            sortType),
                    headerName.getStringValue() + " Column should be sorted properly, sort type is: " + sortType);
        } else {
            log.info(panelName.getStringValue() + " is not available");

        }

    }

    private void filterTableColumnsAndCheck(KYCCDDPanelTablePageComponent.PanelName panelName,
                                            KYCCDDPanelTablePageComponent.HeaderName header) {
        KYCCDDPanelTablePageComponent kyccddPanelTablePageComponent = new KYCCDDPanelTablePageComponent();
        if (kyccddPanelTablePageComponent.checkPanelAvailability(panelName)) {
            String randomColumnValue = kyccddPanelTablePageComponent.getAnyColumnValueKYC(panelName, header);
            System.out.println("randomColumnValue--->"+randomColumnValue);
             kyccddPanelTablePageComponent
                    .clickColumnFilterIcon(panelName, header)
                    .enterFilterValue(panelName, header, randomColumnValue);
            List<String> actualColumnData = kyccddPanelTablePageComponent.getFilteredValue(panelName, header.getStringValue());
            System.out.println("actualColumnData-->"+actualColumnData);
            if (header.getStringValue().equals("Score")) {
                Assert.assertTrue(actualColumnData.contains(randomColumnValue), panelName.getStringValue() + " should be filtered by " + header.getStringValue() + " column correctly");
            } else {

                Assert.assertTrue(
                        kyccddPanelTablePageComponent.getFilteredValue(panelName, header.getStringValue())
                                .stream()
                                .allMatch(it -> it.equals(randomColumnValue)),
                        panelName.getStringValue() + " should be filtered by " + header.getStringValue() + " column correctly");
            }
        } else {
            log.info(panelName.getStringValue() + " is not available");

        }

    }


}





