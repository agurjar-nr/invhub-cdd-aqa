package investigation.hub.common.web.components;

import static com.codeborne.selenide.Selenide.$x;

public class TransactionCommonComponent extends PageGeneralComponent {

    public boolean isTransactionIncludedInNarrativeMessageAppeared() {
        return $x("//p[contains(text(),'1 transaction included in narrative successfully')]")
                .as("1 transaction included in narrative message")
                .isDisplayed();
    }

    public boolean areTransactionsIncludedInNarrativeMessageAppeared(int expectedNumber) {
        return $x("//p[contains(text(),'%s transactions included in narrative successfully')]".formatted(expectedNumber))
                .as("%s transactions included in narrative message".formatted(expectedNumber))
                .isDisplayed();
    }

    public boolean isTransactionExcludedFromNarrativeMessageAppeared() {
        return $x("//p[contains(text(),'1 transaction excluded from narrative successfully')]")
                .as("1 transaction excluded from narrative successfully message")
                .isDisplayed();
    }

    public boolean areTransactionsExcludedFromNarrativeMessageAppeared(int expectedNumber) {
        return $x("//p[contains(text(),'%s transactions excluded from narrative successfully')]".formatted(expectedNumber))
                .as("%s transactions excluded from narrative successfully message".formatted(expectedNumber))
                .isDisplayed();
    }

}
