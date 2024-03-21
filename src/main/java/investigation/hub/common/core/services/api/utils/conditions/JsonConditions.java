package investigation.hub.common.core.services.api.utils.conditions;

public class JsonConditions {

    public static JsonNodeCondition jsonNodePresents(String path) {
        return new JsonNodeCondition(path);
    }

    public static JsonStringValueCondition jsonValueEquals(String path, String expectedValue) {
        return new JsonStringValueCondition(expectedValue, path);
    }

    public static ResponseStatusCodeCondition statusCodeEquals(int expValue) {
        return new ResponseStatusCodeCondition(expValue);
    }

    public static JsonIntValueGreaterThanCondition jsonValueIsGreaterThan(String path, int expValue) {
        return new JsonIntValueGreaterThanCondition(path, expValue);
    }

    public static JsonArraySizeGreaterThanCondition jsonArraySizeGreaterThan(String path, int expValue) {
        return new JsonArraySizeGreaterThanCondition(path, expValue);
    }

    public static JsonValuesSortedCondition jsonArraySortedDescending(String path) {
        return new JsonValuesSortedCondition(path);
    }

    public static JsonArraySizeEqualsCondition jsonArraySizeEquals(String path, int expValue) {
        return new JsonArraySizeEqualsCondition(path, expValue);
    }
}
