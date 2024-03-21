package investigation.hub.test.core;

import com.github.javafaker.Faker;
import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.core.util.Retry;
import investigation.hub.common.web.components.ContentPageComponent;
import investigation.hub.common.web.components.modals.LeftFloatMenuInvestigationModalComponent;
import investigation.hub.common.web.components.tables.ContentTablePageComponent;
import investigation.hub.common.web.components.tables.InvestigationsTablePageComponent;
import investigation.hub.common.web.components.tables.TableGeneralComponent;
import investigation.hub.common.web.pages.InvestigationPage;
import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;

public class ContentTest extends InvHubUiTest {

    public final String fileName = "testFile.txt";
    public final String filePath = "src/main/resources/" + fileName;
    public String subjectId;
    InvestigationPage investigationPage;
    InvestigationsTablePageComponent investigationsTablePageComponent;
    ContentTablePageComponent contentTablePageComponent = new ContentPageComponent().getContentTable();

    @BeforeMethod
    public void openPage() {
        open(apiProperties.getUrl() + apiProperties.getOpenInvestigations());
        openInvestigationsPage.waitForLoad();
        investigationsTablePageComponent = openInvestigationsPage
                .clickAllWorkButton()
                .getInvestigationsTable();
        subjectId = "REF-CUSTEST-LIST-270-0106";
    }

    @TmsLink("INVHUB-1239")
    @Test(retryAnalyzer = Retry.class, description = "Verify user can add note to subject")
    public void checkNoteCanBeAddedToSubjectTest() {
        SoftAssert softAssert = new SoftAssert();
        Map<String, String> note = new HashMap<>();
        note.put(ContentTablePageComponent.HeaderName.TYPE.getStringValue(), "Note");
        note.put(ContentTablePageComponent.HeaderName.SUMMARY.getStringValue(), new Faker().funnyName().name());
        investigationPage = investigationsTablePageComponent.clickRowBySubjectId(subjectId);
        investigationPage.clickAddNoteButton().enterText(note.get(ContentTablePageComponent.HeaderName.SUMMARY.getStringValue()))
                .clickSaveButton();

        softAssert.assertTrue(investigationPage.isNewNoteAddedSuccessfullyMessageAppeared(),
                "Successful note added message should appear");
        ContentPageComponent contentPageComponent = investigationPage.getLeftMenuComponent().clickMenuItemByName(
                LeftFloatMenuInvestigationModalComponent.LeftMenuItemName.CONTENT);
        softAssert.assertNotNull(contentPageComponent.getContentTable().getRowByContent(note),
                "Row with created note should be present in the Content table");
        softAssert.assertAll();
    }

    @Issue("INVHUB-2503")
    @TmsLink("INVHUB-1238")
    @Test(retryAnalyzer = Retry.class, description = "Verify user can attach files to subject with 'Attach file' image")
    public void checkFileCanBeAttachedToSubjectTest() {
        SoftAssert softAssert = new SoftAssert();
        Map<String, String> file = new HashMap<>();
        file.put(ContentTablePageComponent.HeaderName.TYPE.getStringValue(), "Attachment");
        file.put(ContentTablePageComponent.HeaderName.SUMMARY.getStringValue(), "File Name: " + fileName);
        investigationPage = investigationsTablePageComponent.clickRowBySubjectId(subjectId);
        investigationPage.clickAddFileButton()
                .uploadFile(filePath);

        softAssert.assertTrue(investigationPage.isNewFileUploadingMessageAppeared(fileName),
                "Successful file uploading message should appear");
        softAssert.assertTrue(investigationPage.isNewFileUploadedSuccessfullyMessageAppeared(fileName),
                "Successful file uploaded message should appear");
        ContentPageComponent contentPageComponent = investigationPage.getLeftMenuComponent().clickMenuItemByName(
                LeftFloatMenuInvestigationModalComponent.LeftMenuItemName.CONTENT);
        softAssert.assertNotNull(contentPageComponent.getContentTable().getRowByContent(file),
                "Row with created file should be present in the Content table");
        softAssert.assertAll();
    }


    @TmsLink("INVHUB-1638")
    @Test(retryAnalyzer = Retry.class, description = "Verify Content can be sorted by 'Type' column")
    public void checkContentTypeColumnIsSortableTest() {
        SoftAssert softAssert = new SoftAssert();
        ContentTablePageComponent.HeaderName headerName = ContentTablePageComponent.HeaderName.TYPE;
        investigationsTablePageComponent
                .clickRowBySubjectId(subjectId)
                .getLeftMenuComponent()
                .clickMenuItemByName(LeftFloatMenuInvestigationModalComponent.LeftMenuItemName.CONTENT);
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    @Issue("INVHUB-2644")
    @TmsLink("INVHUB-1245")
    @Test(retryAnalyzer = Retry.class, description = "Verify Content can be sorted by 'Summary' column")
    public void checkContentSummaryColumnIsSortableTest() {
        SoftAssert softAssert = new SoftAssert();
        ContentTablePageComponent.HeaderName headerName = ContentTablePageComponent.HeaderName.SUMMARY;
        investigationsTablePageComponent
                .clickRowBySubjectId(subjectId)
                .getLeftMenuComponent()
                .clickMenuItemByName(LeftFloatMenuInvestigationModalComponent.LeftMenuItemName.CONTENT);
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1639")
    @Test(retryAnalyzer = Retry.class, description = "Verify Content can be sorted by 'Added By' column")
    public void checkContentAddedByColumnIsSortableTest() {
        SoftAssert softAssert = new SoftAssert();
        ContentTablePageComponent.HeaderName headerName = ContentTablePageComponent.HeaderName.ADDED_BY;
        investigationsTablePageComponent
                .clickRowBySubjectId(subjectId)
                .getLeftMenuComponent()
                .clickMenuItemByName(LeftFloatMenuInvestigationModalComponent.LeftMenuItemName.CONTENT);
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1640")
    @Test(retryAnalyzer = Retry.class, description = "Verify Content can be sorted by 'Added On' column")
    public void checkContentAddedOnColumnIsSortableTest() {
        SoftAssert softAssert = new SoftAssert();
        ContentTablePageComponent.HeaderName headerName = ContentTablePageComponent.HeaderName.ADDED_ON;
        investigationsTablePageComponent
                .clickRowBySubjectId(subjectId)
                .getLeftMenuComponent()
                .clickMenuItemByName(LeftFloatMenuInvestigationModalComponent.LeftMenuItemName.CONTENT);
        sortTableDateColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableDateColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1246")
    @Test(retryAnalyzer = Retry.class, description = "Verify Content can be filtered by Type")
    public void checkContentCanBeFilteredByTypeTest() {
        ContentTablePageComponent.HeaderName header = ContentTablePageComponent.HeaderName.TYPE;
        investigationsTablePageComponent
                .clickRowBySubjectId(subjectId)
                .getLeftMenuComponent()
                .clickMenuItemByName(LeftFloatMenuInvestigationModalComponent.LeftMenuItemName.CONTENT);
        List<String> contentType =
                Collections.singletonList(contentTablePageComponent.getAnyColumnValue(header));
        contentTablePageComponent.getFilterPageComponent()
                .clickColumnFilterIcon(header.getStringValue())
                .selectFilterQueries(contentType);

        Assert.assertTrue(
                contentType.containsAll(contentTablePageComponent.getColumnData(header.getStringValue())),
                "Content should be filtered by 'Type' correctly");
    }

    @TmsLink("INVHUB-1644")
    @Test(retryAnalyzer = Retry.class, description = "Verify Content can be filtered by Summary")
    public void checkContentCanBeFilteredBySummaryTest() {
        ContentTablePageComponent.HeaderName header = ContentTablePageComponent.HeaderName.SUMMARY;
        investigationsTablePageComponent
                .clickRowBySubjectId(subjectId)
                .getLeftMenuComponent()
                .clickMenuItemByName(LeftFloatMenuInvestigationModalComponent.LeftMenuItemName.CONTENT);
        String contentSummary = contentTablePageComponent.getAnyColumnValue(header);
        contentTablePageComponent.getFilterPageComponent()
                .clickColumnFilterIcon(header.getStringValue())
                .enterFilterValue(header.getStringValue(), contentSummary);

        Assert.assertTrue(
                contentTablePageComponent.getColumnData(header.getStringValue()).stream()
                        .allMatch(it -> it.equals(contentSummary)),
                "Content should be filtered by 'Summary' correctly");
    }

    @TmsLink("INVHUB-1645")
    @Test(retryAnalyzer = Retry.class, description = "Verify Content can be filtered by Added By")
    public void checkContentCanBeFilteredByAddedByTest() {
        ContentTablePageComponent.HeaderName header = ContentTablePageComponent.HeaderName.ADDED_BY;
        investigationsTablePageComponent
                .clickRowBySubjectId(subjectId)
                .getLeftMenuComponent()
                .clickMenuItemByName(LeftFloatMenuInvestigationModalComponent.LeftMenuItemName.CONTENT);
        List<String> contentAddedBy =
                Collections.singletonList(contentTablePageComponent.getAnyColumnValue(header));
        contentTablePageComponent.getFilterPageComponent()
                .clickColumnFilterIcon(header.getStringValue())
                .selectFilterQueries(contentAddedBy);

        Assert.assertTrue(
                contentAddedBy.containsAll(contentTablePageComponent.getColumnData(header.getStringValue())),
                "Content should be filtered by 'Added By' correctly");
    }

    @Issue("INVHUB-2503")
    @TmsLink("INVHUB-1646")
    @Test(retryAnalyzer = Retry.class, description = "Verify Content can be filtered by Added On")
    public void checkContentCanBeFilteredByAddedOnTest() {
        ContentTablePageComponent.HeaderName header = ContentTablePageComponent.HeaderName.ADDED_ON;
        investigationsTablePageComponent
                .clickRowBySubjectId(subjectId)
                .getLeftMenuComponent()
                .clickMenuItemByName(LeftFloatMenuInvestigationModalComponent.LeftMenuItemName.CONTENT);
        String contentAddedOn = contentTablePageComponent.getAnyColumnValue(header);
        contentTablePageComponent.getFilterPageComponent()
                .clickColumnFilterIcon(header.getStringValue())
                .enterFilterDateValue(contentAddedOn, contentAddedOn, contentTablePageComponent.getFormatter());

        Assert.assertTrue(
                contentTablePageComponent.getFilterPageComponent()
                        .isDateTimeColumnFiltered(contentTablePageComponent.getColumnData(header.getStringValue()),
                                contentAddedOn, contentAddedOn, contentTablePageComponent.getFormatter()),
                "Content should be filtered by 'Added On' correctly");
    }

    private void sortTableColumnsAndCheck(SoftAssert softAssert, ContentTablePageComponent.HeaderName headerName,
                                          TableGeneralComponent.SortStatus sortType) {
        contentTablePageComponent.sortTable(headerName, sortType);
        softAssert.assertEquals(contentTablePageComponent.getColumnData(headerName.getStringValue()),
                contentTablePageComponent.getListByDirection(headerName.getStringValue(), sortType),
                headerName.getStringValue() + " column should be sorted properly, sort type is: " + sortType);
    }

    private void sortTableDateColumnsAndCheck(SoftAssert softAssert, ContentTablePageComponent.HeaderName headerName,
                                              TableGeneralComponent.SortStatus sortType) {
        contentTablePageComponent.sortTable(headerName, sortType);
        softAssert.assertEquals(contentTablePageComponent.getColumnData(headerName.getStringValue()),
                contentTablePageComponent.getDateListByDirection(
                        contentTablePageComponent.getFormatter(),
                        headerName.getStringValue(),
                        sortType), headerName.getStringValue() + " column should be sorted properly, sort type is: " + sortType);
    }
}
