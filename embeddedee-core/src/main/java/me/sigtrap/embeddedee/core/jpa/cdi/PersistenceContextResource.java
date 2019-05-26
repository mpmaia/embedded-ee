package me.sigtrap.embeddedee.core.jpa.cdi;

import org.jboss.weld.injection.spi.ResourceReference;

import javax.persistence.EntityManager;

public class PersistenceContextResource implements ResourceReference<EntityManager> {

    private EntityManagerResource emResource;

    public PersistenceContextResource(EntityManagerResource emResource) {
        this.emResource = emResource;
    }

    @Override
    public EntityManager getInstance() {
        return emResource.get();
    }

    @Override
    public void release() {
        emResource.release();
    }
}
