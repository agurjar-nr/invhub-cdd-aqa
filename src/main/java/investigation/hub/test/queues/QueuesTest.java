package investigation.hub.test.queues;

import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.web.components.modals.CreateEditQueueModalComponent;
import investigation.hub.common.web.components.tables.QueuesTablePageComponent;
import investigation.hub.common.web.components.tables.TableGeneralComponent;
import investigation.hub.common.web.components.tables.TeamsManagementTablePageComponent.HeaderName;
import investigation.hub.common.web.pages.LogHistoryPage;
import investigation.hub.common.web.pages.queues.QueuesDetailsPage;
import investigation.hub.common.web.pages.queues.QueuesManagementPage;
import investigation.hub.common.web.test.data.dtos.QueueDto;
import investigation.hub.common.web.test.data.repository.LogRepository;
import investigation.hub.common.web.test.data.repository.QueueRepository;
import investigation.hub.common.web.test.data.repository.UserDtoRepository;
import io.qameta.allure.TmsLink;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static investigation.hub.common.web.components.tables.QueuesTablePageComponent.HeaderName.CREATED_ON;
import static investigation.hub.common.web.components.tables.QueuesTablePageComponent.HeaderName.DESCRIPTION;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.testng.Assert.assertTrue;

public class QueuesTest extends InvHubUiTest {
    QueuesManagementPage queuesManagementPage;
    QueuesTablePageComponent queuesTable;

    @BeforeMethod
    public void openPage() {
        queuesManagementPage = openInvestigationsPage.getMainHeaderComponent().openQueuesManagement();
        queuesTable = queuesManagementPage.getQueuesTablePageComponent();
    }

    @TmsLink("INVHUB-3657")
    @Test(description = "Create queue with empty mandatory fields")
    public void createQueueWithEmptyMandatoryFields() {
        CreateEditQueueModalComponent createQueueModal = queuesManagementPage.clickOnCreateQueueButton();

        createQueueModal
                .submitButtonIsDisabled()
                .enterQueueDescription("Empty mandatory fields")
                .clickSubmitButton();

        createQueueModal.mandatoryFieldsValidationMessagesAreDisplayed();
    }

    @TmsLink("INVHUB-3682")
    @Test(description = "Edit queue with empty mandatory fields")
    public void editQueueWithEmptyMandatoryFields() {
        queuesTable.sortTable(DESCRIPTION, TableGeneralComponent.SortStatus.DESCENDING);
        String queueToEditName = queuesTable.getColumnData(HeaderName.NAME.getStringValue()).get(0);
        QueuesDetailsPage queuesDetailsPage = queuesTable.openQueueDetailsPage(queueToEditName);

        CreateEditQueueModalComponent editQueueModal = queuesDetailsPage.clickOnEditButton();

        editQueueModal
                .submitButtonIsDisabled()
                .clearMandatoryQueueData()
                .clickSubmitButton();

        editQueueModal.mandatoryFieldsValidationMessagesAreDisplayed();
    }

    @TmsLink("INVHUB-3658")
    @Test(description = "Create queue with existing name")
    public void createQueueWithExistingName() {
        String existingQueueName = queuesTable.getAnyColumnValue(QueuesTablePageComponent.HeaderName.NAME);
        QueueDto queueToCreate = QueueRepository.queueWithMandatoryFields();
        queueToCreate.setName(existingQueueName);

        CreateEditQueueModalComponent createQueueModal = queuesManagementPage.clickOnCreateQueueButton();
        createQueueModal
                .enterMandatoryQueueData(queueToCreate)
                .clickSubmitButton();

        createQueueModal
                .createQueueModalIsNotVisible()
                .messageIsDisplayed("Error creating queue");
    }

    @TmsLink("INVHUB-3684")
    @Test(description = "Edit queue with existing name")
    public void editQueueWithExistingName() {
        queuesTable.sortTable(DESCRIPTION, TableGeneralComponent.SortStatus.DESCENDING);
        String queueToEditName = queuesTable.getColumnData(HeaderName.NAME.getStringValue()).get(0);
        String existingQueueName = queuesTable.getColumnData(HeaderName.NAME.getStringValue()).get(1);
        QueuesDetailsPage queuesDetailsPage = queuesTable.openQueueDetailsPage(queueToEditName);

        CreateEditQueueModalComponent editQueueModal = queuesDetailsPage.clickOnEditButton();
        editQueueModal
                .enterQueueName(existingQueueName)
                .clickSubmitButton();

        editQueueModal
                .editQueueModalIsNotVisible()
                .messageIsDisplayed("Error updating queue details");
    }

    @TmsLink("INVHUB-3655")
    @Test(description = "Create queue with mandatory fields")
    public void createQueueWithMandatoryFields() {
        QueueDto queueToCreate = QueueRepository.queueWithMandatoryFields();

        CreateEditQueueModalComponent createQueueModal = queuesManagementPage.clickOnCreateQueueButton();
        createQueueModal
                .enterMandatoryQueueData(queueToCreate)
                .clickSubmitButton();

        verifyQueueIsCreated(createQueueModal, queueToCreate);
        checkLog(queueToCreate);
    }

    @TmsLink("INVHUB-3676")
    @Test(description = "Edit queue with mandatory fields")
    public void editQueueWithMandatoryFields() {
        queuesTable.sortTable(DESCRIPTION, TableGeneralComponent.SortStatus.DESCENDING);
        String queueToEditName = queuesTable.getColumnData(HeaderName.NAME.getStringValue()).get(0);
        QueuesDetailsPage queuesDetailsPage = queuesTable.openQueueDetailsPage(queueToEditName);
        QueueDto newQueueDto = QueueRepository.queueWithMandatoryFields();

        CreateEditQueueModalComponent editQueueModal = queuesDetailsPage.clickOnEditButton();
        editQueueModal
                .enterQueueName(newQueueDto.getName())
                .clickSubmitButton();

        editQueueModal
                .editQueueModalIsNotVisible()
                .messageIsDisplayed("Queue details updated");
        checkLog(newQueueDto);
    }

    @TmsLink("INVHUB-3656")
    @Test(description = "Create queue with all fields")
    public void createQueueWithAllFields() {
        QueueDto queueToCreate = QueueRepository.queueWithAllFields();

        CreateEditQueueModalComponent createQueueModal = queuesManagementPage.clickOnCreateQueueButton();
        createQueueModal
                .enterMandatoryQueueData(queueToCreate)
                .enterQueueDescription(queueToCreate.getDescription())
                .clickSubmitButton();

        verifyQueueIsCreated(createQueueModal, queueToCreate);
        checkLog(queueToCreate);
    }

    @TmsLink("INVHUB-3677")
    @Test(description = "Edit queue with all fields")
    public void editQueueWithAllFields() {
        queuesTable.sortTable(DESCRIPTION, TableGeneralComponent.SortStatus.DESCENDING);
        String queueToEditName = queuesTable.getColumnData(HeaderName.NAME.getStringValue()).get(0);
        QueuesDetailsPage queuesDetailsPage = queuesTable.openQueueDetailsPage(queueToEditName);
        QueueDto newQueueDto = QueueRepository.queueWithAllFields();

        CreateEditQueueModalComponent editQueueModal = queuesDetailsPage.clickOnEditButton();
        editQueueModal
                .enterQueueName(newQueueDto.getName())
                .enterQueueDescription(newQueueDto.getDescription())
                .clickSubmitButton();

        editQueueModal
                .editQueueModalIsNotVisible()
                .messageIsDisplayed("Queue details updated");
        checkLog(newQueueDto);
    }

    private void verifyQueueIsCreated(CreateEditQueueModalComponent createQueueModal, QueueDto queueToCreate) {
        createQueueModal
                .createQueueModalIsNotVisible()
                .messageIsDisplayed("Queue created successfully");

        queuesTable.sortTable(CREATED_ON, TableGeneralComponent.SortStatus.DESCENDING);
        assertThat(queuesTable.getColumnData(HeaderName.NAME.getStringValue()).get(0))
                .as("Queue should be added to Queues table")
                .isEqualTo(queueToCreate.getName());
    }

    private void checkLog(QueueDto newQueueDto) {
        String userFullName = UserDtoRepository.createTestUserInstance().getFullName();
        LogHistoryPage logHistoryPage = openInvestigationsPage.getMainHeaderComponent()
                .openUsers()
                .searchUser(UserDtoRepository.createTestUserInstance().getFirstName())
                .openUserProfile(userFullName)
                .clickLogHistoryButton();
        Map<String, String> recentAuditLogData = logHistoryPage.getLogHistoryTable().getRecentAuditLogData();
        assertTrue(recentAuditLogData.entrySet().containsAll(LogRepository.getCreateQueueLog(newQueueDto).entrySet()),
                "Log: %s should be present".formatted(LogRepository.getCreateQueueLog(newQueueDto).entrySet()));
    }
}