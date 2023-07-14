package jasyncapicmp.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jasyncapicmp.JAsyncApiCmpUserException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ReferenceResolver {
    public static final String PATH_SEPARATOR = "/";
    Map<String, String> resolvedPaths = new HashMap<>();

    public JsonNode resolveRefs(JsonNode jsonNode) {
        return resolveRefsRecursive(jsonNode, jsonNode, "");
    }

    private JsonNode resolveRefsRecursive(JsonNode rootNode, JsonNode jsonNode, String path) {
        Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String fieldName = entry.getKey();
            JsonNode fieldValue = entry.getValue();
            String newPath = path + PATH_SEPARATOR + fieldName;
            if ("$ref".equals(fieldName) && fieldValue.isTextual()) {
                String jsonPtrExpr = fieldValue.textValue();
                int indexSeparator = jsonPtrExpr.indexOf('#');
                if (indexSeparator >= 0) {
                    if (jsonPtrExpr.length() >= (indexSeparator + 2)) {
                        jsonPtrExpr = jsonPtrExpr.substring(indexSeparator + 1);
                    } else {
                        throw new JAsyncApiCmpUserException("Reference too short: " + jsonPtrExpr);
                    }
                }
                JsonNode resolved = rootNode.at(jsonPtrExpr);
                if (resolved.isMissingNode()) {
                    throw new JAsyncApiCmpUserException("Unresolvable reference: " + jsonPtrExpr);
                }
                resolveRefsRecursive(rootNode, resolved, newPath);
                incorporateResolved(jsonNode, resolved);
            } else if (!resolvedPaths.containsKey(newPath)) {
                resolveRefsRecursive(rootNode, fieldValue, newPath);
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
                    resolveRefsRecursive(rootNode, nextNode, newPath);
                    resolvedPaths.put(newPath, newPath);
                }
                count++;
            }
        }
        return rootNode;
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
