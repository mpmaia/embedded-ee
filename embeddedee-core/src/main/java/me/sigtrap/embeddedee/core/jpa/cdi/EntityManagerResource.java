package me.sigtrap.embeddedee.core.jpa.cdi;

import javax.persistence.EntityManager;

public interface EntityManagerResource{
    EntityManager get();
    void release();
}
