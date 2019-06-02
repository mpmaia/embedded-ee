package me.sigtrap.embeddedee.core.jpa.cdi;

import me.sigtrap.embeddedee.core.jpa.EntityManagerRequestHolder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ResourceLocalEntityManagerResource implements EntityManagerResource {

    private EntityManagerFactory emf;

    public ResourceLocalEntityManagerResource(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager createEntityManager() {
        EntityManager em = emf.createEntityManager();
        //save the entity manager for re-use on the same request
        EntityManagerRequestHolder.put(em);
        return em;
    }

    @Override
    public EntityManager get() {
        if(EntityManagerRequestHolder.get()!=null) {
            return EntityManagerRequestHolder.get();
        } else {
            return createEntityManager();
        }
    }

    @Override
    public void release() {
        //ResourceLocal EntityManagers use the OpenSession in View Pattern. They are closed by the
        //ResourceLocalOpenSessionInView listener at the end of the request.
    }
}
