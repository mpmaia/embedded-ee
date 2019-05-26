package me.sigtrap.embeddedee.core.jpa.cdi;

import org.jboss.weld.injection.spi.ResourceReference;

import javax.persistence.EntityManagerFactory;

public class PersistenceUnitResource implements ResourceReference<EntityManagerFactory> {

    private EntityManagerFactory emf;

    public PersistenceUnitResource(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public EntityManagerFactory getInstance() {
        return emf;
    }

    @Override
    public void release() {}
}
