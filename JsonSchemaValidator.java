import java.util.*;

public class JsonSchemaValidator {

    @SuppressWarnings("unchecked")
    public static String validate(Map<String, Object> schema, Map<String, Object> data) {
        if (!"object".equals(schema.get("type"))) return "Error: Root schema must be of type object";

        Map<String, Object> properties = (Map<String, Object>) schema.get("properties");
        List<String> required = (List<String>) schema.get("required");

        for (String key : required) {
            if (!data.containsKey(key)) return "Error: Missing required field '" + key + "'";
        }

        for (String key : properties.keySet()) {
            Object value = data.get(key);
            Map<String, Object> propertySchema = (Map<String, Object>) properties.get(key);

            if (value != null) {
                String error = validateProperty(key, value, propertySchema);
                if (error != null) return error;
            }
        }

        return "Valid JSON";
    }

    @SuppressWarnings("unchecked")
    private static String validateProperty(String key, Object value, Map<String, Object> schema) {
        String type = (String) schema.get("type");

        switch (type) {
            case "string":
                if (!(value instanceof String)) return "Error: '" + key + "' should be a string";
                break;

            case "integer":
                if (!(value instanceof Integer)) return "Error: '" + key + "' should be an integer";
                Integer intValue = (Integer) value;
                if (schema.containsKey("minimum")) {
                    int min = (int) schema.get("minimum");
                    if (intValue < min) return "Error: '" + key + "' should be >= " + min;
                }
                if (schema.containsKey("maximum")) {
                    int max = (int) schema.get("maximum");
                    if (intValue > max) return "Error: '" + key + "' should be <= " + max;
                }
                break;

            case "boolean":
                if (!(value instanceof Boolean)) return "Error: '" + key + "' should be a boolean";
                break;

            case "object":
                if (!(value instanceof Map)) return "Error: '" + key + "' should be an object";
                String result = validate(schema, (Map<String, Object>) value);
                if (!result.equals("Valid JSON")) return result;
                break;

            default:
                return "Error: Unsupported type for key '" + key + "'";
        }

        return null;
    }

    public static void main(String[] args) {
        // Schema
        Map<String, Object> schema = new HashMap<>();
        schema.put("type", "object");

        Map<String, Object> properties = new HashMap<>();

        Map<String, Object> nameProp = new HashMap<>();
        nameProp.put("type", "string");

        Map<String, Object> ageProp = new HashMap<>();
        ageProp.put("type", "integer");
        ageProp.put("minimum", 18);

        properties.put("name", nameProp);
        properties.put("age", ageProp);

        schema.put("properties", properties);
        schema.put("required", Arrays.asList("name", "age"));

        // Valid data
        Map<String, Object> validJson = new HashMap<>();
        validJson.put("name", "Alice");
        validJson.put("age", 25);

        // Invalid data
        Map<String, Object> invalidJson = new HashMap<>();
        invalidJson.put("name", "Bob");
        invalidJson.put("age", "twenty");

        System.out.println("Valid JSON Test: " + validate(schema, validJson));
        System.out.println("Invalid JSON Test: " + validate(schema, invalidJson));
    }
}
