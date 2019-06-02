package me.sigtrap.embeddedee.core.jpa.servlet;

import me.sigtrap.embeddedee.core.jpa.EntityManagerRequestHolder;

import javax.persistence.EntityManager;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

public class ResourceLocalOpenSessionInView implements ServletRequestListener {

    public void requestDestroyed(ServletRequestEvent event) {
        EntityManager em = EntityManagerRequestHolder.get();
        if(em!=null) {
            em.close();
            EntityManagerRequestHolder.clear();
        }
    }

    public void requestInitialized(ServletRequestEvent event) {

    }
}
