package me.sigtrap.embeddedee.core.jpa;

import me.sigtrap.embeddedee.core.datasources.DataSourceType;
import me.sigtrap.embeddedee.core.jpa.cdi.PersistenceContextResourceFactory;
import me.sigtrap.embeddedee.core.jpa.cdi.PersistenceUnitResourceFactory;
import org.jboss.weld.injection.spi.JpaInjectionServices;
import org.jboss.weld.injection.spi.ResourceReferenceFactory;

import javax.annotation.Priority;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

@Priority(0)
public class PersistenceContextInjector implements JpaInjectionServices {

    @Override
    public ResourceReferenceFactory<EntityManager> registerPersistenceContextInjectionPoint
            (InjectionPoint injectionPoint) {

        PersistenceContext pc = injectionPoint.getAnnotated().getAnnotation(PersistenceContext
                .class);

        String unitName = pc.unitName();
        EntityManagerFactoryHolder wrapper = EntityManagerFactoryFactory.getEntityManagerFactory(unitName);

        //TODO: Determinar tipo do DataSource e passar parametro
        return new PersistenceContextResourceFactory(unitName, wrapper.getEntityManagerFactory(),
                DataSourceType.NON_XA, pc.synchronization());
    }

    @Override
    public ResourceReferenceFactory<EntityManagerFactory> registerPersistenceUnitInjectionPoint
            (InjectionPoint injectionPoint) {

        PersistenceUnit pu = injectionPoint.getAnnotated().getAnnotation(PersistenceUnit.class);

        String unitName = pu.unitName();
        EntityManagerFactoryHolder wrapper = EntityManagerFactoryFactory.getEntityManagerFactory(unitName);

        return new PersistenceUnitResourceFactory(wrapper.getEntityManagerFactory());
    }

    /*@Override
    public EntityManager resolvePersistenceContext(InjectionPoint injectionPoint) {
        return null;
    }

    @Override
    public EntityManagerFactory resolvePersistenceUnit(InjectionPoint injectionPoint) {
        return null;
    }*/

    @Override
    public void cleanup() {
    }
}