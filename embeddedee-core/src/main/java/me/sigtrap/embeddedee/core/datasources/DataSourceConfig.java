package me.sigtrap.embeddedee.core.datasources;

import static java.util.concurrent.TimeUnit.SECONDS;

public class DataSourceConfig {

    private String name;
    private String jndiName;

    private int minPoolSize = 1;
    private int maxPoolSize = 10;

    private long validationTimeout = SECONDS.toMillis(30);
    private long connectionTimeout = SECONDS.toMillis(30);
    private long loginTimeout = SECONDS.toMillis(30);

    private long maxIdleTime = SECONDS.toMillis(120);
    private long maxLifeTime = SECONDS.toMillis(240);

    private String connectionTestQuery;
    private String connectionInitSql;

    private String username;
    private String password;

    private String jdbcUrl;
    private String driverClassName;
    private DataSourceType type = DataSourceType.NON_XA;

    public DataSourceType getType() {
        return type;
    }

    public void setType(DataSourceType type) {
        this.type = type;
    }

    public int getMinPoolSize() {
        return minPoolSize;
    }

    public void setMinPoolSize(int minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public long getMaxIdleTime() {
        return maxIdleTime;
    }

    public void setMaxIdleTime(long maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    public long getMaxLifeTime() {
        return maxLifeTime;
    }

    public void setMaxLifeTime(long maxLifeTime) {
        this.maxLifeTime = maxLifeTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJndiName() {
        return jndiName;
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public long getValidationTimeout() {
        return validationTimeout;
    }

    public void setValidationTimeout(long validationTimeout) {
        this.validationTimeout = validationTimeout;
    }

    public String getConnectionTestQuery() {
        return connectionTestQuery;
    }

    public void setConnectionTestQuery(String connectionTestQuery) {
        this.connectionTestQuery = connectionTestQuery;
    }

    public String getConnectionInitSql() {
        return connectionInitSql;
    }

    public void setConnectionInitSql(String connectionInitSql) {
        this.connectionInitSql = connectionInitSql;
    }

    public long getLoginTimeout() {
        return loginTimeout;
    }

    public void setLoginTimeout(long loginTimeout) {
        this.loginTimeout = loginTimeout;
    }
}
