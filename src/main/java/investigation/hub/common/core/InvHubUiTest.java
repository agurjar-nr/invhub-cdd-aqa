package investigation.hub.common.core;

import static com.codeborne.selenide.Selenide.open;
import static investigation.hub.common.web.test.data.constants.AccessPolicy.ALL_AML_ACCESS;
import static investigation.hub.common.web.test.data.constants.Role.ADMIN;

import investigation.hub.common.core.properties.ConfigProperties;
import investigation.hub.common.core.properties.loaders.ConfigLoader;
import investigation.hub.common.core.services.api.client.LoginClient;
import investigation.hub.common.core.services.api.client.QueueClient;
import investigation.hub.common.core.services.api.client.TeamClient;
import investigation.hub.common.core.services.api.client.UserClient;
import investigation.hub.common.core.services.api.model.provider.TeamProvider;
import investigation.hub.common.core.services.api.model.provider.UserGraphQLProvider;
import investigation.hub.common.core.services.api.model.team.Queue;
import investigation.hub.common.core.services.api.model.team.Team;
import investigation.hub.common.core.services.api.model.user.UserGraphQL;
import investigation.hub.common.web.pages.AllOpenInvestigationsPage;
import investigation.hub.common.web.pages.LoginPage;
import investigation.hub.common.web.test.data.constants.Role;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.BeforeClass;

import java.util.List;

@Log4j2
public class InvHubUiTest extends InvHubBaseTest {

    protected static final ConfigProperties apiProperties = ConfigLoader.getConfigProperties();

    protected LoginPage loginPage = new LoginPage();
    protected LoginClient loginClient = new LoginClient();

    protected AllOpenInvestigationsPage openInvestigationsPage;

    protected String email = apiProperties.getAutoTestUser();

    protected Team team;
    protected UserGraphQL user;

    protected TeamClient teamClient = new TeamClient();
    protected UserClient userClient = new UserClient();

    @BeforeClass
    public void loginUser() {
        loginUser(email);
    }

    public void loginUser(String email) {
        open(apiProperties.getUrl() + apiProperties.getLogin());
        openInvestigationsPage = loginPage.loginUser(email, loginClient);
        log.info("Login as `%s` user".formatted(email));
    }

    protected void createUserWithNoTeam(Role role) {
        user = new UserGraphQLProvider(role).provide();
        user.setId(userClient.createUser(user));
        userClient.resetCache();
    }

    protected void createUserInTeam(Team team) {
        user = new UserGraphQLProvider().provide();
        user.setId(userClient.createUser(user));
        teamClient.addUsersToTeam(List.of(user), team);
        userClient.resetCache();
    }

    protected void deleteUser(UserGraphQL deleteUser) {
        if (deleteUser != null) {
            userClient.deleteUser(deleteUser.getId());
        }
    }

    protected void deleteTeam(Team deleteTeam) {
        if (deleteTeam != null) {
            teamClient.deleteTeam(deleteTeam.getId());
        }
    }
}