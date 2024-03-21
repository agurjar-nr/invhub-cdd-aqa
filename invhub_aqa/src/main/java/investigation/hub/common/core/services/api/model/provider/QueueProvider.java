package investigation.hub.common.core.services.api.model.provider;

import com.github.javafaker.Faker;
import groovy.util.logging.Log4j2;
import investigation.hub.common.core.services.api.model.team.Queue;


@Log4j2
public class QueueProvider implements Provider<Queue> {
    private final Queue queue;

    public QueueProvider() {
        String prefix = "AQA_API_TEST_QUEUE";
        Faker faker = new Faker();
        String uuid = faker.internet().uuid();
        queue = Queue.builder()
                .id(uuid)
                .name("%s_%s_%s".formatted(prefix,faker.funnyName().name(),uuid))
                .description("%s_DESCRIPTION_%s".formatted(prefix,uuid))
                .logic(new LogicProvider().provide())
                .build();
    }

    @Override
    public Queue provide() {
        return queue;
    }
}