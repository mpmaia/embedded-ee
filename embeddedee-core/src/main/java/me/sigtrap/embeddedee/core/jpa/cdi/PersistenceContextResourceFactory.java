package me.sigtrap.embeddedee.core.jpa.cdi;

import me.sigtrap.embeddedee.core.config.exceptions.UnsupportedConfigurationException;
import me.sigtrap.embeddedee.core.datasources.DataSourceType;
import org.jboss.weld.injection.spi.ResourceReference;
import org.jboss.weld.injection.spi.ResourceReferenceFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SynchronizationType;

public class PersistenceContextResourceFactory implements ResourceReferenceFactory<EntityManager> {

    private String unitName;
    private EntityManagerFactory emf;
    private SynchronizationType synchronizationType;
    private DataSourceType dataSourceType;

    public PersistenceContextResourceFactory(String unitName, EntityManagerFactory emf, DataSourceType dataSourceType, SynchronizationType synchronizationType) {
        this.unitName = unitName;
        this.emf = emf;
        this.synchronizationType = synchronizationType;
        this.dataSourceType = dataSourceType;
    }

    @Override
    public ResourceReference<EntityManager> createResource() {

        EntityManagerResource em = null;

        if (dataSourceType == DataSourceType.XA) {
            throw new UnsupportedConfigurationException("XA DataSources are not supported yet.");
        } else {
            em = new ResourceLocalEntityManagerResource(emf);
        }

        return new PersistenceContextResource(em);
    }
}
