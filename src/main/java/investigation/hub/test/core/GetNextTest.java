package investigation.hub.test.core;

import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.core.services.api.model.provider.TeamProvider;
import investigation.hub.common.web.components.tables.InvestigationsTablePageComponent;
import investigation.hub.common.web.pages.InvestigationPage;
import investigation.hub.common.web.test.data.repository.QueueRepository;
import io.qameta.allure.TmsLink;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static investigation.hub.common.web.components.tables.InvestigationsTablePageComponent.HeaderName.SUBJECT_ID;
import static investigation.hub.common.web.components.tables.InvestigationsTablePageComponent.HeaderName.SUBJECT_NAME;
import static investigation.hub.common.web.test.data.constants.AccessPolicy.EUR_AML_ACCESS;
import static investigation.hub.common.web.test.data.constants.Role.ADMIN;

@Log4j2
public class GetNextTest extends InvHubUiTest {
    InvestigationsTablePageComponent investigationsTable;
    InvestigationPage investigationPage;

    @Override
    public void loginUser() {
    }

    @TmsLink("INVHUB-4500")
    @Test(description = "Get next multiple investigations via header")
    public void getNextMultipleInvestigationsViaHeader() {
        team = new TeamProvider(ADMIN, List.of(QueueRepository.amlL1Queue()), EUR_AML_ACCESS).provide();
        team.setId(teamClient.createTeam(team));
        createUserInTeam(team);
        loginUser(user.getEmail());

        openInvestigationsPage.getMainHeaderComponent().clickGetNextButton();
        investigationPage = new InvestigationPage();
        String investigation1SubjectId = investigationPage.getSubjectId();
        String investigation1Title = investigationPage.getInvestigationTitle();

        investigationPage.getMainHeaderComponent().clickGetNextButton();
        String investigation2SubjectId = investigationPage.getSubjectId();
        String investigation2Title = investigationPage.getInvestigationTitle();

        openInvestigationsPage = investigationPage.getMainHeaderComponent().openAllOpenInvestigations();
        investigationsTable = openInvestigationsPage.getInvestigationsTable();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(investigationsTable.getColumnData(SUBJECT_ID.stringValue))
                .as("Assigned investigations' subject ids should be in the table")
                .isEqualTo(List.of(investigation1SubjectId, investigation2SubjectId));

        softly.assertThat(investigationsTable.getColumnData(SUBJECT_NAME.stringValue))
                .as("Assigned investigations' subject names should be in the table")
                .isEqualTo(Stream.of(investigation1Title, investigation2Title)
                        .map(String::toUpperCase)
                        .collect(Collectors.toList()));
        softly.assertAll();
    }

    @TmsLink("INVHUB-4498")
    @Test(description = "Get next investigation via header for user with no queue")
    public void getNextInvestigationViaHeaderForUserWithNoQueue() {
        createUserWithNoTeam(ADMIN);
        loginUser(user.getEmail());

        openInvestigationsPage.getMainHeaderComponent().clickGetNextButton();
        openInvestigationsPage.messageIsDisplayed("Please assign user to some queue");
    }

    @AfterMethod(alwaysRun = true)
    public void deleteEntities() {
        deleteUser(user);
        deleteTeam(team);
    }
}
