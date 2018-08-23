package me.sigtrap.embeddedee.core;

import com.fasterxml.jackson.core.type.TypeReference;
import me.sigtrap.embeddedee.core.config.ConfigurationReader;
import me.sigtrap.embeddedee.core.config.ConfigurationSource;
import me.sigtrap.embeddedee.core.config.json.JsonConfigurationReader;
import me.sigtrap.embeddedee.core.config.sources.ClassPathConfigurationSource;
import me.sigtrap.embeddedee.core.datasources.AtomikosDataSourceFactory;
import me.sigtrap.embeddedee.core.datasources.DataSourceConfig;
import me.sigtrap.embeddedee.core.datasources.DataSourceType;
import me.sigtrap.embeddedee.core.datasources.HikariDataSourceFactory;
import me.sigtrap.embeddedee.core.server.JettyServer;

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

        ConfigurationSource configurationSource = new ClassPathConfigurationSource("application.json");
        ConfigurationReader configurationReader = new JsonConfigurationReader(configurationSource);

        JettyServer server = null;
        List<DataSourceConfig> dataSourcesConfig = Collections.emptyList();
        try {

            if(configurationReader.hasProperty(SERVER_PROPERTY)) {
                server = configurationReader.read(SERVER_PROPERTY, JettyServer.class);
            } else {
                server = new JettyServer();
            }

            server.start();

            if(configurationReader.hasProperty(DATASOURCES_PROPERTY)) {
                dataSourcesConfig = configurationReader.read(DATASOURCES_PROPERTY, new TypeReference<List<DataSourceConfig>>() {});
            }

            for (DataSourceConfig dsConfig: dataSourcesConfig) {
                if(dsConfig.getType().equals(DataSourceType.NON_XA)) {
                    DataSource ds = HikariDataSourceFactory.createDataSource(dsConfig);
                    server.addDataSource(dsConfig.getJndiName(), ds);
                } else {
                    DataSource ds = AtomikosDataSourceFactory.createDataSource(dsConfig);
                    server.addDataSource(dsConfig.getJndiName(), ds);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
