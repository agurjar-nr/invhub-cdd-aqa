package investigation.hub.common.web.components.tables;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.smile.components.GeneralComponent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openqa.selenium.By;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@GeneralComponent
public abstract class TableGeneralComponent<T extends Enum<T> & TableGeneralComponent.GeneralTableHeaderName> {

    protected List<String> primaryList;
    SelenideElement header;
    SelenideElement body;

    public TableGeneralComponent() {
        header = $("thead");
        body = $("tbody");
    }

    public TableGeneralComponent(SelenideElement header, SelenideElement body) {
        this.header = header;
        this.body = body;
    }

    public int getAllRowsNumber() {
        return getAllRows()
                .asDynamicIterable()
                .stream()
                .filter(row -> !row.$("tr > td").getText().equals("No Data Found"))
                .toList()
                .size();
    }

    protected SelenideElement getRowByIndex(int index) {
        if (getAllRowsNumber() <= index) {
            throw new IllegalArgumentException("A row with index " + index + " doesn't exist!");
        }

        return getAllRows()
                .get(index);
    }

    protected SelenideElement getRowByHeaderAndValue(String headerName, String value) {
        ElementsCollection headerCells = header.$$("th");
        System.out.println("hedcells"+ headerCells);
        int cellIndex;

        Optional<SelenideElement> headerElement = headerCells
                .asDynamicIterable()
                .stream()
                .filter(headerCell -> headerCell.innerText().contains(headerName))
                .findFirst();

        if (headerElement.isPresent()) {
            cellIndex = headerCells.asDynamicIterable().stream().toList().indexOf(headerElement.get());
        } else {
            throw new RuntimeException("There is no such header " + headerName);
        }
        for (SelenideElement line : body.$$("tr")) {
            String text = line
                    .$$("td")
                    .get(cellIndex)
                    .shouldBe(visible)
                    .getText();
            if (value.equals(text)) {
                return line;
            }
        }
        throw new RuntimeException("There is no line with expected value '" + value + "' by header '" + headerName + "'");
    }

    public SelenideElement getRowByContent(Map<String, String> rowToFind) {
        ElementsCollection elements = body.$$("tr");
        List<String> columns = header.$$("th").asDynamicIterable()
                .stream()
                .map(it -> it.getText().trim())
                .toList();
        Optional<SelenideElement> foundRow = elements.asDynamicIterable()
                .stream()
                .filter(rowElement -> {
                    Map<String, String> row = rowToMap(columns, rowElement);
                    List<String> headers =
                            row.keySet().stream()
                                    .filter(key -> key.contains("\n"))
                                    .toList();
                    headers.forEach(header -> {
                        String fixedHeaderName = header.substring(0, header.indexOf("\n"));
                        row.put(fixedHeaderName, row.remove(header));
                    });
                    return row.entrySet().containsAll(rowToFind.entrySet());
                }).findFirst();

        return foundRow.orElse(null);
    }

    protected Map<String, String> rowToMap(List<String> columns, SelenideElement row) {
        List<String> cells = row.$$(By.tagName("td")).asDynamicIterable()
                .stream()
                .map(it -> it.getText().trim())
                .toList();
        return IntStream.range(0, columns.size())
                .boxed()
                .collect(Collectors.toMap(columns::get, cells::get));
    }

    protected List<String> getAllHeadersNames(By locator) {
        return header
                .$$(locator)
                .as("All headers names of a table")
                .asDynamicIterable()
                .stream()
                .map(SelenideElement::getText)
                .filter(s -> !s.isEmpty())
                .map(s -> s.split("\n")[0])
                .toList();
    }

    public List<String> getAllHeadersNames() {
        return getAllHeadersNames(By.cssSelector("th"));
    }

    public ElementsCollection getAllRows() {
        return body
                .$$("tr")
                .as("All rows in a table");
    }

    protected SelenideElement getHeaderElementByName(String headerName) {
        return header
                .$x(".//th//*[contains(text(),'" + headerName + "')]/ancestor::th")
                .as("Header element by text " + headerName);
    }

    public List<String> getListByDirection(String column, SortStatus sortType) {
        primaryList = getColumnData(column);
        if (SortStatus.isAsc(sortType)) {
            return primaryList.stream().sorted(Comparator.comparing(String::toString)).toList();
        } else if (SortStatus.isDesc(sortType)) {
            return primaryList.stream().sorted(Comparator.comparing(String::toString).reversed()).toList();
        }
        return primaryList;
    }

    public List<Double> getNumericListByDirection(List<Double> primaryNumericList, SortStatus sortType) {
        if (SortStatus.isAsc(sortType)) {
            return primaryNumericList.stream().sorted(Comparator.naturalOrder()).toList();
        } else if (SortStatus.isDesc(sortType)) {
            return primaryNumericList.stream().sorted(Comparator.reverseOrder()).toList();
        }
        return primaryNumericList;
    }

    public List<Double> getNumericColumnData(String columnName) {
        return getColumnData(columnName).stream().map(s -> Double.valueOf(s.replaceAll("[^\\d.]", "")))
                .collect(Collectors.toList());
    }

    public List<String> getColumnData(String columnName) {
        System.out.println("header------->"+header);
        int index = header.$$("th").asDynamicIterable()
                .stream()
                .map(it -> it.getText().replaceAll("\n\\d+", ""))
                .toList()
                .indexOf(columnName) + 1;
        return body.$$("tr > td:nth-of-type(%d)".formatted(index))
                .asDynamicIterable()
                .stream()
                .map(element -> element.getText().trim())
                .filter(text -> !text.equals("No Data Found") && !text.isEmpty())
                .toList();
    }

       public String getAnyColumnValue(T headerName) {
        String header = headerName.getStringValue();
        System.out.println("Heade---->"+header);
        List<String> values = new ArrayList<>(getColumnData(header));
        Collections.shuffle(values);
        return values
                .stream()
                .findAny()
                .orElseThrow(() -> new RuntimeException("No values are found for the '%s' column!".formatted(header)));
    }

    protected void sortTableWithAriaLabel(SelenideElement header, String sortStatus) {
        String sortDirection;
        int retry = 1;
        do {
            header.$("span[role$='button']")
                    .as("Clickable header")
                    .shouldBe(enabled)
                    .click();
            sortDirection = header.$("span.dn-header-sorting span")
                    .as("Sort direction icon")
                    .getAttribute("aria-label");
            if (retry++ > 3) {
                throw new RuntimeException("Could not get correct sorting arrow direction");
            }
        } while (sortDirection != null && !sortDirection.equals(sortStatus));
    }

    protected void sortTableWithDataTestid(SelenideElement header, String sortStatus) {
        String sortDirection;
        int retry = 1;
        setNoArrow(header);
        do {
            header.click();
            sortDirection = header
                    .$x(".//span/*")
                    .as("Sort direction area")
                    .getAttribute("data-testid");
            if (retry++ > 2) {
                throw new RuntimeException("Could not get correct sorting arrow direction");
            }
        } while (!sortStatus.equals(sortDirection));
    }

    private void setNoArrow(SelenideElement header) {
        SelenideElement arrow = header.$("span.flex");
        for (boolean exist = arrow.exists(); exist; exist = arrow.exists()) {
            header.click();
        }
    }

    public List<String> getDateListByDirection(DateTimeFormatter formatter,
                                               String headerName,
                                               SortStatus sortType) {
        primaryList = getColumnData(headerName);
        if (formatter.toString().contains("HourOfDay")) {
            if (SortStatus.isAsc(sortType)) {
                return sortDateTimePrimaryList(formatter, Comparator.naturalOrder());
            } else if (SortStatus.isDesc(sortType)) {
                return sortDateTimePrimaryList(formatter, Comparator.reverseOrder());
            }
        } else {
            if (SortStatus.isAsc(sortType)) {
                return sortDatePrimaryList(formatter, Comparator.naturalOrder());
            } else if (SortStatus.isDesc(sortType)) {
                return sortDatePrimaryList(formatter, Comparator.reverseOrder());
            }
        }
        return primaryList;
    }

    private List<String> sortDatePrimaryList(DateTimeFormatter formatter, Comparator<? super LocalDate> comparator) {
        return primaryList
                .stream()
                .map(s -> LocalDate.parse(s, formatter))
                .sorted(comparator)
                .map(ld -> ld.format(formatter))
                .toList();
    }

    private List<String> sortDateTimePrimaryList(DateTimeFormatter formatter, Comparator<? super LocalDateTime> comparator) {
        return primaryList
                .stream()
                .map(s -> LocalDateTime.parse(s, formatter))
                .sorted(comparator)
                .map(ldt -> ldt.format(formatter))
                .toList();
    }

    public TableGeneralComponent<T> waitForTableContent() {
        SelenideElement loadingDots = body.$(".rcs-ellipsis");
        try {
            loadingDots.shouldBe(visible, Duration.ofMillis(1000));
        } catch (com.codeborne.selenide.ex.ElementNotFound ex) {
            //no loading dots
            return this;
        }
        loadingDots.shouldBe(Condition.hidden, Duration.ofMillis(3000));
        return this;
    }

    @Getter
    @AllArgsConstructor
    public enum SortStatus {
        ASCENDING("ArrowUpwardIcon", "Sort arrow ascending"),
        DESCENDING("ArrowDownwardIcon", "Sort arrow descending");
        private final String dataTestId;
        private final String ariaLabel;

        public static boolean isDesc(SortStatus sortingStatus) {
            return DESCENDING.equals(sortingStatus);
        }

        public static boolean isAsc(SortStatus sortingStatus) {
            return ASCENDING.equals(sortingStatus);
        }
    }

    public interface GeneralTableHeaderName {
        String getStringValue();
    }
}
