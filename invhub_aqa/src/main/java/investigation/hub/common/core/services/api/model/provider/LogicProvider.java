package investigation.hub.common.core.services.api.model.provider;

import com.github.javafaker.Faker;
import groovy.util.logging.Log4j2;
import investigation.hub.common.core.services.api.model.team.Logic;


@Log4j2
public class LogicProvider implements Provider<Logic> {
    private final Logic logic;
    public LogicProvider() {

        logic = Logic.builder()
                .field("AQA_API_TEST_QUEUE")
                .stringValue(new Faker().internet().uuid())
                .build();
    }

    @Override
    public Logic provide() {
        return logic;
    }
}