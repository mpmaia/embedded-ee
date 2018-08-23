package me.sigtrap.embeddedee.core.jpa.jta;

import javax.persistence.EntityManager;
import javax.transaction.Synchronization;

/**
 * Created by Mauricio Maia on 23/08/2018.
 */
public class EntityManagerSynchronization implements Synchronization {

    private EntityManager em;

    public EntityManagerSynchronization(EntityManager em) {
        this.em = em;
    }

    @Override
    public void afterCompletion(int status) {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    @Override
    public void beforeCompletion() {
        //do nothing
    }
}
