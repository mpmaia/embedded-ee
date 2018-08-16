package me.sigtrap.embeddedee.core.server;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.jndi.Resource;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Slf4jLog;
import org.eclipse.jetty.util.thread.MonitoredQueuedThreadPool;
import org.eclipse.jetty.webapp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class JettyServer extends AbstractServer {

    private static final String CONTAINER_INCLUDED_JAR_PATTERN = "org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern";
    private static final String JAR_PATTERN = ".*/classes/.*";
    private Logger logger = LoggerFactory.getLogger(JettyServer.class.getName());

    private Server server;
    private WebAppContext appContext;
    private HttpConfiguration httpConfiguration;

    public void start() {
        logger.info("Starting Jetty at URL %s.", this.getServerUrl());
        try {
            build();
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

    protected void build() {
        server = buildJettyServer();
        appContext = buildWebAppContext();
        server.setHandler(appContext);
    }

    @Override
    public void addDataSource(DataSource dataSource, String jndiName) {
        try {
            Resource resource = new Resource(jndiName, dataSource);
            appContext.setAttribute(jndiName, resource);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create datasource: " + jndiName + ".", e);
        }
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
        appContext.setAttribute(CONTAINER_INCLUDED_JAR_PATTERN, JAR_PATTERN);
        appContext.setContextPath(getServerContextPath());
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
            logger.error("Malformed URL %s", this.getServerUrl());
            httpConnector.setHost("localhost");
        }

        return httpConnector;
    }

    public HttpConfiguration getHttpConfiguration() {
        return httpConfiguration;
    }

    public void setHttpConfiguration(HttpConfiguration httpConfiguration) {
        this.httpConfiguration = httpConfiguration;
    }
}
