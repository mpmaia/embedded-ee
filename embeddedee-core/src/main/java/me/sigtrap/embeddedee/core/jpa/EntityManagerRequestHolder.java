package me.sigtrap.embeddedee.core.jpa;

import javax.persistence.EntityManager;

public class EntityManagerRequestHolder {

    private static final ThreadLocal<EntityManager> threadLocal = new ThreadLocal<>();

    public static void put(EntityManager em) {
        threadLocal.set(em);
    }

    public static EntityManager get() {
        return threadLocal.get();
    }

    public static void clear() {
        threadLocal.remove();
    }
}
