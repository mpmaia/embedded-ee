package me.sigtrap.embeddedee.core.datasources;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class HikariDataSourceFactory {

    public static DataSource createDataSource(DataSourceConfig dataSourceConfig) {

        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setIdleTimeout(dataSourceConfig.getMaxIdleTime());
        dataSource.setMaxLifetime(dataSourceConfig.getMaxLifeTime());
        dataSource.setConnectionTimeout(dataSourceConfig.getConnectionTimeout());
        dataSource.setValidationTimeout(dataSourceConfig.getValidationTimeout());

        dataSource.setMaximumPoolSize(dataSourceConfig.getMaxPoolSize());
        dataSource.setMinimumIdle(dataSourceConfig.getMinPoolSize());

        dataSource.setConnectionInitSql(dataSourceConfig.getConnectionInitSql());
        dataSource.setConnectionTestQuery(dataSourceConfig.getConnectionTestQuery());

        dataSource.setDriverClassName(dataSourceConfig.getDriverClassName());
        dataSource.setJdbcUrl(dataSourceConfig.getJdbcUrl());
        dataSource.setUsername(dataSourceConfig.getUsername());
        dataSource.setPassword(dataSourceConfig.getPassword());

        dataSource.setDataSourceJNDI(dataSourceConfig.getJndiName());

        dataSource.setPoolName(dataSourceConfig.getName());

        try {
            dataSource.setLoginTimeout((int)dataSourceConfig.getLoginTimeout());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return dataSource;
    }
}
