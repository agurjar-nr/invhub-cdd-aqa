package investigation.hub.common.core.services.api.requests.authorization;

import investigation.hub.common.core.properties.ConfigProperties;

import io.restassured.builder.RequestSpecBuilder;
import org.aeonbits.owner.ConfigFactory;

public class EmptyAuthorizer implements Authorizer {

    private static final ConfigProperties config = ConfigFactory.create(ConfigProperties.class);

    @Override
    public RequestSpecBuilder produceRequestWithAuthData() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder
                .addHeader("Authorization", "Bearer " + "")
                .setBaseUri(config.getUrl());
        return requestSpecBuilder;
    }

    @Override
    public RequestSpecBuilder produceRequest() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        return requestSpecBuilder
                .setBaseUri(config.getUrl());
    }

    @Override
    public void authorize() {
        throw new IllegalStateException("The user cannot be authorized");
    }
}
