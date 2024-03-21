package investigation.hub.common.web.components.tables;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.PageComponent;
import investigation.hub.common.web.components.FilterPageComponent;
import investigation.hub.common.web.enums.InvHubDateFormat;
import investigation.hub.common.web.pages.InvestigationPage;
import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Log4j2
@PageComponent
public class InvestigationsTablePageComponent
        extends TableGeneralComponent<InvestigationsTablePageComponent.HeaderName> {

    private final FilterPageComponent filterPageComponent = new FilterPageComponent();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            InvHubDateFormat.DAY_MONTH_YEAR.getFormatterPattern()).withLocale(Locale.US);

    public List<String> getAllHeadersNames() {
        return super.getAllHeadersNames(By.cssSelector("th div span[role=button]"));
    }

    @Step
    public InvestigationPage clickRowBySubjectId(String subjectId) {
        SelenideElement rowBySubjectId = super
                .getRowByHeaderAndValue(HeaderName.SUBJECT_ID.getStringValue(), subjectId);

        rowBySubjectId
                .$("a")
                .click();

        log.info("Click Open Investigation by subjectId: " + subjectId);
        return new InvestigationPage();
    }

    /*
     * Workaround if there is a couple of subjects with the same ID, but different names
     * */
    @Step
    public InvestigationPage clickRowBySubjectName(String subjectName) {
        SelenideElement rowBySubjectName = super.getRowByHeaderAndValue(
                HeaderName.SUBJECT_NAME.getStringValue(), subjectName);

        rowBySubjectName
                .$("a")
                .click();

        log.info("Click Open Investigation by subjectName: " + subjectName);
        return new InvestigationPage();
    }


    /*
     * Returns map that represents table row and contains key - name of header, value - value of row cell
     * */
    public Map<HeaderName, String> getRowValuesByHeaderName(HeaderName headerName, String cellValue) {
        SelenideElement rowByHeaderValue = super.getRowByHeaderAndValue(headerName.getStringValue(), cellValue);

        List<String> cellValues = rowByHeaderValue
                .findAll("td")
                .asDynamicIterable()
                .stream()
                .map(SelenideElement::getText)
                .toList();

        List<HeaderName> headerValues = Arrays.asList(HeaderName.values());

        return IntStream
                .range(0, headerValues.size())
                .boxed()
                .collect(Collectors.toMap(headerValues::get, cellValues::get));
    }

    @Step("User sorts Investigations table by column")
    public void sortTable(HeaderName headerName, SortStatus sortStatus) {
        SelenideElement header = getHeaderElementByName(headerName.getStringValue());
        sortTableWithAriaLabel(header, sortStatus.getAriaLabel());
    }

    public String getRandomSubjectId() {
        return getAnyColumnValue(HeaderName.SUBJECT_ID);
    }

    @Step("User selects checkbox in Investigations table by row data")
    public InvestigationPage selectCheckboxByRowData(Map<String, String> rowToFind) {
        getRowByContent(rowToFind).$("td[class='checkboxColumn'] label")
                .as("Select row icon")
                .click();
        return new InvestigationPage();
    }

    @Getter
    @AllArgsConstructor
    public enum HeaderName implements GeneralTableHeaderName {

        SELECT_ALL("Select All"),
        SUBJECT_ID("Subject ID"),
        SUBJECT_NAME("Subject Name"),
        SUBJECT_TYPE("Subject Type"),
        FINANCIAL_CRIME_TYPES("Financial Crime Types"),
        ASSIGNED_INVESTIGATOR("Assigned Investigator"),
        CONTACT_INFORMATION("Contact Information"),
        TAX_NUMBER("Tax Number"),
        DUE_DATE("Due Date"),
        ORGANISATION_UNIT("Organisation Unit"),
        PRIMARY_ACTIVITY_OCCUPATION("Primary Activity/Occupation");

        public final String stringValue;
    }
}
