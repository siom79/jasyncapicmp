package jasyncapicmp.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ReferenceResolverTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testChannelRef() throws JsonProcessingException {
        ReferenceResolver resolver = new ReferenceResolver();
        JsonNode jsonNode = objectMapper.readTree("{\n" +
                "  \"channels\": {\n" +
                "    \"mychannel\": {\n" +
                "      \"$ref\": \"#/components/channels/cref\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"components\": {\n" +
                "    \"channels\": {\n" +
                "      \"cref\": {\n" +
                "        \"description\": \"desc\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}");

        JsonNode resolved = resolver.resolveRefs(jsonNode);

        Assertions.assertThat(resolved.at("/channels/mychannel/description").textValue()).isEqualTo("desc");
    }

    @Test
    void testMessageAndPayloadRef() throws JsonProcessingException {
        ReferenceResolver resolver = new ReferenceResolver();
        JsonNode jsonNode = objectMapper.readTree("{\n" +
                "  \"asyncapi\": \"2.2.0\",\n" +
                "  \"channels\": {\n" +
                "    \"light/measured\": {\n" +
                "      \"publish\": {\n" +
                "        \"summary\": \"Inform about environmental lighting conditions for a particular streetlight.\",\n" +
                "        \"operationId\": \"onLightMeasured\",\n" +
                "        \"message\": {\n" +
                "          \"$ref\": \"#/components/messages/LightMeasured\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"components\": {\n" +
                "    \"messages\": {\n" +
                "      \"LightMeasured\": {\n" +
                "        \"name\": \"LightMeasured\",\n" +
                "        \"payload\": {\n" +
                "          \"$ref\": \"#/components/schemas/LightMeasurement\"\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"schemas\": {\n" +
                "      \"LightMeasurement\": {\n" +
                "        \"type\": \"object\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}");

        JsonNode resolved = resolver.resolveRefs(jsonNode);

        Assertions.assertThat(resolved.get("channels").get("light/measured").get("publish").get("message").get("name").textValue()).isEqualTo("LightMeasured");
    }

    @Test
    void testMessageOneOf() throws JsonProcessingException {
        ReferenceResolver resolver = new ReferenceResolver();
        JsonNode jsonNode = objectMapper.readTree("{\n" +
                "  \"channels\": {\n" +
                "    \"mychannel\": {\n" +
                "      \"subscribe\": {\n" +
                "        \"message\": {\n" +
                "          \"oneOf\": [\n" +
                "            {\n" +
                "              \"$ref\": \"#/components/messages/signup\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"$ref\": \"#/components/messages/login\"\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"components\": {\n" +
                "    \"messages\": {\n" +
                "      \"signup\": {\n" +
                "        \"title\": \"signup\",\n" +
                "        \"type\": \"object\"\n" +
                "      },\n" +
                "      \"login\": {\n" +
                "        \"title\": \"login\",\n" +
                "        \"type\": \"object\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}");

        JsonNode resolved = resolver.resolveRefs(jsonNode);

        Assertions.assertThat(resolved.get("channels").get("mychannel").get("subscribe").get("message").get("oneOf").get(0).get("title").textValue()).isEqualTo("signup");
        Assertions.assertThat(resolved.get("channels").get("mychannel").get("subscribe").get("message").get("oneOf").get(1).get("title").textValue()).isEqualTo("login");
    }
}