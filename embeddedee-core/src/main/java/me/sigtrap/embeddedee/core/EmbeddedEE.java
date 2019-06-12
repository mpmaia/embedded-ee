package me.sigtrap.embeddedee.core;

import com.fasterxml.jackson.core.type.TypeReference;
import me.sigtrap.embeddedee.core.config.ConfigurationReader;
import me.sigtrap.embeddedee.core.config.ConfigurationSource;
import me.sigtrap.embeddedee.core.config.exceptions.UnsupportedConfigurationException;
import me.sigtrap.embeddedee.core.config.sources.FileSystemConfigurationSource;
import me.sigtrap.embeddedee.core.config.yaml.YamlConfigurationReader;
import me.sigtrap.embeddedee.core.config.sources.ClassPathConfigurationSource;
import me.sigtrap.embeddedee.core.datasources.DataSourceConfig;
import me.sigtrap.embeddedee.core.datasources.DataSourceType;
import me.sigtrap.embeddedee.core.datasources.HikariDataSourceFactory;
import me.sigtrap.embeddedee.core.jpa.servlet.ResourceLocalOpenSessionInView;
import me.sigtrap.embeddedee.core.server.JettyServer;
import org.apache.commons.cli.*;
import org.jboss.weld.environment.servlet.BeanManagerResourceBindingListener;
import org.jboss.weld.environment.servlet.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EmbeddedEE {

    private static final String SERVER_PROPERTY = "/server";
    private static final String DATASOURCES_PROPERTY = "/dataSources";
    private static final String PROPERTIES_PROPERTY = "/properties";
    private static final String DEFAULT_CONFIG_FILE = "application.yaml";
    private static final String LOG4J_CONFIG_FILE = "./log4j.properties";
    private static final Logger logger = LoggerFactory.getLogger(EmbeddedEE.class);

    private static final String OPTION_LOG = "l";
    private static final String OPTION_PORT = "p";
    private static final String OPTION_CONFIG = "c";

    private Integer httpPort;

    public EmbeddedEE() {

    }

    public void start() {
        start(new String[]{});
    }

    public void start(String[] cmdLineArgs) {

        logger.info("Starting EmbeddedEE...");

        JettyServer server;
        Map<String, DataSourceConfig> dataSourcesConfig = Collections.emptyMap();
        CommandLine cmdLine = parseCommandLine(cmdLineArgs);

        try {

            //loads log4j configuration file
            if(cmdLine.hasOption(OPTION_LOG)) {
                String logConfigPath = cmdLine.getOptionValue(OPTION_LOG);
                if(!Files.exists(Paths.get(logConfigPath))) {
                    System.err.println("Log4j file not found.");
                }
                setLog4JFile(logConfigPath);
            } else {
                if(Files.exists(Paths.get(LOG4J_CONFIG_FILE))) {
                    setLog4JFile(Paths.get(LOG4J_CONFIG_FILE).toFile().getAbsolutePath());
                }
            }

            List<ConfigurationSource> configurationSources = new ArrayList<>();

            //loads application config file
            if(cmdLine.hasOption(OPTION_CONFIG)) {
                String[] configFilePaths = cmdLine.getOptionValues(OPTION_CONFIG);
                for(String configFilePath: configFilePaths) {
                    if(Files.exists(Paths.get(configFilePath))) {
                        configurationSources.add(new FileSystemConfigurationSource(configFilePath));
                    } else {
                        System.err.println(String.format("Config file %s not found.", configFilePath));
                        return;
                    }
                }
            } else {
                if(Files.exists(Paths.get("./" + DEFAULT_CONFIG_FILE))) {
                    configurationSources.add(new FileSystemConfigurationSource("./" + DEFAULT_CONFIG_FILE));
                } else {
                    configurationSources.add(new ClassPathConfigurationSource(DEFAULT_CONFIG_FILE));
                }
            }

            ConfigurationReader configurationReader = new YamlConfigurationReader(configurationSources.toArray(new ConfigurationSource[]{}));

            if(configurationReader.hasProperty(SERVER_PROPERTY)) {
                server = configurationReader.read(SERVER_PROPERTY, JettyServer.class);
            } else {
                server = new JettyServer();
            }

            //overrides default port
            if(cmdLine.hasOption(OPTION_PORT)) {
                String port = cmdLine.getOptionValue(OPTION_PORT);
                server.setHttpPort(Integer.parseInt(port));
            } else if(httpPort!=null) {
                server.setHttpPort(httpPort);
            }

            server.build();

            //Weld listeners
            server.addEventListener(new BeanManagerResourceBindingListener());
            server.addEventListener(new Listener());

            //JPA OpenSessionInView request
            server.addRequestListener(new ResourceLocalOpenSessionInView());

            if(configurationReader.hasProperty(DATASOURCES_PROPERTY)) {
                dataSourcesConfig = configurationReader.read(DATASOURCES_PROPERTY, new TypeReference<Map<String, DataSourceConfig>>() {});
            }

            for (DataSourceConfig dsConfig: dataSourcesConfig.values()) {
                if(dsConfig.getType().equals(DataSourceType.NON_XA)) {
                    DataSource ds = HikariDataSourceFactory.createDataSource(dsConfig);
                    server.addDataSource(dsConfig.getJndiName(), ds);
                } else {
                    throw new UnsupportedConfigurationException("Only NON_XA datasources are currently supported.");
                }
            }

            if(configurationReader.hasProperty(PROPERTIES_PROPERTY)) {
                Map<String, String> properties = configurationReader.read(PROPERTIES_PROPERTY, new TypeReference<Map<String, String>>() {});
                for(String property: properties.keySet()) {
                    System.setProperty(property, properties.get(property));
                }
            }

            server.start();

        } catch (IOException e) {
            logger.error("Failed to start server", e);
        }
    }

    private CommandLine parseCommandLine(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmdLine = parser.parse(getCommandLineOptions(), args );
            return cmdLine;
        } catch(ParseException e) {
            logger.error("Failed to parse command line.", e);
            throw new RuntimeException(e);
        }
    }

    private Options getCommandLineOptions() {
        final Options options = new Options();

        options.addOption(new Option( "h", "help", false, "prints this help message" ));
        options.addOption(new Option(OPTION_LOG, "log", true, "Log4j config file"));
        options.addOption(new Option(OPTION_PORT, "port", true, "Listening port"));
        Option config = new Option(OPTION_CONFIG, "config", true, "Config file");
        config.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(config);

        return options;
    }

    private void setLog4JFile(String file) {
        System.setProperty("log4j.configuration", file);
    }

    public Integer getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(Integer httpPort) {
        this.httpPort = httpPort;
    }
}
