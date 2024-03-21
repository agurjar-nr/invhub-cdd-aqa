package investigation.hub.test.core;

import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.core.util.Retry;
import investigation.hub.common.web.components.tables.InvestigationsTablePageComponent;
import investigation.hub.common.web.components.tables.InvestigationsTablePageComponent.HeaderName;
import investigation.hub.common.web.components.tables.TableGeneralComponent;
import investigation.hub.common.web.pages.NewUserPage;
import investigation.hub.common.web.pages.UsersPage;
import investigation.hub.common.web.test.data.dtos.UserDto;
import investigation.hub.common.web.test.data.repository.UserDtoRepository;
import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static investigation.hub.common.web.test.data.repository.UserDtoRepository.createAdminUserInstance;

public class AllOpenInvestigationsTest extends InvHubUiTest {

    InvestigationsTablePageComponent investigationsTablePageComponent;
    UsersPage usersAndRolesPage = new UsersPage();
    NewUserPage newUserPage = new NewUserPage();
    String subjectId;

    @BeforeMethod
    public void openPage() {
        open(apiProperties.getUrl() + apiProperties.getOpenInvestigations());
        openInvestigationsPage.waitForLoad();
        investigationsTablePageComponent = openInvestigationsPage
                .clickAllWorkButton()
                .getInvestigationsTable();
        subjectId = investigationsTablePageComponent
                .getRandomSubjectId();
    }

    @TmsLink("INVHUB-1079")
    @Test(retryAnalyzer = Retry.class, description = "Verify subjects can be sorted by 'Subject ID' column")
    public void checkSubjectIdColumnIsSortableTest() {
        SoftAssert softAssert = new SoftAssert();
        HeaderName headerName = HeaderName.SUBJECT_ID;
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.DESCENDING);
            softAssert.assertAll();
    }

    @TmsLink("INVHUB-1055")
    @Test(retryAnalyzer = Retry.class, description = "Verify subjects can be sorted by 'Subject Name' column")
    public void checkSubjectNameColumnIsSortableTest() {
        SoftAssert softAssert = new SoftAssert();
        HeaderName headerName = HeaderName.SUBJECT_NAME;
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1820")
    @Test(retryAnalyzer = Retry.class, description = "Verify subjects can be sorted by 'Assigned Investigator' column")
    public void checkAssignedInvestigatorColumnIsSortableTest() {
        SoftAssert softAssert = new SoftAssert();
        HeaderName headerName = HeaderName.ASSIGNED_INVESTIGATOR;
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1462")
    @Test(retryAnalyzer = Retry.class, description = "Verify subjects can be sorted by 'Subject Type' column")
    public void checkSubjectTypeColumnIsSortableTest() {
        SoftAssert softAssert = new SoftAssert();
        HeaderName headerName = HeaderName.SUBJECT_TYPE;
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1465")
    @Test(retryAnalyzer = Retry.class, description = "Verify subjects can be sorted by 'Contact Information' column")
    public void checkContactInformationColumnIsSortableTest() {
        SoftAssert softAssert = new SoftAssert();
        HeaderName headerName = HeaderName.CONTACT_INFORMATION;
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1466")
    @Test(retryAnalyzer = Retry.class, description = "Verify subjects can be sorted by 'Due Date' column")
    public void checkDueDateColumnIsSortableTest() {
        SoftAssert softAssert = new SoftAssert();
        HeaderName headerName = HeaderName.DUE_DATE;
        sortTableDateColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableDateColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1468")
    @Test(retryAnalyzer = Retry.class, description = "Verify subjects can be sorted by 'Tax Number' column")
    public void checkTaxNumberColumnIsSortableTest() {
        SoftAssert softAssert = new SoftAssert();
        HeaderName headerName = HeaderName.TAX_NUMBER;
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1080")
    @Test(retryAnalyzer = Retry.class, description = "Verify subjects can be filtered by Subject ID")
    public void checkSubjectsCanBeFilteredByIdTest() {
        HeaderName header = HeaderName.SUBJECT_ID;
        Map<HeaderName, String> investigation = openInvestigationsPage
                .getInvestigationsTable()
                .getRowValuesByHeaderName(header, subjectId);
        openInvestigationsPage.getInvestigationsTable()
                .getFilterPageComponent()
                .clickColumnFilterIcon(header.getStringValue())
                .enterFilterValue(header.getStringValue(), investigation.get(header));

        Assert.assertTrue(
                openInvestigationsPage.getInvestigationsTable()
                        .getColumnData(header.getStringValue()).stream()
                        .allMatch(it -> it.equals(investigation.get(header))),
                "Subjects should be filtered by 'Subject ID' correctly");
    }

    @TmsLink("INVHUB-1472")
    @Test(retryAnalyzer = Retry.class, description = "Verify subjects can be filtered by Subject Name")
    public void checkSubjectsCanBeFilteredByNameTest() {
        HeaderName header = HeaderName.SUBJECT_NAME;
        Map<HeaderName, String> investigation = openInvestigationsPage
                .getInvestigationsTable()
                .getRowValuesByHeaderName(HeaderName.SUBJECT_ID, subjectId);
        openInvestigationsPage.getInvestigationsTable()
                .getFilterPageComponent()
                .clickColumnFilterIcon(header.getStringValue())
                .enterFilterValue(header.getStringValue(), investigation.get(header));

        Assert.assertTrue(
                openInvestigationsPage.getInvestigationsTable()
                        .getColumnData(header.getStringValue()).stream()
                        .allMatch(it -> it.equals(investigation.get(header))),
                "Subjects should be filtered by 'Subject Name' correctly");
    }

    @TmsLink("INVHUB-1474")
    @Issue("INVHUB-2832")
    @Test(retryAnalyzer = Retry.class, description = "Verify subjects can be filtered by Assigned Investigator")
    public void checkSubjectsCanBeFilteredByAssignedInvestigatorTest() {
        HeaderName header = HeaderName.ASSIGNED_INVESTIGATOR;
        Map<HeaderName, String> investigation = openInvestigationsPage
                .getInvestigationsTable()
                .getRowValuesByHeaderName(HeaderName.SUBJECT_ID, subjectId);

        String assignedInvestigator = investigation.get(header);
        if ("Test User (Me)".equals(assignedInvestigator)) {
            assignedInvestigator = assignedInvestigator.replaceAll("\\(.*?\\)", "").trim();
        }

        openInvestigationsPage.getInvestigationsTable()
                .getFilterPageComponent()
                .clickColumnFilterIcon(header.getStringValue())
                .selectFilterQueries(Collections.singletonList(assignedInvestigator));

        Assert.assertTrue(
                openInvestigationsPage.getInvestigationsTable()
                        .getColumnData(header.getStringValue()).stream()
                        .allMatch(it -> it.equals(investigation.get(header))),
                "Subjects should be filtered by 'Priority' correctly");
    }

    @TmsLink("INVHUB-1473")
    @Test(retryAnalyzer = Retry.class, description = "Verify subjects can be filtered by Subject Type")
    public void checkSubjectsCanBeFilteredByTypeTest() {
        HeaderName header = HeaderName.SUBJECT_TYPE;
        Map<HeaderName, String> investigation = openInvestigationsPage
                .getInvestigationsTable()
                .getRowValuesByHeaderName(HeaderName.SUBJECT_ID, subjectId);
        openInvestigationsPage.getInvestigationsTable()
                .getFilterPageComponent()
                .clickColumnFilterIcon(header.getStringValue())
                .selectFilterQueries(Collections.singletonList(investigation.get(header)));

        Assert.assertTrue(
                openInvestigationsPage.getInvestigationsTable()
                        .getColumnData(header.getStringValue()).stream()
                        .allMatch(it -> it.equals(investigation.get(header))),
                "Subject should be filtered by 'Subject Type' correctly");
    }

    @TmsLink("INVHUB-1475")
    @Test(retryAnalyzer = Retry.class, description = "Verify subjects can be filtered by Contact Information")
    public void checkSubjectsCanBeFilteredByContactInformationTest() {
        HeaderName header = HeaderName.CONTACT_INFORMATION;
        Map<HeaderName, String> investigation = openInvestigationsPage
                .getInvestigationsTable()
                .getRowValuesByHeaderName(HeaderName.SUBJECT_ID, subjectId);
        openInvestigationsPage.getInvestigationsTable()
                .getFilterPageComponent()
                .clickColumnFilterIcon(header.getStringValue())
                .enterFilterValue(header.getStringValue(), investigation.get(header));

        Assert.assertTrue(
                openInvestigationsPage.getInvestigationsTable()
                        .getColumnData(header.getStringValue()).stream()
                        .allMatch(it -> it.equals(investigation.get(header))),
                "Subjects should be filtered by 'Contact Information' correctly");
    }

    @TmsLink("INVHUB-1476")
    @Test(retryAnalyzer = Retry.class, description = "Verify subjects can be filtered by Tax Number")
    public void checkSubjectsCanBeFilteredByTaxNumberTest() {
        HeaderName header = HeaderName.TAX_NUMBER;
        Map<HeaderName, String> investigation = openInvestigationsPage
                .getInvestigationsTable()
                .getRowValuesByHeaderName(HeaderName.SUBJECT_ID, subjectId);
        openInvestigationsPage.getInvestigationsTable()
                .getFilterPageComponent()
                .clickColumnFilterIcon(header.getStringValue())
                .enterFilterValue(header.getStringValue(), investigation.get(header));

        Assert.assertTrue(
                openInvestigationsPage.getInvestigationsTable()
                        .getColumnData(header.getStringValue()).stream()
                        .allMatch(it -> it.equals(investigation.get(header))),
                "Subjects should be filtered by 'Tax Number' correctly");
    }

    @TmsLink("INVHUB-1478")
    @Test(retryAnalyzer = Retry.class, description = "Verify subjects can be filtered by Due Date")
    public void checkSubjectsCanBeFilteredByDueDateTest() {
        HeaderName header = HeaderName.DUE_DATE;
        Map<HeaderName, String> investigation = openInvestigationsPage
                .getInvestigationsTable()
                .getRowValuesByHeaderName(HeaderName.SUBJECT_ID, subjectId);
        openInvestigationsPage.getInvestigationsTable()
                .getFilterPageComponent()
                .clickColumnFilterIcon(header.getStringValue())
                .enterFilterDateValue(investigation.get(header), investigation.get(header),
                        openInvestigationsPage.getInvestigationsTable().getFormatter());

        Assert.assertTrue(
                openInvestigationsPage.getInvestigationsTable()
                        .getColumnData(header.getStringValue()).stream()
                        .allMatch(it -> it.equals(investigation.get(header))),
                "Subjects should be filtered by 'Due Date' correctly");
    }

    @TmsLink("INVHUB-2333")
    @Test(enabled = false, description = "Verify subjects can be sorted by 'Financial Crime Types' column")
    public void checkFinancialCrimeTypesColumnIsSortableTest() {
        SoftAssert softAssert = new SoftAssert();
        HeaderName headerName = HeaderName.FINANCIAL_CRIME_TYPES;
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1469")
    @Test(retryAnalyzer = Retry.class, description = "Verify subjects can be sorted by 'Organisation Unit' column")
    public void checkOrganisationUnitColumnIsSortableTest() {
        SoftAssert softAssert = new SoftAssert();
        HeaderName headerName = HeaderName.ORGANISATION_UNIT;
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    //BUG - to clarify
    @TmsLink("INVHUB-1470")
    @Test(retryAnalyzer = Retry.class, description = "Verify subjects can be sorted by 'Primary Activity/Occupation' column")
    public void checkPrimaryActivityOccupationColumnIsSortableTest() {
        SoftAssert softAssert = new SoftAssert();
        HeaderName headerName = HeaderName.PRIMARY_ACTIVITY_OCCUPATION;
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.ASCENDING);
        sortTableColumnsAndCheck(softAssert, headerName, TableGeneralComponent.SortStatus.DESCENDING);
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-2334")
    @Test(retryAnalyzer = Retry.class, description = "Verify subjects can be filtered by Financial Crime Types")
    public void checkSubjectsCanBeFilteredByFinancialCrimeTypesTest() {
        HeaderName  header = HeaderName.FINANCIAL_CRIME_TYPES;
        Map<HeaderName, String> investigation = openInvestigationsPage
                .getInvestigationsTable()
                .getRowValuesByHeaderName(HeaderName.SUBJECT_ID, subjectId);
        openInvestigationsPage.getInvestigationsTable()
                .getFilterPageComponent()
                .clickColumnFilterIcon(header.getStringValue())
                .selectFilterQueries(Collections.singletonList(investigation.get(header)));

        Assert.assertTrue(
                openInvestigationsPage.getInvestigationsTable()
                        .getColumnData(header.getStringValue()).stream()
                        .allMatch(it -> it.equals(investigation.get(header))),
                "Subject should be filtered by 'Financial Crime Types' correctly");
    }

    @TmsLink("INVHUB-1479")
    @Test(retryAnalyzer = Retry.class, description = "Verify subjects can be filtered by Organisation Unit")
    public void checkSubjectsCanBeFilteredByOrganisationUnitTest() {
        HeaderName header = HeaderName.ORGANISATION_UNIT;
        Map<HeaderName, String> investigation = openInvestigationsPage
                .getInvestigationsTable()
                .getRowValuesByHeaderName(HeaderName.SUBJECT_ID, subjectId);
        openInvestigationsPage.getInvestigationsTable()
                .getFilterPageComponent()
                .clickColumnFilterIcon(header.getStringValue())
                .selectFilterQueries(Collections.singletonList(investigation.get(header)));

        Assert.assertTrue(
                openInvestigationsPage.getInvestigationsTable()
                        .getColumnData(header.getStringValue()).stream()
                        .allMatch(it -> it.equals(investigation.get(header))),
                "Subject should be filtered by 'Organisation Unit' correctly");
    }

    @TmsLink("INVHUB-1480")
    @Test(retryAnalyzer = Retry.class, description = "Verify subjects can be filtered by Primary Activity/Occupation")
    public void checkSubjectsCanBeFilteredByPrimaryActivityOccupationTest() {
        HeaderName header = HeaderName.PRIMARY_ACTIVITY_OCCUPATION;
        Map<HeaderName, String> investigation = openInvestigationsPage
                .getInvestigationsTable()
                .getRowValuesByHeaderName(HeaderName.SUBJECT_ID, subjectId);
        openInvestigationsPage.getInvestigationsTable()
                .getFilterPageComponent()
                .clickColumnFilterIcon(header.getStringValue())
                .enterFilterValue(header.getStringValue(), investigation.get(header));

        Assert.assertTrue(
                openInvestigationsPage.getInvestigationsTable()
                        .getColumnData(header.getStringValue()).stream()
                        .allMatch(it -> it.equals(investigation.get(header))),
                "Subjects should be filtered by 'Primary Activity/Occupation' correctly");
    }

    @TmsLink("INVHUB-2332")
    @Test(retryAnalyzer = Retry.class, description = "Verify investigation can be assigned to user")
    public void checkInvestigationCanBeAssignedToUserTest() {
        SoftAssert softAssert = new SoftAssert();
        HashMap<String, String> investigation = new HashMap<>();
        investigation.put(HeaderName.SUBJECT_ID.getStringValue(), subjectId);
        openInvestigationsPage.getInvestigationsTable().selectCheckboxByRowData(investigation);
        openInvestigationsPage.clickAssignButton()
                .assignInvestigationToUser(UserDtoRepository.createTestUserInstance().getFullName())
                .clickSaveButton();
        investigation.put(HeaderName.ASSIGNED_INVESTIGATOR.getStringValue(),
                "%s (Me)".formatted(UserDtoRepository.createTestUserInstance().getFullName()));
        softAssert.assertTrue(openInvestigationsPage.isUserAssignedToOpenInvestigationMessageAppeared(),
                "'User assigned to open investigation.' message should appear");
        openInvestigationsPage.clickMyWorkButton();
        softAssert.assertNotNull(openInvestigationsPage.getInvestigationsTable().getRowByContent(investigation),
                "Investigation with subject id: %s with assigned user should be present in the table".formatted(
                        investigation.get(HeaderName.SUBJECT_ID.getStringValue())));
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-1240")
    @Test(retryAnalyzer = Retry.class, description = "Verify investigation can be unassigned")
    public void checkInvestigationCanBeUnassignedTest() {
        SoftAssert softAssert = new SoftAssert();
        HashMap<String, String> investigation = new HashMap<>();
        investigation.put(HeaderName.SUBJECT_ID.getStringValue(), subjectId);
        openInvestigationsPage.getInvestigationsTable().selectCheckboxByRowData(investigation);
        investigation.put(HeaderName.ASSIGNED_INVESTIGATOR.getStringValue(),
                "Unassigned");
        openInvestigationsPage.clickAssignButton()
                .clickSaveButton();
        softAssert.assertTrue(openInvestigationsPage.isUserAssignedToOpenInvestigationMessageAppeared(),
                "'User assigned to open investigation.' message should appear");
        softAssert.assertNotNull(openInvestigationsPage.getInvestigationsTable().getRowByContent(investigation),
                "Unassigned investigation should be present in the table");
        softAssert.assertAll();
    }

    @TmsLink("INVHUB-2327")
    @Test(retryAnalyzer = Retry.class, description = "Verify investigation assigned to other user is not visible in 'My Work' tab table")
    public void checkInvestigationAssignedToOtherUserIsNotVisibleInMyWorkTabTableTest() {
        SoftAssert softAssert = new SoftAssert();
        UserDto userDto = createAdminUserInstance();
        usersAndRolesPage = openInvestigationsPage
                .getMainHeaderComponent()
                .openUsers();
        createUserWithData(userDto);
        HashMap<String, String> investigation = new HashMap<>();
        investigation.put(HeaderName.SUBJECT_ID.getStringValue(), subjectId);
        openInvestigationsPage.getMainHeaderComponent()
                .openAllOpenInvestigations()
                .clickAllWorkButton();
        openInvestigationsPage.getInvestigationsTable()
                .selectCheckboxByRowData(investigation);
        openInvestigationsPage.clickAssignButton()
                .assignInvestigationToUser(UserDtoRepository.createTestUserInstance().getFullName())
                .clickSaveButton();
        investigation.put(HeaderName.ASSIGNED_INVESTIGATOR.getStringValue(),
                UserDtoRepository.createTestUserInstance().getFullName());
        softAssert.assertTrue(openInvestigationsPage.isUserAssignedToOpenInvestigationMessageAppeared(),
                "'User assigned to open investigation.' message should appear");
        Assert.assertNull(openInvestigationsPage.clickMyWorkButton().getInvestigationsTable().getRowByContent(investigation),
                "Investigation with assigned user should be present in 'My work' tab table");
        softAssert.assertAll();
    }

    private void createUserWithData(UserDto userDto) {
        usersAndRolesPage
                .clickCreateNewUserButton()
                .getPersonalDetails()
                .enterFirstName(userDto.getFirstName())
                .enterLastName(userDto.getLastName())
                .clickUploadButton()
                .uploadPhoto(userDto.getPhoto())
                .clickUpload1FileButton()
                .clickDoneButton()
                .enterEmail(userDto.getEmail())
                .clickLanguageDropdown()
                .selectLanguage(userDto.getLanguage())
                .clickWeekStarsOnDropdown()
                .selectWeekStartsOnDay(userDto.getWeekStartsOn())
                .clickTimeZoneDropdown()
                .selectTimeZone(userDto.getTimeZone());
        newUserPage.clickSaveButton();
    }

    private void sortTableColumnsAndCheck(SoftAssert softAssert, HeaderName headerName,
                                          TableGeneralComponent.SortStatus sortType) {
        openInvestigationsPage
                .getInvestigationsTable()
                .sortTable(headerName, sortType);
        softAssert.assertEquals(openInvestigationsPage.getInvestigationsTable()
                        .getColumnData(headerName.getStringValue()),
                openInvestigationsPage.getInvestigationsTable()
                        .getListByDirection(headerName.getStringValue(), sortType),
                headerName.getStringValue() + " column should be sorted properly, sort type is: " + sortType);
    }

    private void sortTableDateColumnsAndCheck(SoftAssert softAssert, HeaderName headerName,
                                              TableGeneralComponent.SortStatus sortType) {
        InvestigationsTablePageComponent table = openInvestigationsPage.getInvestigationsTable();

        table
                .sortTable(headerName, sortType);
        softAssert.assertEquals(table.getColumnData(headerName.getStringValue()),
                openInvestigationsPage.getInvestigationsTable().getDateListByDirection(
                        table.getFormatter(),
                        headerName.getStringValue(),
                        sortType),
                headerName.getStringValue() + " column should be sorted properly, sort type is: " + sortType);
    }
}
