package investigation.hub.common.core.services.api.conditions;

import static com.google.common.truth.Truth.assertWithMessage;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ResponseStatusCodeCondition extends AbstractJsonCondition implements JsonCondition {

    @NonNull
    private Integer expValue;

    @Override
    public String provideDescription() {
        return "Validating response code";
    }

    @Override
    public void doDirectValidation() {
        assertWithMessage("Unexpected response status code!")
                .that(response.getStatusCode()).isEqualTo(expValue);
    }

    @Override
    public void doInvertedValidation() {
        assertWithMessage("Response status code should not be equals '" + expValue + "'!")
                .that(response.getStatusCode()).isNotEqualTo(expValue);
    }

    @Override
    public String provideReportData() {
        return "Status code: " + response.statusCode();
    }

}
