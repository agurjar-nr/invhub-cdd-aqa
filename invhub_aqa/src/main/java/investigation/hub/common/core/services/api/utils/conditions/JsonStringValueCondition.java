package investigation.hub.common.core.services.api.utils.conditions;

import investigation.hub.common.core.services.api.utils.GraphqlResponseDataExtractor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static com.google.common.truth.Truth.assertWithMessage;

/**
 * Validates value from JSON by path
 * Find basic path syntax here: https://www.baeldung.com/guide-to-jayway-jsonpath
 * */
@RequiredArgsConstructor
public class JsonStringValueCondition extends AbstractJsonCondition implements JsonCondition {

    @NonNull private String expectedValue;
    @NonNull private String path;

    @Override
    public String provideDescription() {
        return "Validating response' JSON value by path '" + path + "'";
    }

    @Override
    public void doDirectValidation() {
        String actualValue = new GraphqlResponseDataExtractor.For().path(path).response(response).extract(String.class);
        assertWithMessage("JSON from response with path '" + path + "' has unexpected value!")
                .that(actualValue).isEqualTo(expectedValue);
    }

    @Override
    public void doInvertedValidation() {
        String actualValue = new GraphqlResponseDataExtractor.For().path(path).response(response).extract(String.class);
        assertWithMessage("JSON from response with path '" + path + "' has unexpected value!")
                .that(actualValue).isNotEqualTo(expectedValue);
    }

    @Override
    public String provideReportData() {
        return "Json data by path '" + path + "'" + new GraphqlResponseDataExtractor.For()
                .path(path).response(response).extract(String.class);
    }
}
