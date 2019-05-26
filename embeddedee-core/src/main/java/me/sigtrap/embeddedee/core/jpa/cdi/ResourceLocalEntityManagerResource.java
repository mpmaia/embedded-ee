package me.sigtrap.embeddedee.core.jpa.cdi;

import javax.persistence.EntityManager;

public class ResourceLocalEntityManagerResource implements EntityManagerResource {

    private EntityManager em;

    public ResourceLocalEntityManagerResource(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager get() {
        return em;
    }

    @Override
    public void release() {
        if (em.isOpen()) {
            em.close();
        }
    }
}
