package investigation.hub.common.web.components.tables;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.smile.components.PageComponent;
import investigation.hub.common.web.components.FilterPageComponent;
import investigation.hub.common.web.components.KYCCDDPanelPageComponent;
import investigation.hub.common.web.components.WebSearchPageComponent;
import investigation.hub.common.web.enums.InvHubDateFormat;
import investigation.hub.common.web.test.data.dtos.Result;
import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.JavascriptExecutor;

import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static java.lang.String.format;

@Getter
@Log4j2
@PageComponent
public class KYCCDDPanelTablePageComponent<T extends Enum<T> & TableGeneralComponent.GeneralTableHeaderName>
        extends TableGeneralComponent<T> {
    private final FilterPageComponent filterPageComponent = new FilterPageComponent();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            InvHubDateFormat.MONTH_DAY_YEAR.getFormatterPattern()).withLocale(Locale.US);
    private final SelenideElement container = $("thead tr");

    private final SelenideElement sort = $x("//div/span[@role='button']");
    SelenideElement body;

    public KYCCDDPanelTablePageComponent() {
        body = $("tbody");
    }


    @Step("User sorts KYC/CDD table by column")
    public KYCCDDPanelTablePageComponent sortTable(KYCCDDPanelTablePageComponent.PanelName panelName, KYCCDDPanelTablePageComponent.HeaderName headerName,
                                                   TableGeneralComponent.SortStatus sortStatus) {

        SelenideElement headerKYC = getKYCHeaderElementByName(panelName.getStringValue(), headerName.getStringValue());
       // scroll(headerKYC);
        log.info("Sort KYC table by column: " + headerName.getStringValue());
      //  sortTableWithAriaLabel(headerKYC, sortStatus.getAriaLabel());
        sortTableWithAriaLabel1(panelName,headerName, sortStatus.getAriaLabel());

        return new KYCCDDPanelTablePageComponent();
    }

    public void scroll(SelenideElement element) {
        ((JavascriptExecutor) getWebDriver()).executeScript("arguments[0].scrollIntoView(true);", element);

    }

    @Step("User get random column value")
    public String getAnyColumnValueKYC(KYCCDDPanelTablePageComponent.PanelName panelName, T headerName) {
        String header = $x(".//div//*[@id='" + panelName.getStringValue() + "-panel-header']//parent::div//th//*[contains(text(),'" + headerName.getStringValue() + "')]").getText();
     //   List<String> values = new ArrayList<>(getColumnData(panelName, header));getKYCColumnData
        List<String> values = new ArrayList<>(getKYCColumnData(panelName, header));
        Collections.shuffle(values);
        log.info("Any random value");
        return values
                .stream()
                .findAny()
                .orElseThrow(() -> new RuntimeException("No values are found for the '%s' column!".formatted(header)));

    }

    public SelenideElement panel(KYCCDDPanelTablePageComponent.PanelName panelName) {
        return $x(".//div//*[@id='" + panelName.getStringValue() + "-panel-header']//parent::div");
    }

    @Step("User check panel availability")
    public boolean checkPanelAvailability(KYCCDDPanelTablePageComponent.PanelName panelName) {
        log.info("Check panel availability");
        return panel(panelName).isDisplayed();
    }

    @Step("Choose header index")
    public int getHeaderIndex(KYCCDDPanelTablePageComponent.PanelName panelName, String name) {
        ElementsCollection headers = panel(panelName).$$x(".//th/div/span");
        int hindex = 0;
        int headerIndex = 0;
        for (hindex = 0; hindex < headers.size(); hindex++) {
            String text = headers.get(hindex).getText();
            if (text.equals(name)) {
                headerIndex = hindex + 1;
            }
        }
        log.info("Get header Index");
        return headerIndex;
    }

        @Step("User gets column list")
    public List<String> getKYCColumnData(KYCCDDPanelTablePageComponent.PanelName panelName, String columnName) {
        ElementsCollection ele = $x(".//div//*[@id='" + panelName.getStringValue() + "-panel-header']//parent::div//th/div/span/ancestor::table").$$x("./tbody/tr/td["+ getHeaderIndex(panelName,columnName)+"]");
       // ElementsCollection ele = $x(".//div//*[@id='" + panelName.getStringValue() + "-panel-header']//parent::div").$$x(".//tbody/tr/td[" + getHeaderIndex(panelName, columnName) + "]");

        List<String> values = ele.texts();
        log.info("Get column list");
        return values;
    }
    @Step("User gets filtered value")
    public List<String> getFilteredValue(KYCCDDPanelTablePageComponent.PanelName panelName, String columnName) {
        ElementsCollection ele = $x(".//div//*[@id='" + panelName.getStringValue() + "-panel-header']//parent::div//th/div/span/ancestor::table").$$x("./tbody/tr/td[" + getHeaderIndex(panelName, columnName) + "]");
        log.info("get filtered value");
        return ele.texts();
    }

    @Step("Get column data")
    public List<String> getColumnData(KYCCDDPanelTablePageComponent.PanelName panelName, String columnName) {
        int index = panel(panelName).$$("th").asDynamicIterable()
                .stream()
                .map(it -> it.getText().replaceAll("\n\\d+", ""))
                .toList()
                .indexOf(columnName) + 1;
        log.info("Get Sorted column data");
        return body.$$("tr > td:nth-of-type(%d)".formatted(index))
                .asDynamicIterable()
                .stream()
                .map(element -> element.getText().trim())
                .filter(text -> !text.equals("No Data Found") && !text.isEmpty())
                .toList();
    }


    @Step("User clicks filter button by column")
    public SelenideElement filterIcon(KYCCDDPanelTablePageComponent.PanelName
                                              panelName, KYCCDDPanelTablePageComponent.HeaderName headerName) {
        return $x(".//div[./span[@title='" + headerName.getStringValue() + "']]//button[contains(@class,'" + panelName.getStringValue() + "')]");
    }

    @Step("User clicks filter button by column")
    public KYCCDDPanelTablePageComponent clickColumnFilterIcon(KYCCDDPanelTablePageComponent.PanelName
                                                                       panelName, KYCCDDPanelTablePageComponent.HeaderName headerName) {
        filterIcon(panelName, headerName)
                .as("Content filter icon")
                .click();
        log.info("Click filter icon");
        return this;
    }

    @Step("User enters filter input")
    public KYCCDDPanelTablePageComponent enterFilterValue(KYCCDDPanelTablePageComponent.PanelName
                                                                  panelName, KYCCDDPanelTablePageComponent.HeaderName headerName, String value) {
        SelenideElement filterInput = filterIcon(panelName, headerName).$x("./ancestor::th/div[2]/div/input")
                .as("Filter input area");
        filterInput.sendKeys(value);
        filterInput.pressEnter();
        log.info("Filter column data by value");
        return this;
    }

    @Step("Get KYC header element by its name")
    public SelenideElement getKYCHeaderElementByName(String panelName, String headerName) {
        SelenideElement ele = $x(".//div//*[@id='" + panelName + "-panel-header']//parent::div//th//*[contains(text(),'" + headerName + "')]/ancestor::th");
        return ele;
    }

    protected void sortTableWithAriaLabel1 (KYCCDDPanelTablePageComponent.PanelName panelName, KYCCDDPanelTablePageComponent.HeaderName headerName,
                                            String sortStatus){
        header = getKYCHeaderElementByName(panelName.getStringValue(), headerName.getStringValue());

       String sortDirection;
        int retry = 1;
        do {
            header.$x(".//*[@role='button']")
                    .as("Clickable header")
                    .shouldBe(enabled)
                    .click();
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            sortDirection = header.$("span.dn-header-sorting span")
                    .as("Sort direction icon")
                    .getAttribute("aria-label");
            System.out.println( "sortDirection   "+ sortDirection);
            if (retry++ > 3) {
                throw new RuntimeException("Could not get correct sorting arrow direction");
            }
        } while (sortDirection != null && !sortDirection.equals(sortStatus));
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

    @Getter
    @AllArgsConstructor
    public enum HeaderName implements GeneralTableHeaderName {

        GEOGRAPHY_NAME("Name"),
        GEOGRAPHY_SCORE("Score"),
        GEOGRAPHY_INFORMATION("Information"),
        CUSTOMER_NAME("Name"),
        CUSTOMER_SCORE("Score"),
        CUSTOMER_INFORMATION("Information"),
        PRODUCT_NAME("Name"),
        PRODUCT_SCORE("Score"),
        PRODUCT_INFORMATION("Information"),
        CHANNEL_NAME("Name"),
        CHANNEL_SCORE("Score"),
        CHANNEL_INFORMATION("Information"),
        OVERRIDE_NAME("Name"),
        OVERRIDE_SCORE("Score"),
        OVERRIDE_INFORMATION("Information");
        public final String stringValue;
    }

    @Getter
    @AllArgsConstructor
    public enum PanelName implements GeneralTableHeaderName {

        GEOGRAPHY("Geography"),
        CUSTOMER("Customer"),
        PRODUCT("Product"),
        CHANNEL("Channel"),
        OVERRIDE("Override");

        public final String stringValue;
    }
}

