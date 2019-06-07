package me.sigtrap.embeddedee.core.util;

import me.sigtrap.embeddedee.core.server.JettyServer;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class ClassPathUtil {

    public static boolean isRunningExploded() {
        try {
            URL classes = JettyServer.class.getClassLoader().getResource(".");

            if(classes==null) {
                return false;
            }
            return Paths.get(classes.toURI()).getFileName().toString().equals("classes");
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
