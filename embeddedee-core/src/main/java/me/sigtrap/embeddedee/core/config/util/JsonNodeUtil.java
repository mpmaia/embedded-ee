package me.sigtrap.embeddedee.core.config.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JsonNodeUtil {

    public static JsonNode merge(JsonNode firstNode, JsonNode secondNode) {

        Iterable<String> fieldIt = () -> secondNode.fieldNames();
        Stream<String> fieldNames = StreamSupport.stream(fieldIt.spliterator(), false);

        fieldNames.forEach(fieldName -> {

            JsonNode firstNodeValue = firstNode.get(fieldName);
            JsonNode secondNodeValue = secondNode.get(fieldName);

            if(firstNodeValue != null) {
                if (firstNodeValue.isArray() && secondNodeValue.isArray()) {
                    ArrayNode secondValueArrayNode = (ArrayNode) secondNodeValue;
                    for(JsonNode item: secondValueArrayNode) {
                        ((ArrayNode) firstNodeValue).add(item);
                    }
                } else if (firstNodeValue.isObject() && secondNodeValue.isObject()) {
                    merge(firstNodeValue, secondNodeValue);
                }
            } else {
                if (firstNode instanceof ObjectNode) {
                    ((ObjectNode) firstNode).replace(fieldName, secondNodeValue);
                }
            }
        });

        return firstNode;
    }
}
