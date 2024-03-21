package investigation.hub.common.core.services.api.query;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Query {
    private String query;
}
