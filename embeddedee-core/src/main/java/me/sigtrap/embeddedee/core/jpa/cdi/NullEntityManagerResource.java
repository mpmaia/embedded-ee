package me.sigtrap.embeddedee.core.jpa.cdi;

import javax.persistence.EntityManager;

public class NullEntityManagerResource implements EntityManagerResource  {
    @Override
    public EntityManager get() {
        return null;
    }

    @Override
    public void release() {
        //do nothing
    }
}
