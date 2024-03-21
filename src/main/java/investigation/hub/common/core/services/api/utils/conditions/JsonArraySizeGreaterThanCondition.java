package investigation.hub.common.core.services.api.utils.conditions;

import com.jayway.jsonpath.TypeRef;
import investigation.hub.common.core.services.api.utils.GraphqlResponseDataExtractor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.google.common.truth.Truth.assertWithMessage;

@RequiredArgsConstructor
public class JsonArraySizeGreaterThanCondition extends AbstractJsonCondition implements JsonCondition {

    @NonNull private String path;
    @NonNull private Integer expValue;

    @Override
    public String provideDescription() {
        return "Validating JSON array size by path '" + path + "'";
    }

    @Override
    public void doDirectValidation() {
        List<Object> actualArray = new GraphqlResponseDataExtractor.For()
                .response(response).path(path).extract(new TypeRef<List<Object>>() {});
        assertWithMessage("JSON array from response with path '" + path + "' has unexpected size!")
                .that(actualArray.size()).isGreaterThan(expValue);
    }

    @Override
    public void doInvertedValidation() {
        List<Object> actualArray = new GraphqlResponseDataExtractor.For()
                .response(response).path(path).extract(new TypeRef<List<Object>>() {});
        assertWithMessage("JSON array from response with path '" + path + "' has unexpected size!")
                .that(actualArray.size()).isLessThan(expValue);
    }
}
