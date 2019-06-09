package me.sigtrap.embeddedee.core.config;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;

public interface ConfigurationReader {
    boolean hasProperty(String name);
    <T> T read(String propertyRoot, Class<T> clazz) throws IOException;
    <T> T read(String propertyRoot, TypeReference<T> type) throws IOException;
    <T> T readInto(String propertyRoot, T obj) throws IOException;
}
