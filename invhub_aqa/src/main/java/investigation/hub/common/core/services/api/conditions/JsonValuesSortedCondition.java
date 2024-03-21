package investigation.hub.common.core.services.api.conditions;

import static com.google.common.truth.Truth.assertWithMessage;

import investigation.hub.common.core.services.api.GraphqlResponseDataExtractor;
import com.jayway.jsonpath.TypeRef;

import java.util.stream.IntStream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JsonValuesSortedCondition extends AbstractJsonCondition implements JsonCondition {

    @NonNull
    private String path;

    @Override
    public String provideDescription() {
        return "Validating JSON array values sorted in descending order by path '" + path + "'";
    }

    @Override
    public void doDirectValidation() {
        double[] valuesArray = new GraphqlResponseDataExtractor.For()
                .response(response).path(path).extract(new TypeRef<>() {
                });
        assertWithMessage("JSON array values '" + path + "' are not sorted in descending  order!")
                .that(areValuesSortedDescending(valuesArray)).isTrue();
    }

    @Override
    public void doInvertedValidation() {
        double[] valuesArray = new GraphqlResponseDataExtractor.For()
                .response(response).path(path).extract(new TypeRef<>() {
                });
        assertWithMessage("JSON array values '" + path + "' are sorted in descending  order!")
                .that(areValuesSortedDescending(valuesArray)).isFalse();
    }

    private boolean areValuesSortedDescending(double[] values) {
        return IntStream
                .range(0, values.length - 1)
                .noneMatch(i -> values[i] <= values[i + 1]);
    }
}
