package me.sigtrap.embeddedee.core.jpa;

import me.sigtrap.embeddedee.core.jpa.persistence.JpaPersistenceHandler;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class EntityManagerFactoryFactory {

    private static Map<String, EntityManagerFactoryHolder> cache = new HashMap<>();

    public static synchronized EntityManagerFactoryHolder getEntityManagerFactory(String unitName) {

        EntityManagerFactoryHolder wrapper = null;

        if(unitName==null || unitName.isEmpty())
            unitName = getDefaultUnitName();

        wrapper = cache.get(unitName);
        if (wrapper == null) {

            Properties properties = new Properties();

            EntityManagerFactory factory = Persistence.createEntityManagerFactory(unitName, properties);
            wrapper = new EntityManagerFactoryHolder(factory, null);

            cache.put(unitName, wrapper);
        }

        return wrapper;
    }

    private static String PERSISTENCE_XML = "META-INF/persistence.xml";

    private static String getDefaultUnitName() {

        try {

            Enumeration<URL> enumeration = EntityManagerFactoryFactory.class.getClassLoader().getResources(PERSISTENCE_XML);

            if (!enumeration.hasMoreElements()) {
                return null;
            }

            enumeration.nextElement();

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            JpaPersistenceHandler jpaPersistenceHandler = new JpaPersistenceHandler();

            saxParser.parse(EntityManagerFactoryFactory.class.getClassLoader().getResourceAsStream(PERSISTENCE_XML), jpaPersistenceHandler);

            return jpaPersistenceHandler.getUnitName();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse persistence.xml file", ex);
        }
    }
}

