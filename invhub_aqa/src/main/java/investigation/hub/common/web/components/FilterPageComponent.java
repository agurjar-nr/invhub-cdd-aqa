package investigation.hub.common.web.components;

import com.codeborne.selenide.SelenideElement;
import com.smile.components.PageComponent;
import investigation.hub.common.web.enums.InvHubDateFormat;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;

@Log4j2
@PageComponent
public class FilterPageComponent {
    private final DateTimeFormatter filterModalFormatter = DateTimeFormatter.ofPattern(
            InvHubDateFormat.MONTH_DAY_YEAR.getFormatterPattern());
    private final SelenideElement container = $("thead tr");

    @Step("User clicks filter button by column")
    public FilterPageComponent clickColumnFilterIcon(String headerName) {
        container
                .$x(".//div[./span[@title='%s']]//button".formatted(headerName))
                .as("Content filter icon")
                .click();
        log.info("Click filter icon");
        return this;
    }

    @Step("User enters filter input")
    public FilterPageComponent enterFilterValue(String headerName, String value) {
        SelenideElement filterInput = container
                .$("input[aria-label*='%s'], input[aria-labelledby*='%s']".formatted(headerName, headerName))
                .as("Filter input area");
        filterInput.sendKeys(value);
        filterInput.pressEnter();
        log.info("Filter column data by value");
        return this;
    }

    @Step("User selects filter queries")
    public FilterPageComponent selectFilterQueries(List<String> filterQueries) {
        filterQueries
                .forEach(query -> {
                    container
                            .$x(".//div[contains(@class, 'dn-multi-select__indicator')]")
                            .as("Arrow to expand filter dropdown list")
                            .click();
                    container
                            .$x(".//div[contains(text(),'" + query + "')]")
                            .as("Filter dropdown option by text: " + query)
                            .click();
                });

        container
                .$x(".//input[@class='dn-multi-select__input']")
                .as("Input with chosen filters")
                .pressEnter();
        log.info("Filter column data by select menu");
        return this;
    }

    @Step("User selects filter queries")
    public FilterPageComponent selectFilterQuery(String query) {


                    container
                            .$x(".//div[contains(@class, 'dn-multi-select__indicator')]")
                            .as("Arrow to expand filter dropdown list")
                            .click();
                    container
                            .$x(".//div[contains(text(),'" + query + "')]")
                            .as("Filter dropdown option by text: " + query)
                            .click();


        container
                .$x(".//input[@class='dn-multi-select__input']")
                .as("Input with chosen filters")
                .pressEnter();
        log.info("Filter column data by select menu");
        return this;
    }


    @Step("User enters filter date input")
    public FilterPageComponent enterFilterDateValue(String fromValue, String toValue, DateTimeFormatter format) {
        container
                .$x(".//input[contains(@aria-labelledby,'- From')]")
                .as("Date From input")
                .sendKeys(formatDate(fromValue, format));
        container
                .$x(".//input[contains(@aria-labelledby,'- To')]")
                .as("Date To input")
                .sendKeys(formatDate(toValue, format));
        container
                .$x(".//input[@class='form-control input.to.placeholder']")
                .as("Input with chosen filters")
                .pressEnter();
        log.info("Filter column data by date");
        return this;
    }

    private String formatDate(String date, DateTimeFormatter formatter) {
        LocalDate datetime = LocalDate.parse(date, formatter);
        return datetime.format(filterModalFormatter);
    }

    /**
     * This method is to be used for dates only without time, like 09/09/2023
     */
    public boolean isDateColumnFiltered(List<String> columnData,
                                        String startDate,
                                        String endDate,
                                        DateTimeFormatter formatter) {
        if (columnData.isEmpty()) {
            return false;
        }

        LocalDate startLocalDate = LocalDate.parse(startDate, formatter);
        LocalDate endLocalDate = LocalDate.parse(endDate, formatter);

        return columnData
                .stream()
                .map(date -> LocalDate.parse(date, formatter))
                .allMatch(date ->
                        (date.isEqual(startLocalDate) || date.isAfter(startLocalDate)) &&
                                (date.isEqual(endLocalDate) || date.isBefore(endLocalDate)));
    }

    /**
     * This method is to be used for dates with time, like 01/01/2023, 10:20:30
     */
    public boolean isDateTimeColumnFiltered(List<String> columnData,
                                            String startDate,
                                            String endDate,
                                            DateTimeFormatter formatter) {
        if (columnData.isEmpty()) {
            return false;
        }

        LocalDateTime startLocalDate = LocalDateTime.parse(startDate, formatter);
        LocalDateTime endLocalDate = LocalDateTime.parse(endDate, formatter);

        return columnData.size() == columnData
                .stream()
                .filter(p -> LocalDateTime.parse(p, formatter)
                        .isAfter(LocalDate.parse(startLocalDate.format(filterModalFormatter), filterModalFormatter)
                                .atStartOfDay()) && LocalDateTime.parse(p, formatter)
                        .isBefore(LocalDate.parse(endLocalDate.format(filterModalFormatter), filterModalFormatter)
                                .atStartOfDay().plusDays(1))).toList().size();
    }


}
