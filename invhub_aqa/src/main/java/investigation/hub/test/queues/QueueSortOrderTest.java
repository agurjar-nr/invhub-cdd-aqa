package investigation.hub.test.queues;

import investigation.hub.common.core.InvHubUiTest;
import investigation.hub.common.web.components.PaginationComponent;
import investigation.hub.common.web.components.tables.QueuesTablePageComponent;
import investigation.hub.common.web.components.tables.TableGeneralComponent;
import investigation.hub.common.web.pages.queues.QueuesManagementPage;
import io.qameta.allure.TmsLink;
import io.qameta.allure.TmsLinks;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static investigation.hub.common.web.components.tables.QueuesTablePageComponent.HeaderName.*;
import static investigation.hub.common.web.components.tables.TableGeneralComponent.SortStatus.ASCENDING;
import static investigation.hub.common.web.components.tables.TableGeneralComponent.SortStatus.DESCENDING;

public class QueueSortOrderTest extends InvHubUiTest {
    PaginationComponent paginationComponent;
    QueuesManagementPage queuesManagementPage;
    QueuesTablePageComponent queueManagementTablePageComponent = new QueuesTablePageComponent();

    @BeforeMethod
    public void openPage() {
        queuesManagementPage = openInvestigationsPage.getMainHeaderComponent().openQueuesManagement();
        paginationComponent = openInvestigationsPage.getPaginationComponent();
    }
    @TmsLinks({
            @TmsLink("INVHUB-4586"),
            @TmsLink("INVHUB-4355"),
            @TmsLink("INVHUB-3338")
    })
    @Test(description = "Check pages 1 and 2 sorted by name and desc using previous and next button", dataProvider = "sortOrderDataProviderByDescAndName")
    public void verifyPagesSortedByPreviousAndNextButton(QueuesTablePageComponent.HeaderName headerName, TableGeneralComponent.SortStatus sortStatus) {
        queueManagementTablePageComponent.sortTable(headerName, sortStatus);
        List<String> firstPageColumnData = queueManagementTablePageComponent.getColumnData(headerName.getStringValue());

        paginationComponent.clickNextButton();
        List<String> secondPageColumnData = queueManagementTablePageComponent.getColumnData(headerName.getStringValue());

        List<String> combinedColumn = new ArrayList<>(firstPageColumnData);
        combinedColumn.addAll(secondPageColumnData);

        verifyTextSortOrder(combinedColumn, sortStatus);

        paginationComponent.clickPreviousButton();
        List<String> columnsAfterClickPreviousButton = queueManagementTablePageComponent.getColumnData(headerName.getStringValue());

        Assertions.assertThat(columnsAfterClickPreviousButton).isEqualTo(firstPageColumnData);
    }

    @TmsLinks({
            @TmsLink("INVHUB-4586"),
            @TmsLink("INVHUB-4356"),
            @TmsLink("INVHUB-3338")
    })
    @Test(description = "Check pages 1 and 2 sorted by name and desc using number button", dataProvider = "sortOrderDataProviderByDate")
    public void verifyPagesSortedByNumberButton(QueuesTablePageComponent.HeaderName headerName, TableGeneralComponent.SortStatus sortStatus) {
        queueManagementTablePageComponent.sortTable(headerName, sortStatus);
        List<String> firstPageColumnData = queueManagementTablePageComponent.getColumnData(headerName.getStringValue());

        paginationComponent.clickButtonByNumber(2);
        List<String> secondPageColumnData = queueManagementTablePageComponent.getColumnData(headerName.getStringValue());

        List<String> combinedColumn = new ArrayList<>(firstPageColumnData);
        combinedColumn.addAll(secondPageColumnData);

        verifyDateSortOrderDate(combinedColumn, sortStatus);

        paginationComponent.clickButtonByNumber(1);
        List<String> columnsAfterClickPageOne = queueManagementTablePageComponent.getColumnData(headerName.getStringValue());

        Assertions.assertThat(columnsAfterClickPageOne).isEqualTo(firstPageColumnData);
    }

    @DataProvider
    public Object[][] sortOrderDataProviderByDescAndName() {
        return new Object[][]{
                {DESCRIPTION, ASCENDING},
                {DESCRIPTION, DESCENDING},
                {NAME, ASCENDING},
                {NAME, DESCENDING},
        };
    }

    @DataProvider
    public Object[][] sortOrderDataProviderByDate() {
        return new Object[][]{
                {LAST_UPDATE, ASCENDING},
                {LAST_UPDATE, DESCENDING},
                {CREATED_ON, ASCENDING},
                {CREATED_ON, DESCENDING},
        };
    }

    private void verifyTextSortOrder(List<String> combinedColumn, TableGeneralComponent.SortStatus sortStatus) {
        List<String> combinedColumnWithoutSpaceAndSpecialCharacters = new ArrayList<>();

        for (String data : combinedColumn) {
            String cleanData = data.replaceAll("[^a-zA-Z0-9]+", "");
            combinedColumnWithoutSpaceAndSpecialCharacters.add(cleanData);
        }

        List<String> sortedCombinedColumn = new ArrayList<>(combinedColumnWithoutSpaceAndSpecialCharacters);

        if (TableGeneralComponent.SortStatus.isAsc(sortStatus)) {
            sortedCombinedColumn.sort(String.CASE_INSENSITIVE_ORDER);
        } else if (TableGeneralComponent.SortStatus.isDesc(sortStatus)) {
                sortedCombinedColumn.sort(Comparator.comparing(String::toString, String.CASE_INSENSITIVE_ORDER).reversed());
        }

        Assertions.assertThat(combinedColumnWithoutSpaceAndSpecialCharacters).isEqualTo(sortedCombinedColumn);
    }

    private void verifyDateSortOrderDate(List<String> combinedColumn, TableGeneralComponent.SortStatus sortStatus) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy, HH:mm:ss");
        List<String> sortedCombinedColumn = new ArrayList<>(combinedColumn);

        if (TableGeneralComponent.SortStatus.isAsc(sortStatus)) {
            sortedCombinedColumn.sort(Comparator.comparing(s -> LocalDateTime.parse(s, formatter)));
            Assertions.assertThat(combinedColumn).isEqualTo(sortedCombinedColumn);
        } else if (TableGeneralComponent.SortStatus.isDesc(sortStatus)) {
            sortedCombinedColumn.sort(Comparator.comparing((String s) -> LocalDateTime.parse(s, formatter)).reversed());
            Assertions.assertThat(combinedColumn).isEqualTo(sortedCombinedColumn);
        }
    }
}
