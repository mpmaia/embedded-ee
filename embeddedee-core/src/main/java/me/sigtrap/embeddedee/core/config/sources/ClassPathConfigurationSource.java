package me.sigtrap.embeddedee.core.config.sources;

import me.sigtrap.embeddedee.core.config.ConfigurationSource;

import java.io.*;

public class ClassPathConfigurationSource implements ConfigurationSource {

    private String path;

    public ClassPathConfigurationSource(String path) {
        this.path = path;
    }

    @Override
    public InputStream read() throws IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(path);
        return stream;
    }
}