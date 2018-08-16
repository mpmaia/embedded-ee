package me.sigtrap.embeddedee.core.datasources;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class HikariDataSourceFactory {

    public static DataSource createDataSource(HikariConfig config) {
        HikariDataSource dataSource = new HikariDataSource(config);
        return dataSource;
    }
}
