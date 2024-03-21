package investigation.hub.common.core.services.api.requests.services;

import investigation.hub.common.core.services.api.requests.authorization.Authorizer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AbstractRequestService {

    @NonNull
    protected Authorizer authorizer;

    public AbstractRequestService() {}

    protected String cleanUpRedundantNewLinesInRequest(String inoutRequest) {
        return inoutRequest.replaceAll("\\n","");
    }
}
