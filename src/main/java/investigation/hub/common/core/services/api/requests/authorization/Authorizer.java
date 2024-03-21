package investigation.hub.common.core.services.api.requests.authorization;

import io.restassured.builder.RequestSpecBuilder;

public interface Authorizer {

     /**
      * Returns RequestSpecBuilder that filled by data
      * that required by specific type of authorization
      * (f.e. tokens, ids, etc)
      * */
     RequestSpecBuilder produceRequestWithAuthData();

     RequestSpecBuilder produceRequest();

     /**
      * Executes actions required for authorization.
      * Sends credentials and receives authorization data.
      * */
     void authorize();

}
