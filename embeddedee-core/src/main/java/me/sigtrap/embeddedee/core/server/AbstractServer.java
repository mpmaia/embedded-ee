package me.sigtrap.embeddedee.core.server;

import javax.sql.DataSource;

public abstract class AbstractServer {

    private String serverUrl;
    private Integer minThreads = 16;
    private Integer maxThreads = 128;
    private String serverContextPath = "/";
    private int httpPort = 8080;

    public abstract void start();

    public abstract void stop();

    protected abstract void build();

    public abstract void addDataSource(String jndiName, DataSource dataSource);

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public Integer getMinThreads() {
        return minThreads;
    }

    public void setMinThreads(Integer minThreads) {
        this.minThreads = minThreads;
    }

    public Integer getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(Integer maxThreads) {
        this.maxThreads = maxThreads;
    }

    public String getServerContextPath() {
        return serverContextPath;
    }

    public void setServerContextPath(String serverContextPath) {
        this.serverContextPath = serverContextPath;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }
}
