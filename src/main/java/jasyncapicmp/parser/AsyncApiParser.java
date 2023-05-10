package jasyncapicmp.parser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import jasyncapicmp.JAsyncApiCmpUserException;
import jasyncapicmp.model.AsyncApi;

public class AsyncApiParser {

    private enum Type {
        JSON, YAML
    }

    public AsyncApi parse(byte[] file, String filePath) {
        Type type = detectType(file);
        AsyncApi asyncApi;
        if (type == Type.JSON) {
            asyncApi = parseJson(file, filePath);
        } else {
            asyncApi = parseYaml(file, filePath);
        }
        return asyncApi;
    }

    AsyncApi parseYaml(byte[] file, String filePath) {
        ObjectMapper mapper = new YAMLMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            JsonNode jsonNode = mapper.readTree(file);
            ReferenceResolver resolver = new ReferenceResolver();
            resolver.resolveRefs(jsonNode);
            return mapper.treeToValue(jsonNode, AsyncApi.class);
        } catch (Exception e) {
            throw new JAsyncApiCmpUserException("Failed to parse YAML (" + filePath + "): " + e.getMessage(), e);
        }
    }

    AsyncApi parseJson(byte[] file, String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            JsonNode jsonNode = mapper.readTree(file);
            ReferenceResolver resolver = new ReferenceResolver();
            resolver.resolveRefs(jsonNode);
            return mapper.treeToValue(jsonNode, AsyncApi.class);
        } catch (Exception e) {
            throw new JAsyncApiCmpUserException("Failed to parse JSON (" + filePath + "): " + e.getMessage(), e);
        }
    }

    private Type detectType(byte[] file) {
        for (byte b : file) {
            char c = (char) b;
            if (!Character.isWhitespace(c)) {
                if (c == '{') {
                    return Type.JSON;
                } else {
                    return Type.YAML;
                }
            }
        }
        throw new JAsyncApiCmpUserException("Could not detect YAML or JSON format.");
    }
}
