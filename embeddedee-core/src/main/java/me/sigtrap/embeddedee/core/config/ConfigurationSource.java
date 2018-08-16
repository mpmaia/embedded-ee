package me.sigtrap.embeddedee.core.config;

import java.io.IOException;
import java.io.InputStream;

public interface ConfigurationSource {
    InputStream read() throws IOException;
}
