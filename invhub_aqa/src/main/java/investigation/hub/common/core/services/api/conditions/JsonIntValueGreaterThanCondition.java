package investigation.hub.common.core.services.api.conditions;

import static com.google.common.truth.Truth.assertWithMessage;

import investigation.hub.common.core.services.api.GraphqlResponseDataExtractor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JsonIntValueGreaterThanCondition extends AbstractJsonCondition implements JsonCondition {

    @NonNull private String path;
    @NonNull private Integer expValue;

    @Override
    public String provideDescription() {
        return "Comparing response' JSON value by path '" + path + "' with expected " +
                "value '" + expValue + "'";
    }

    @Override
    public void doDirectValidation() {
        Integer actualValue = new GraphqlResponseDataExtractor.For()
                .response(response).path(path).extract(Integer.class);
        assertWithMessage("JSON value from response with path '" + path + "' should be greater than " +
                "'" + expValue + "'!")
                .that(actualValue).isGreaterThan(expValue);
    }

    @Override
    public void doInvertedValidation() {
        Integer actualValue = new GraphqlResponseDataExtractor.For()
                .response(response).path(path).extract(Integer.class);
        assertWithMessage("JSON value from response with path '" + path + "' should be greater than " +
                "'" + expValue + "'!")
                .that(actualValue).isLessThan(expValue);
    }
}
