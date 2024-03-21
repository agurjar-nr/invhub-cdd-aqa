package investigation.hub.common.core.properties;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.net.URI;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

/**
 * Keeps credentials values
 */
@Config.LoadPolicy(Config.LoadType.MERGE)
@Sources({"system:properties", "classpath:configurations/config.properties"})
public interface ConfigProperties extends Config {
    @Key("env_url")
    @DefaultValue(EMPTY)
    URI getUrl();

    @Key("test_user")
    @DefaultValue(EMPTY)
    String getTestUser();

    @Key("graphql_path")
    @DefaultValue(EMPTY)
    String getGraphQLPath();

    @Key("retry_count")
    @DefaultValue("1")
    String getRetryCount();

    @Key("api_key")
    @DefaultValue("api_key_not_set_in_config")
    String getApiKey();

    @Key("api_user")
    @DefaultValue("api_user_not_set_in_config")
    String getApiUser();

    @Key("api_login_graphql")
    @DefaultValue("")
    String getApiLoginGraphql();

    @Key("graphql_request_path")
    @DefaultValue("graphql_not_set_in_config")
    String getGraphqlRequestPath();

    @Key("login_path")
    @DefaultValue(EMPTY)
    String getLogin();

    @Key("open_investigations")
    @DefaultValue("not_set_in_config")
    String getOpenInvestigations();

    @Key("use.remote.browsers.provider")
    @DefaultValue("false")
    boolean getUseRemoteBrowsersProvider();

    @Key("remote.browsers.provider.url")
    @DefaultValue("")
    String getRemoteBrowsersProviderUrl();

    @Key("auto_test_user")
    @DefaultValue("not_set_in_config")
    String getAutoTestUser();

    //anand
    @Key("finCrimeTypeFilter")
    @DefaultValue("")
    String finCrimeTypeFilter();


}
