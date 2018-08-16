package me.sigtrap.embeddedee.core.config.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import me.sigtrap.embeddedee.core.config.ConfigurationReader;
import me.sigtrap.embeddedee.core.config.ConfigurationSource;

import java.io.IOException;
import java.io.InputStream;

public class JsonConfigurationReader implements ConfigurationReader {

    private ObjectMapper mapper = ObjectMapperFactory.get();
    private ConfigurationSource configurationSource;

    public JsonConfigurationReader(ConfigurationSource configurationSource) {
        this.configurationSource = configurationSource;
    }

    public <T> T read(String propertyRoot, TypeReference<T> type) throws IOException {
        try {
            JsonNode jsonNode = getJsonNode(propertyRoot);
            ObjectReader objectReader = mapper.readerFor(type);
            return objectReader.readValue(jsonNode);
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public <T> T read(String propertyRoot, Class<T> clazz) throws IOException {
        try {
            JsonNode jsonNode = getJsonNode(propertyRoot);
            return mapper.treeToValue(jsonNode, clazz);
        } catch (IOException e) {
            throw e;
        }
    }

    private JsonNode getJsonNode(String propertyRoot) throws IOException {
        InputStream inputStream = configurationSource.read();
        JsonNode jsonNode = mapper.readTree(inputStream);
        jsonNode = jsonNode.at(propertyRoot);
        return jsonNode;
    }
}
