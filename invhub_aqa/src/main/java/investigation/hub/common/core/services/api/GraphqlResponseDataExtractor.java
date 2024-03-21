package investigation.hub.common.core.services.api;

import com.jayway.jsonpath.*;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.restassured.response.Response;

public class GraphqlResponseDataExtractor {

    private String path;
    private Response response;

    private <T> T extract(Class<T> type) {
        T extractedData;
        String stepId = AllureCommonActions.allureStepStart("Extracting JSON data by path '" + path + "'", Status.PASSED);
        try {
            DocumentContext documentContext = JsonPath.parse(response.asPrettyString());
            extractedData = documentContext.read(path, type);
            AllureCommonActions.allureAttachTxt("JSON array '" + path + "'", extractedData.toString());
        } catch (PathNotFoundException pnfe) {
            Allure.step("Failed to extract data from JSON by path '" + path + "'!", Status.BROKEN);
            Allure.getLifecycle().updateStep(stepId, sr -> sr.setStatus(Status.BROKEN));
            throw pnfe;
        } finally {
            Allure.getLifecycle().stopStep();
        }
        return extractedData;
    }

    private <T> T extract(TypeRef<T> typeRef) {
        T extractedData;
        String stepId = AllureCommonActions.allureStepStart("Extracting JSON data by path '" + path + "'", Status.PASSED);

        Configuration config = Configuration.defaultConfiguration()
                .jsonProvider(new GsonJsonProvider())
                .mappingProvider(new GsonMappingProvider());
        try {
            extractedData = JsonPath.using(config).parse(response.asPrettyString()).read(path, typeRef);
            AllureCommonActions.allureAttachTxt("JSON array '" + path + "'", extractedData.toString());
        } catch (PathNotFoundException pnfe) {
            Allure.step("Failed to extract data from JSON by path '" + path + "'!", Status.BROKEN);
            Allure.getLifecycle().updateStep(stepId, sr -> sr.setStatus(Status.BROKEN));
            throw pnfe;
        } finally {
            Allure.getLifecycle().stopStep();
        }
        return extractedData;
    }



    public static class For {
        private final GraphqlResponseDataExtractor extractor = new GraphqlResponseDataExtractor();

        public For path(String path) {
            extractor.path = path;
            return this;
        }

        public For response(Response response) {
            extractor.response = response;
            return this;
        }

        public <T> T extract(Class<T> type) {
            return extractor.extract(type);
        }

        public <T> T extract(TypeRef<T> typeRef) {
            return extractor.extract(typeRef);
        }

        public boolean isJsonPathExists() {
            try {
                DocumentContext documentContext = JsonPath.parse(extractor.response.asPrettyString());
                documentContext.read(extractor.path);
                return true;
            } catch (PathNotFoundException ignored) {
                return false;
            }
        }
    }

}
