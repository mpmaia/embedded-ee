package me.sigtrap.embeddedee.core.jpa.cdi;

import org.jboss.weld.injection.spi.ResourceReference;
import org.jboss.weld.injection.spi.ResourceReferenceFactory;

import javax.persistence.EntityManagerFactory;

public class PersistenceUnitResourceFactory implements ResourceReferenceFactory<EntityManagerFactory> {

    private EntityManagerFactory emf;

    public PersistenceUnitResourceFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public ResourceReference<EntityManagerFactory> createResource() {
        return new PersistenceUnitResource(emf);
    }
}
