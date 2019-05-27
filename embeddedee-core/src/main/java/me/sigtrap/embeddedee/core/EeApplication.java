package me.sigtrap.embeddedee.core;

import com.fasterxml.jackson.core.type.TypeReference;
import me.sigtrap.embeddedee.core.config.ConfigurationReader;
import me.sigtrap.embeddedee.core.config.ConfigurationSource;
import me.sigtrap.embeddedee.core.config.exceptions.UnsupportedConfigurationException;
import me.sigtrap.embeddedee.core.config.yaml.YamlConfigurationReader;
import me.sigtrap.embeddedee.core.config.sources.ClassPathConfigurationSource;
import me.sigtrap.embeddedee.core.datasources.DataSourceConfig;
import me.sigtrap.embeddedee.core.datasources.DataSourceType;
import me.sigtrap.embeddedee.core.datasources.HikariDataSourceFactory;
import me.sigtrap.embeddedee.core.server.JettyServer;
import org.jboss.weld.environment.servlet.BeanManagerResourceBindingListener;
import org.jboss.weld.environment.servlet.Listener;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class EeApplication {

    private static final String SERVER_PROPERTY = "/server";
    private static final String DATASOURCES_PROPERTY = "/dataSources";

    public EeApplication() {

    }

    public void start() {

        ConfigurationSource configurationSource = new ClassPathConfigurationSource("application.yaml");
        ConfigurationReader configurationReader = new YamlConfigurationReader(configurationSource);

        JettyServer server = null;
        List<DataSourceConfig> dataSourcesConfig = Collections.emptyList();
        try {

            if(configurationReader.hasProperty(SERVER_PROPERTY)) {
                server = configurationReader.read(SERVER_PROPERTY, JettyServer.class);
            } else {
                server = new JettyServer();
            }

            server.build();

            server.addEventListener(new BeanManagerResourceBindingListener());
            server.addEventListener(new Listener());

            server.start();

            if(configurationReader.hasProperty(DATASOURCES_PROPERTY)) {
                dataSourcesConfig = configurationReader.read(DATASOURCES_PROPERTY, new TypeReference<List<DataSourceConfig>>() {});
            }

            for (DataSourceConfig dsConfig: dataSourcesConfig) {
                if(dsConfig.getType().equals(DataSourceType.NON_XA)) {
                    DataSource ds = HikariDataSourceFactory.createDataSource(dsConfig);
                    server.addDataSource(dsConfig.getJndiName(), ds);
                } else {
                    throw new UnsupportedConfigurationException("Only NON_XA datasources are currently supported.");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
