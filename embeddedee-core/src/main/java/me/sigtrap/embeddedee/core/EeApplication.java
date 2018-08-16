package me.sigtrap.embeddedee.core;

import com.fasterxml.jackson.core.type.TypeReference;
import me.sigtrap.embeddedee.core.config.ConfigurationReader;
import me.sigtrap.embeddedee.core.config.ConfigurationSource;
import me.sigtrap.embeddedee.core.config.json.JsonConfigurationReader;
import me.sigtrap.embeddedee.core.config.sources.ClassPathConfigurationSource;
import me.sigtrap.embeddedee.core.datasources.DataSourceConfig;
import me.sigtrap.embeddedee.core.datasources.DataSourceType;
import me.sigtrap.embeddedee.core.datasources.HikariDataSourceFactory;
import me.sigtrap.embeddedee.core.server.JettyServer;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

public class EeApplication {

    public EeApplication() {

    }

    public void start() {

        ConfigurationSource configurationSource = new ClassPathConfigurationSource("application.json");
        ConfigurationReader configurationReader = new JsonConfigurationReader(configurationSource);

        JettyServer server = null;
        List<DataSourceConfig> dataSourcesConfig;
        try {
            server = configurationReader.read("/server", JettyServer.class);
            dataSourcesConfig = configurationReader.read("/dataSources", new TypeReference<List<DataSourceConfig>>() {
            });

            for (DataSourceConfig dsConfig: dataSourcesConfig) {
                if(dsConfig.getType().equals(DataSourceType.NON_XA)) {
                    DataSource ds = HikariDataSourceFactory.createDataSource(dsConfig);
                    server.addDataSource(ds, dsConfig.getDataSourceJNDI());
                } else {
                    //TODO: XA Wrapper
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }



        server.start();
    }
}
