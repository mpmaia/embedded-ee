package me.sigtrap.embeddedee.core.config.yaml;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import me.sigtrap.embeddedee.core.config.ConfigurationReader;
import me.sigtrap.embeddedee.core.config.ConfigurationSource;

import java.io.IOException;
import java.io.InputStream;

public class YamlConfigurationReader implements ConfigurationReader {

    private ObjectMapper mapper = YamlObjectMapperFactory.get();
    private ConfigurationSource configurationSource;

    public YamlConfigurationReader(ConfigurationSource configurationSource) {
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
    public boolean hasProperty(String name) {
        try {
            JsonNode jsonNode = getJsonNode(name);
            return jsonNode!=null && !jsonNode.isMissingNode();
        } catch (IOException e) {
            return false;
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
