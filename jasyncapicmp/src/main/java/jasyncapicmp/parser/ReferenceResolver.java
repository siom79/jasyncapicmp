package jasyncapicmp.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jasyncapicmp.JAsyncApiCmpUserException;

import java.util.*;
import java.util.logging.Logger;

public class ReferenceResolver {
	private static final Logger LOGGER = Logger.getLogger(ReferenceResolver.class.getName());
	public static final String PATH_SEPARATOR = "/";
    Map<String, String> resolvedPaths = new HashMap<>();

    public JsonNode resolveRefs(JsonNode jsonNode) {
        return resolveRefsRecursive(jsonNode, jsonNode, "", new ArrayList<>());
    }

    private JsonNode resolveRefsRecursive(JsonNode rootNode, JsonNode jsonNode, String path, List<JsonNode> visited) {
		if (visited.stream().anyMatch(v -> v == jsonNode)) {
			throw new JAsyncApiCmpUserException("Recursion detected: " + jsonNode);
		}
        Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String fieldName = entry.getKey();
            JsonNode fieldValue = entry.getValue();
            String newPath = path + PATH_SEPARATOR + fieldName;
            if ("$ref".equals(fieldName) && fieldValue.isTextual()) {
                String jsonPtrExpr = fieldValue.textValue();
				String jsonPath = removeHash(jsonPtrExpr);
				JsonNode resolved = rootNode.at(jsonPath);
				LOGGER.fine("Resolved " + jsonPtrExpr + ": " + resolved);
                if (resolved.isMissingNode()) {
                    throw new JAsyncApiCmpUserException("Unresolvable reference: " + jsonPtrExpr);
                }
                resolveRefsRecursive(rootNode, resolved, jsonPath, copyAndAdd(visited, jsonNode));
                incorporateResolved(jsonNode, resolved);
            } else if (!resolvedPaths.containsKey(newPath)) {
                resolveRefsRecursive(rootNode, fieldValue, newPath, copyAndAdd(visited, jsonNode));
                resolvedPaths.put(newPath, newPath);
            }
        }
        if (jsonNode.isArray()) {
            Iterator<JsonNode> elements = jsonNode.elements();
            int count = 0;
            while(elements.hasNext()) {
                JsonNode nextNode = elements.next();
                String newPath = path + PATH_SEPARATOR + count;
                if (!resolvedPaths.containsKey(newPath)) {
                    resolveRefsRecursive(rootNode, nextNode, newPath, copyAndAdd(visited, jsonNode));
                    resolvedPaths.put(newPath, newPath);
                }
                count++;
            }
        }
        return rootNode;
    }

	private List<JsonNode> copyAndAdd(List<JsonNode> nodes, JsonNode newNode) {
		List<JsonNode> copy = new ArrayList<>(nodes);
		copy.add(newNode);
		return copy;
	}

	private static String removeHash(String jsonPtrExpr) {
		int indexSeparator = jsonPtrExpr.indexOf('#');
		if (indexSeparator >= 0) {
			if (jsonPtrExpr.length() >= (indexSeparator + 2)) {
				jsonPtrExpr = jsonPtrExpr.substring(indexSeparator + 1);
			} else {
				throw new JAsyncApiCmpUserException("Reference too short: " + jsonPtrExpr);
			}
		}
		return jsonPtrExpr;
	}

	private void incorporateResolved(JsonNode jsonNode, JsonNode resolved) {
        if (resolved.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Iterator<Map.Entry<String, JsonNode>> fields = resolved.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                objectNode.set(entry.getKey(), entry.getValue());
            }
        }
    }
}
