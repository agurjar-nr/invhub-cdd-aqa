package investigation.hub.common.core.services.api.utils.conditions;

import investigation.hub.common.core.services.api.utils.GraphqlResponseDataExtractor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static com.google.common.truth.Truth.assertWithMessage;

@RequiredArgsConstructor
public class JsonNodeCondition extends AbstractJsonCondition implements JsonCondition {

    @NonNull private String path;

    @Override
    public String provideDescription() {
        return "Validating response' JSON contains value by path '" + path + "'";
    }

    @Override
    public void doDirectValidation() {
        assertWithMessage("JSON node with path '" + path + "' doesn't exist!")
                .that(new GraphqlResponseDataExtractor.For().response(response).path(path).isJsonPathExists())
                .isEqualTo(true);
    }

    @Override
    public void doInvertedValidation() {
        assertWithMessage("JSON node with path '" + path + "' exists!")
                .that(new GraphqlResponseDataExtractor.For().response(response).path(path).isJsonPathExists())
                .isEqualTo(false);
    }

    @Override
    public String provideReportData() {
        return "JSON node path '" + path  + "'";
    }

}
