package jasyncapicmp.parser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import jasyncapicmp.JAsyncApiCmpUserException;
import jasyncapicmp.model.Api;
import jasyncapicmp.model.asyncapi.AsyncApi;
import jasyncapicmp.model.openapi.OpenApi;

public class ApiParser {

    private enum Format {
        JSON, YAML
    }

    public Api parse(byte[] file, String filePath) {
        Format format = detectFormat(file);
		Api api;
        if (format == Format.JSON) {
			api = parseJson(file, filePath);
        } else {
			api = parseYaml(file, filePath);
        }
        return api;
    }

	private Class<? extends Api> detectType(JsonNode rootNode) {
		if (rootNode.isObject()) {
			ObjectNode objectNode = (ObjectNode) rootNode;
			JsonNode value = objectNode.findValue("asyncapi");
			if (value != null) {
				return AsyncApi.class;
			}
			value = objectNode.findValue("openapi");
			if (value != null) {
				return OpenApi.class;
			}
		}
		throw new JAsyncApiCmpUserException("Could not detect ASyncApi or OpenApi format: missing top-level property " +
			"'asyncapi' or 'openapi'.");
	}

	Api parseYaml(byte[] file, String filePath) {
        ObjectMapper mapper = new YAMLMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            JsonNode jsonNode = mapper.readTree(file);
            ReferenceResolver resolver = new ReferenceResolver();
            resolver.resolveRefs(jsonNode);
            return mapper.treeToValue(jsonNode, detectType(jsonNode));
        } catch (Exception e) {
            throw new JAsyncApiCmpUserException("Failed to parse YAML (" + filePath + "): " + e.getMessage(), e);
        }
    }

	Api parseJson(byte[] file, String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            JsonNode jsonNode = mapper.readTree(file);
            ReferenceResolver resolver = new ReferenceResolver();
            resolver.resolveRefs(jsonNode);
            return mapper.treeToValue(jsonNode, detectType(jsonNode));
        } catch (Exception e) {
            throw new JAsyncApiCmpUserException("Failed to parse JSON (" + filePath + "): " + e.getMessage(), e);
        }
    }

    private Format detectFormat(byte[] file) {
        for (byte b : file) {
            char c = (char) b;
            if (!Character.isWhitespace(c)) {
                if (c == '{') {
                    return Format.JSON;
                } else {
                    return Format.YAML;
                }
            }
        }
        throw new JAsyncApiCmpUserException("Could not detect YAML or JSON format.");
    }
}
