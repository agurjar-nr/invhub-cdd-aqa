package investigation.hub.common.core.services.api.model.provider;

import static investigation.hub.common.web.enums.InvHubDateFormat.YEAR_MONTH_DAY_TIME_STAMP;
import static java.util.concurrent.TimeUnit.DAYS;

import com.github.javafaker.Faker;
import groovy.util.logging.Log4j2;
import investigation.hub.common.web.enums.InvHubDateFormat;
import java.text.SimpleDateFormat;


@Log4j2
public class DateTimeProvider implements Provider<String> {
    private String dateTimeString;

    public DateTimeProvider(Faker faker, InvHubDateFormat format,int daysBeforeNow) {
        dateTimeString = new SimpleDateFormat(format.getFormatterPattern()).format(faker.date().past(daysBeforeNow,1, DAYS));
    }

    public DateTimeProvider(Faker faker,int daysBeforeNow) {
        new DateTimeProvider(faker, YEAR_MONTH_DAY_TIME_STAMP,daysBeforeNow);
    }

    @Override
    public String  provide() {
        return dateTimeString;
    }
}