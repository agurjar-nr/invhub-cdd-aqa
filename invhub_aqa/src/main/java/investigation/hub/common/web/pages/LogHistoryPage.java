package investigation.hub.common.web.pages;

import static com.codeborne.selenide.Selenide.$x;

import com.smile.components.Page;
import investigation.hub.common.web.components.modals.TimePeriodModalComponent;
import investigation.hub.common.web.components.tables.LogHistoryTableComponent;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Page
@Getter
public class LogHistoryPage {

    LogHistoryTableComponent logHistoryTable = new LogHistoryTableComponent();

    public boolean isLogHistoryBreadCrumbVisible() {
        return $x("//a[contains(@href, '/history')]")
                .as("Log history breadcrumb")
                .isDisplayed();
    }

    @Step("User clicks on Time Period")
    public TimePeriodModalComponent clickTimePeriodForm() {
        $x("//*[@data-testid='CalendarTodayIcon']/parent::span")
                .as("Time Period")
                .click();
        log.info("Click 'Time Period'");
        return new TimePeriodModalComponent();
    }
}
