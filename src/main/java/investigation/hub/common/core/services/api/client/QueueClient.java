package investigation.hub.common.core.services.api.client;

import investigation.hub.common.core.services.api.model.team.Queue;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class QueueClient extends ApiClient {

    public String createQueue(Queue queue) {
        action = "createQueue";
        query = getGraphQLQuery(action, queue);
        response = graphQlRequest(query);
        return getResponseId(action,response);
    }

}
