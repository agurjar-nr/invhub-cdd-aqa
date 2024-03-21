package investigation.hub.common.web.components;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.smile.components.PageComponent;
import investigation.hub.common.core.util.Retry;
import investigation.hub.common.web.components.tables.AccountsTablePageComponent;

import investigation.hub.common.web.components.tables.KYCCDDPanelTablePageComponent;
import investigation.hub.common.web.components.tables.OpenDetectionsTablePageComponent;
import investigation.hub.common.web.components.tables.TableGeneralComponent;
import io.qameta.allure.Step;
import io.qameta.allure.TmsLink;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
@Log4j2
@PageComponent
@Getter

public class KYCCDDPanelPageComponent {

    private final KYCCDDPanelTablePageComponent kyccddPanelTablePageComponent = new KYCCDDPanelTablePageComponent();

    private final SelenideElement KYC_CDD = $x("//div[text()='KYC/CDD']");
    private final SelenideElement Score_Details = $x("//h3[text()='Score Details']");
    private final SelenideElement Score_Date = $x("//div[text()='Score Date']");
    private final SelenideElement Score_DateValue = $x("//div[text()='Score Date']/parent::li/span");
    private final SelenideElement CDD_Risk = $x("//div[text()='CDD Risk']");
    private final SelenideElement CDD_RiskValue = $x("//*[text()='CDD Risk']/parent::li/span");
    private final SelenideElement CDD_Score = $x("//div[text()='CDD Score']");
    private final SelenideElement CDD_ScoreValue = $x("//div[text()='CDD Score']/parent::li/span");
    private final SelenideElement Outcome = $x("//div[text()='Outcome']");
    private final SelenideElement OutcomeValue = $x("//div[text()='Outcome']/parent::li/span");
    private final SelenideElement Geography = $x("//h3[text()='Geography']");
    private final SelenideElement GeographyName = $x("//*[@id='Geography-panel-header']/parent::div/div[2]/div/div");
    private final SelenideElement GeographyGeo = $x("//*[@id='Geography-panel-header']/parent::div/div[2]/div/div");
    private final SelenideElement GeographyValue = $x("//*[@id='Geography-panel-header']/parent::div/div[2]/div[2]/div");
    private final SelenideElement GeographyValueText = $x("//*[@id='Geography-panel-header']/parent::div/div[2]/div[2]/span");
    private final SelenideElement Customer = $x("//h3[text()='Customer']");
    private final SelenideElement CustomerName = $x("//*[@id='Customer-panel-header']/parent::div/div[2]/div/div");
    private final SelenideElement CustomerCust = $x("//*[@id='Customer-panel-header']/parent::div/div[2]/div/div");
    private final SelenideElement CustomerValue = $x("//*[@id='Customer-panel-header']/parent::div/div[2]/div[2]/div");
    private final SelenideElement CustomerText = $x("//*[@id='Customer-panel-header']/parent::div/div[2]/div[2]/span");
    private final SelenideElement Product = $x("//h3[text()='Product']");
    private final SelenideElement ProductName = $x("//*[@id='Product-panel-header']/parent::div/div[2]/div/div");
    private final SelenideElement ProductProd = $x("//*[@id='Product-panel-header']/parent::div/div[2]/div/div");
    private final SelenideElement ProductValue = $x("//*[@id='Product-panel-header']/parent::div/div[2]/div[2]/div");
    private final SelenideElement ProductText = $x("//*[@id='Product-panel-header']/parent::div/div[2]/div[2]/span");
    private final SelenideElement Channel = $x("//h3[text()='Channel']");
    private final SelenideElement ChannelName = $x("//*[@id='Channel-panel-header']/parent::div/div[2]/div/div");
    private final SelenideElement ChannelChan = $x("//*[@id='Channel-panel-header']/parent::div/div[2]/div/div");
    private final SelenideElement ChanneltValue = $x("//*[@id='Channel-panel-header']/parent::div/div[2]/div[2]/div");
    private final SelenideElement ChannelText = $x("//*[@id='Channel-panel-header']/parent::div/div[2]/div[2]/span");
    private final SelenideElement sortName = $x("//*[@id='headlessui-tabs-panel-99']/div/div/div[2]/div[4]/table/thead/tr/th[1]/div/span");
    private final SelenideElement NoOption = $x("//[contains(text(),'No Options')]");



    public KYCCDDPanelPageComponent clickColumnFilterquery(String query){
              $x("(.//span[contains(text(),'" + query + "')]/../parent::tr/td[3]/a)[1]")
                .as("Clinking on subject name which has text: " + query)
                .click();
        log.info("Click subject name with filter query");
        waitForLoad();
        return new  KYCCDDPanelPageComponent();
    }

    public boolean checkElementNoOption(){
        return NoOption.isDisplayed();
    }

   public KYCCDDPanelPageComponent toggleSwitch() {
        $x("//*[contains(@class, 'toggle-switch')]")
                .as("Clicking on toggle swtich")
                .click();
        log.info("Click toggle switch");
        return new  KYCCDDPanelPageComponent();
    }
    public void scrollUpToElement(SelenideElement element){
       ((JavascriptExecutor) getWebDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
    }
    public  boolean checkComponent(){
      String actual= $("button[title='Risk']>span ")
                .as("Hover on Risk")
                .hover().getText();
       String expected = $x("//*[@class='dn-section-header open']/div/h2")
                .as("Expected")
                .getText();
        boolean flag = false;
        if(actual.equals(expected)){
            flag = true;
        }
        log.info("Check component");
        return flag;
    }
    public boolean panelIndex(){
        ElementsCollection ele= $x(".//*[contains(@class,'dn-panel dn-detail-panel col-12')]")
                .$$x(".//div[contains(@class,'dn-panel dn-detail-panel col-6')]/div[2]/div[2]/span")
                .as("extracting total no. of panel values");
        scrollUpToElement(ele.get(0));
        boolean flag =false;
        int num, firstNum;
        firstNum = Integer.parseInt(ele.get(0).getText());
        for(int i=1; i<ele.size(); i++){
             num =Integer.parseInt(ele.get(i).getText());
             if(firstNum>num){
               flag=true;
             }else {
               flag=false;
             }
             firstNum =num;
        }
        log.info("Find panel index");

        return flag;
    }

    @Step("User clicks Historical Detections button")
    public KYCCDDPanelPageComponent clickHistoricalDetectionsButton() {
        $x("//p[contains(text(),'Latest Score Card')]")
                .as("Latest Score Card")
                .scrollIntoView(false)
                .click();
        log.info("Click Latest Score Card button");
        return new KYCCDDPanelPageComponent();
    }

    public boolean isElementPresent(SelenideElement element) {
        log.info("Check "+element);
        return element
                .scrollIntoView(false)
                .isDisplayed();


    }
    public KYCCDDPanelPageComponent waitForLoad() {
        $("#current-screen-subjectDetails")
                .as("subject details window container")
                .shouldBe(Condition.visible);
        log.info("Main page window container should be visible");

        return new KYCCDDPanelPageComponent();


    }

    @Step("Check CDD risk label colour")
    public boolean checkRiskLevelColor() {
       String color= CDD_RiskValue.getAttribute("class");
       String riskLevel = CDD_RiskValue.getText();
       String[] co =color.split("bg-");
       String colorValue = co[1].split("!text")[0].trim();
        System.out.println("Color value-->"+colorValue);
        System.out.println("riskLevel"+riskLevel);

        boolean flag = false;
        switch (riskLevel) {
            case "VERY HIGH" -> {
               if(colorValue.equals("red-700") ){
                    flag = true;
                   log.info("Very High level");
               }
            }
            case "HIGH" -> {
               if( colorValue.equals("red-600")) {
                   flag = true;
                   log.info("High level");

               }

            }
            case "MEDIUM" -> {
                if( colorValue.equals("yellow-600")){
                   flag = true;
                    log.info("Medium level");
                }
            }
            case "LOW" -> {
                if(colorValue.equals("green-600")) {
                    flag = true;
                    log.info("Green level");
                }

            }
            case "STANDARD" -> {
                if(colorValue.equals("purple-700")) {
                     flag = true;
                    log.info("Purple level");
                }

            }
            default -> {
                return flag;

            }
        }
            log.info("Validate risk level color");
        return flag;
    }


}
