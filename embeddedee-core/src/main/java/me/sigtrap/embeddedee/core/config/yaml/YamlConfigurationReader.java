package me.sigtrap.embeddedee.core.config.yaml;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import me.sigtrap.embeddedee.core.config.ConfigurationReader;
import me.sigtrap.embeddedee.core.config.ConfigurationSource;
import me.sigtrap.embeddedee.core.config.util.JsonNodeUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class YamlConfigurationReader implements ConfigurationReader {

    private ObjectMapper mapper = YamlObjectMapperFactory.get();
    private List<ConfigurationSource> configurationSources;
    private JsonNode jsonNode;

    public YamlConfigurationReader(ConfigurationSource ...configurationSource) throws IOException {
        this.configurationSources = Arrays.asList(configurationSource);
        this.jsonNode = readNodes();
    }

    private JsonNode readNodes() throws IOException {
        JsonNode node = null;
        for(ConfigurationSource cs: this.configurationSources) {
            InputStream inputStream = cs.read();
            JsonNode jsonNode = mapper.readTree(inputStream);
            if(node!=null) {
                node = JsonNodeUtil.merge(node, jsonNode);
            } else {
                node = jsonNode;
            }
        }
        return node;
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

    @Override
    public <T> T readInto(String propertyRoot, T obj) throws IOException {
        try {
            JsonNode jsonNode = getJsonNode(propertyRoot);
            return mapper.readerForUpdating(obj).readValue(jsonNode);
        } catch (IOException e) {
            throw e;
        }
    }

    private JsonNode getJsonNode(String propertyRoot) throws IOException {
        JsonNode subNode = this.jsonNode.at(propertyRoot);
        return subNode;
    }
}
