package investigation.hub.test.core;

import com.codeborne.selenide.Selenide;
import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.core.util.Retry;
import investigation.hub.common.web.components.AmlPageComponent;
import investigation.hub.common.web.components.OpenDetectionsPageComponent;
import investigation.hub.common.web.components.tables.HistoricalDetectionsTablePageComponent;
import investigation.hub.common.web.components.tables.InvestigationsTablePageComponent;
import investigation.hub.common.web.components.tables.OpenDetectionsTablePageComponent;
import investigation.hub.common.web.test.data.dtos.TransactionProvider;
import investigation.hub.common.web.test.data.dtos.transaction.Transaction;
import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static investigation.hub.common.web.components.modals.LeftFloatMenuInvestigationModalComponent.LeftMenuItemName.DETECTIONS;
import static investigation.hub.common.web.components.tables.OpenDetectionsTablePageComponent.HeaderName.SCENARIO_NAME;
import static investigation.hub.common.web.test.data.dtos.transaction.Aml.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Log4j2
public class DetectionsValidationTest extends InvHubUiTest {
    private static final String SUBJECT_ID_OPEN = "REF-CUSTEST-LIST-87-0107";
    private static final String SUBJECT_ID_HISTORICAL = "AUTO_SUBJ_14"; //TODO: to be updated, when INVHUB-3194 is fixed

    InvestigationsTablePageComponent investigationsTablePageComponent;

    HistoricalDetectionsTablePageComponent historicalDetectionsTablePageComponent;

    AmlPageComponent amlPageComponent;
    OpenDetectionsPageComponent openDetectionsPageComponent;

    @BeforeMethod
    public void openPage() {
        Selenide.open(apiProperties.getUrl() + apiProperties.getOpenInvestigations());
        openInvestigationsPage.waitForLoad();

        investigationsTablePageComponent = openInvestigationsPage.clickAllWorkButton()
                .getInvestigationsTable();
    }

    @DataProvider(name = "openData")
    static Object[][] openData() {
        return new Object[][] {
                {new TransactionProvider(AML03, SUBJECT_ID_OPEN, true).provide()},
                {new TransactionProvider(AML13, SUBJECT_ID_OPEN, true).provide()},
                {new TransactionProvider(AML16, SUBJECT_ID_OPEN, true).provide()},
                {new TransactionProvider(AML17, SUBJECT_ID_OPEN, true).provide()},
                {new TransactionProvider(AML18, SUBJECT_ID_OPEN, true).provide()},
                {new TransactionProvider(AML50, SUBJECT_ID_OPEN, true).provide()},
                {new TransactionProvider(AML51, SUBJECT_ID_OPEN, true).provide()},
                {new TransactionProvider(AML52, SUBJECT_ID_OPEN, true).provide()},
                {new TransactionProvider(AML56, SUBJECT_ID_OPEN, true).provide()},
                {new TransactionProvider(AML57, SUBJECT_ID_OPEN, true).provide()},
                {new TransactionProvider(AML99, SUBJECT_ID_OPEN, true).provide()}
        };
    }

    @DataProvider(name = "historicalData")
    static Object[][] historicalData() {
        return new Object[][] {
                {new TransactionProvider(AML03, SUBJECT_ID_HISTORICAL, false).provide()},
                {new TransactionProvider(AML13, SUBJECT_ID_HISTORICAL, false).provide()},
                {new TransactionProvider(AML16, SUBJECT_ID_HISTORICAL, false).provide()},
                {new TransactionProvider(AML17, SUBJECT_ID_HISTORICAL, false).provide()},
                {new TransactionProvider(AML18, SUBJECT_ID_HISTORICAL, false).provide()},
                {new TransactionProvider(AML50, SUBJECT_ID_HISTORICAL, false).provide()},
                {new TransactionProvider(AML51, SUBJECT_ID_HISTORICAL, false).provide()},
                {new TransactionProvider(AML52, SUBJECT_ID_HISTORICAL, false).provide()},
                {new TransactionProvider(AML56, SUBJECT_ID_HISTORICAL, false).provide()},
                {new TransactionProvider(AML57, SUBJECT_ID_HISTORICAL, false).provide()},
                {new TransactionProvider(AML99, SUBJECT_ID_HISTORICAL, false).provide()}
        };
    }

    @TmsLink("INVHUB-2293")
    @Test(description = "Validate Transaction field values for `Open Detections` tab", dataProvider = "openData", retryAnalyzer = Retry.class)
    public void validateOpenDetectionValues(Transaction expected) {
        amlPageComponent = investigationsTablePageComponent
                .clickRowBySubjectId(expected.getSubjectId())
                .getLeftMenuComponent()
                .clickMenuItemByName(DETECTIONS);
        openDetectionsPageComponent = amlPageComponent.getOpenDetectionsPageComponent();

        OpenDetectionsTablePageComponent openDetectionsTablePageComponent =
                openDetectionsPageComponent.getOpenDetectionsTablePageComponent();
        Map<String, String> amlDetection =
                Map.of(SCENARIO_NAME.getStringValue(), expected.getAml().name() + " " + expected.getAml().getInfo());

        Transaction actual = openDetectionsTablePageComponent.clickExpandRowIconByRowData(amlDetection)//.expandDetectionRow(amlDetection)
                .getTransaction();
        validateDataValues(actual, expected);
    }

    @Issue("INVHUB-3194")
    @TmsLink("INVHUB-2351")
    @Test(description = "Validate AML `Historical Detections` table row values", dataProvider = "historicalData", retryAnalyzer = Retry.class)
    public void validateHistoricalDetectionValues(Transaction expected) {
        amlPageComponent = investigationsTablePageComponent.clickRowBySubjectId(expected.getSubjectId())
                .getLeftMenuComponent()
                .clickMenuItemByName(DETECTIONS);

        historicalDetectionsTablePageComponent = amlPageComponent.clickHistoricalDetectionsButton()
                .getHistoricalDetectionsTablePageComponent();

        Map<String, String> amlDetection =
                Map.of(SCENARIO_NAME.getStringValue(), expected.getAml().name() + " " + expected.getAml().getInfo());

        Transaction actual = historicalDetectionsTablePageComponent.clickExpandRowIconByRowData(amlDetection)//.expandDetectionRow(amlDetection)
                .getTransaction();
        validateDataValues(actual, expected);
    }

    private void validateDataValues(Transaction actual, Transaction expected) {
        assertThat(actual).usingRecursiveComparison().ignoringActualNullFields()
                .ignoringFields("originationDate")
                .describedAs("Compare actual and expected values by fields: Transaction ID, Debit/Credit, Base Amount, "
                        + "Transaction Type, Beneficiary Name & Country, Originator Name & Country, Account ID")
                .isEqualTo(expected);
    }

}
