package me.sigtrap.embeddedee.core.server;

import me.sigtrap.embeddedee.core.config.exceptions.InvalidConfigurationException;
import me.sigtrap.embeddedee.core.util.ClassPathUtil;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.jndi.Resource;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.ListenerHolder;
import org.eclipse.jetty.servlet.Source;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Slf4jLog;
import org.eclipse.jetty.util.thread.MonitoredQueuedThreadPool;
import org.eclipse.jetty.webapp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestListener;
import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class JettyServer extends AbstractServer {

    private static final String CONTAINER_INCLUDED_JAR_PATTERN = "org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern";
    private static final String CLASSES_PATTERN = "^.*/classes/.*$";
    private static final String JAR_PATTERN = "^([\\\\/]lib[\\\\/].*\\.jar|.*\\.jar[^\\/]*)*$";
    private Logger logger = LoggerFactory.getLogger(JettyServer.class.getName());

    private Server server;
    private WebAppContext appContext;
    private HttpConfiguration httpConfiguration;
    private boolean built = false;

    public void start() {
        logger.info("Starting Jetty at URL {}.", this.getServerUrl());
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void build() {
        server = buildJettyServer();
        appContext = buildWebAppContext();
        server.setHandler(appContext);
        built = true;
    }

    @Override
    public void addDataSource(String jndiName, DataSource dataSource) {
        try {
            Resource resource = new Resource(jndiName, dataSource);
            appContext.setAttribute(jndiName, resource);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create datasource: " + jndiName + ".", e);
        }
    }

    public void addEventListener(ServletContextListener listener) {
        appContext.addEventListener(listener);
    }

    public void addRequestListener(ServletRequestListener listener) {
        appContext.addEventListener(listener);
    }

    private Server buildJettyServer() {

        try {
            Log.setLog(new Slf4jLog());
        } catch (Exception e) {
            logger.error("Failed to set jetty's slf4j logger.");
        }

        MonitoredQueuedThreadPool threadPool = new MonitoredQueuedThreadPool();
        threadPool.setMinThreads(this.getMinThreads());
        threadPool.setMaxThreads(this.getMaxThreads());

        Server server = new Server(threadPool);
        server.addBean(getServerClassList());

        server.setConnectors(new Connector[] {createHttpConnector(server)});
        server.setStopAtShutdown(true);
        return server;
    }

    private WebAppContext buildWebAppContext() {
        //http://www.eclipse.org/jetty/documentation/9.4.x/configuring-webapps.html
        WebAppContext appContext = new WebAppContext();

        String pattern = ClassPathUtil.isRunningExploded()?CLASSES_PATTERN:JAR_PATTERN;

        appContext.setAttribute(CONTAINER_INCLUDED_JAR_PATTERN, pattern);
        appContext.setContextPath(getServerContextPath());
        appContext.setResourceBase(this.getWebAppResourceBase());
        appContext.setParentLoaderPriority(true);

        return appContext;
    }

    private Configuration.ClassList getServerClassList() {
        Configuration.ClassList classList = new Configuration.ClassList();
        classList.addAll(
            Arrays.asList(
                AnnotationConfiguration.class.getName(),
                WebInfConfiguration.class.getName(),
                WebXmlConfiguration.class.getName(),
                MetaInfConfiguration.class.getName(),
                FragmentConfiguration.class.getName(),
                JettyWebXmlConfiguration.class.getName(),
                EnvConfiguration.class.getName(),
                PlusConfiguration.class.getName()
            )
        );
        return classList;
    }

    private ServerConnector createHttpConnector(Server server) {

        if(getHttpConfiguration() ==null) {
            logger.info("HTTP configuration not set. Loading defaults.");
            HttpConfiguration httpConfiguration = new HttpConfiguration();
            httpConfiguration.setSendServerVersion(false);
        }

        HttpConnectionFactory http = new HttpConnectionFactory(getHttpConfiguration());
        ServerConnector httpConnector = new ServerConnector(server, http);
        httpConnector.setPort(this.getHttpPort());
        try {
            httpConnector.setHost(new URL(this.getServerUrl()).getHost());
        } catch (MalformedURLException e) {
            logger.error("Malformed URL {}", this.getServerUrl());
            httpConnector.setHost("localhost");
        }

        return httpConnector;
    }

    public void setPort(int httpPort) {
        if(!built) {
            this.setHttpPort(httpPort);
        } else {
            throw new IllegalStateException("Cannot set port after Jetty Server has been built.");
        }
    }

    public HttpConfiguration getHttpConfiguration() {
        return httpConfiguration;
    }

    public void setHttpConfiguration(HttpConfiguration httpConfiguration) {
        this.httpConfiguration = httpConfiguration;
    }

    private String getWebAppResourceBase() {
        try {

            if(this.getWebRoot()!=null) {
                if(Files.isDirectory(Paths.get(this.getWebRoot()))) {
                    return this.getWebRoot();
                } else {
                    throw new InvalidConfigurationException(String.format("Web root path %s not found.", this.getWebRoot()));
                }
            }

            //check if jar has a webapp dir
            URL webApp = JettyServer.class.getClassLoader().getResource("webapp");
            if(webApp!=null && Files.isDirectory(Paths.get(webApp.toURI()))) {
                return webApp.toExternalForm();
            }

            //check if there is a webapp dir on the current dir
            Path webAppPath = Paths.get("./webapp");
            if(Files.isDirectory(webAppPath)) {
                return webAppPath.toString();
            }

            //check if running on maven
            URL rootResource = JettyServer.class.getClassLoader().getResource(".");
            //if rootResource is null we are running inside a jar
            if(rootResource!=null) {
                Path classes = Paths.get(rootResource.toURI());
                if(Files.isDirectory(classes)) {
                    Path target = classes.getParent();
                    if(target.getFileName().toString().endsWith("target")) {
                        webAppPath = target.resolveSibling(Paths.get("src", "main", "webapp"));
                        if(Files.isDirectory(webAppPath)) {
                            return webAppPath.toString();
                        }
                    }
                }
            }

            throw new IllegalStateException("Unable to find webapp diretory.");

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
