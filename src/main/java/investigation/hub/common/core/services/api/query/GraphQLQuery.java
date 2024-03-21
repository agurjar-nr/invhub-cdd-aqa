package investigation.hub.common.core.services.api.query;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GraphQLQuery {
    private String query;
    private Object variables;
}
