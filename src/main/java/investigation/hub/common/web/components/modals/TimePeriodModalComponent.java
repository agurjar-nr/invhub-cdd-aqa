package investigation.hub.common.web.components.modals;

import static com.codeborne.selenide.Selenide.$x;

import com.smile.components.ModalComponent;
import investigation.hub.common.web.enums.InvHubDateFormat;
import investigation.hub.common.web.pages.LogHistoryPage;
import io.qameta.allure.Step;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ModalComponent
public class TimePeriodModalComponent {

    @Step("User picks Time Period")
    public TimePeriodModalComponent pickTimePeriod(String date) {
        $x("//div[@aria-label='Choose %s']".formatted(getDateFormat(date)))
                .as("Start/End date")
                .click();
        log.info("Picks 'Time Period'");
        return this;
    }

    public String getDateFormat(String date) {
        DateTimeFormatter before = DateTimeFormatter.ofPattern(
                InvHubDateFormat.YEAR_MONTH_DAY_TIME.getFormatterPattern()).withLocale(Locale.US);
        DateTimeFormatter after = DateTimeFormatter.ofPattern(
                InvHubDateFormat.MONTH_DAY_YEAR.getFormatterPattern()).withLocale(Locale.US);
        return LocalDateTime.parse(date, before).format(after);
    }

    @Step("User clicks Apply button")
    public LogHistoryPage clickApplyButton() {
        $x("//div[contains(text(),'Apply')]")
                .as("Apply button")
                .click();
        log.info("Click 'Apply' button");
        return new LogHistoryPage();
    }
}
