package me.sigtrap.embeddedee.core.jpa;

import me.sigtrap.embeddedee.core.datasources.DataSourceConfig;

import javax.persistence.EntityManagerFactory;

public class EntityManagerFactoryHolder {

    private EntityManagerFactory entityManagerFactory;
    private DataSourceConfig dataSourceConfig;

    public EntityManagerFactoryHolder(EntityManagerFactory entityManagerFactory, DataSourceConfig dataSourceConfig) {
        this.entityManagerFactory = entityManagerFactory;
        this.dataSourceConfig = dataSourceConfig;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}