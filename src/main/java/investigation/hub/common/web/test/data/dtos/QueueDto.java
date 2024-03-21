package investigation.hub.common.web.test.data.dtos;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class QueueDto {
    private String name;
    private String description;
    private String logic;

}
