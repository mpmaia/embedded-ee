package me.sigtrap.embeddedee.core.datasources;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class HikariDataSourceFactory {

    public static DataSource createDataSource(DataSourceConfig dataSourceConfig) {
        HikariDataSource dataSource = new HikariDataSource();

        //dataSource.set(dataSourceConfig.getBorrowConnectionTimeout());
        dataSource.setIdleTimeout(dataSourceConfig.getMaxIdleTime());
        dataSource.setMaxLifetime(dataSourceConfig.getMaxLifetime());
        dataSource.setMaximumPoolSize(dataSourceConfig.getMaxPoolSize());
        dataSource.setMinimumIdle(dataSourceConfig.getMinPoolSize());
        //dataSource.set(dataSourceConfig.getReapTimeout());
        dataSource.setDriverClassName(dataSourceConfig.getDriverClassName());

        dataSource.setJdbcUrl(dataSourceConfig.getUrl());
        dataSource.setUsername(dataSourceConfig.getUser());
        dataSource.setPassword(dataSourceConfig.getPassword());

        dataSource.setDataSourceJNDI(dataSourceConfig.getJndiName());
        dataSource.setPoolName(dataSourceConfig.getName());

        try {
            dataSource.setLoginTimeout(dataSourceConfig.getLoginTimeout());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataSource;
    }
}
