package investigation.hub.common.web.test.data.repository;

import com.github.javafaker.Faker;
import investigation.hub.common.core.services.api.model.team.Queue;
import investigation.hub.common.web.test.data.dtos.QueueDto;

public class QueueRepository {
    private static final String PREFIX = "AQA_QUEUE";

    public static QueueDto queueWithMandatoryFields() {
        Faker faker = new Faker();
        return QueueDto.builder()
                .name("%s-M-%s".formatted(PREFIX, faker.internet().uuid()))
                .description("")
                .logic("""
                        {"value":"m"}
                        """)
                .build();
    }

    public static QueueDto queueWithAllFields() {
        Faker faker = new Faker();
        return QueueDto.builder()
                .name("%s-A-%s".formatted(PREFIX, faker.internet().uuid()))
                .description("Queue description" + faker.internet().uuid())
                .logic("""
                        {"value":"a"}
                        """)
                .build();
    }

    public static Queue amlL1Queue() {
        return Queue.builder()
                .id("50c2797e-2b63-40b4-9677-dfc3f5bfb4c1")
                .name("AML L1 Queue")
                .description("AML L1 Queue for first line triage of investigations")
                .build();
    }
}
