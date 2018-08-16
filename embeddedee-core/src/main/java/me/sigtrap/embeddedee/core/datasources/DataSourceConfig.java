package me.sigtrap.embeddedee.core.datasources;

import com.zaxxer.hikari.HikariConfig;

public class DataSourceConfig extends HikariConfig {

    private DataSourceType type = DataSourceType.NON_XA;

    public DataSourceType getType() {
        return type;
    }

    public void setType(DataSourceType type) {
        this.type = type;
    }
}
