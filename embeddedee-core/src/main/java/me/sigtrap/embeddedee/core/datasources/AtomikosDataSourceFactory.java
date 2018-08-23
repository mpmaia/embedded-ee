package me.sigtrap.embeddedee.core.datasources;

import com.atomikos.jdbc.AtomikosDataSourceBean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by Mauricio Maia on 23/08/2018.
 */
public class AtomikosDataSourceFactory {

    public static DataSource createDataSource(DataSourceConfig dataSourceConfig) {

        AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();
        dataSource.setBorrowConnectionTimeout(dataSourceConfig.getBorrowConnectionTimeout());
        dataSource.setMaxIdleTime(dataSourceConfig.getMaxIdleTime());
        dataSource.setMaxLifetime(dataSourceConfig.getMaxLifetime());
        dataSource.setMaxPoolSize(dataSourceConfig.getMaxPoolSize());
        dataSource.setMinPoolSize(dataSourceConfig.getMinPoolSize());
        dataSource.setReapTimeout(dataSourceConfig.getReapTimeout());
        dataSource.setXaDataSourceClassName(dataSourceConfig.getXaDataSourceClassName());
        dataSource.setUniqueResourceName(dataSourceConfig.getName());

        Properties p = new Properties();
        p.setProperty ( "user" , dataSourceConfig.getUser());
        p.setProperty ( "password" , dataSourceConfig.getPassword());
        p.setProperty ( "URL" , dataSourceConfig.getUrl());
        dataSource.setXaProperties(p);

        try {
            dataSource.setLoginTimeout(dataSourceConfig.getLoginTimeout());
        } catch (Exception e) {}

        return dataSource;
    }
}
