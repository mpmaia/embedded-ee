package me.sigtrap.embeddedee.core.config.sources;

import me.sigtrap.embeddedee.core.config.ConfigurationSource;

import java.io.*;

public class FileSystemConfigurationSource implements ConfigurationSource {

    private String path;

    public FileSystemConfigurationSource(String path) {
        this.path = path;
    }

    @Override
    public InputStream read() throws IOException {
        final File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("File " + file + " not found.");
        }
        return new FileInputStream(file);
    }
}
