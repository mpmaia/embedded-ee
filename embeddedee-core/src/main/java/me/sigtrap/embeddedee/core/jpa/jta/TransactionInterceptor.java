package me.sigtrap.embeddedee.core.jpa.jta;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.TransactionRequiredException;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Interceptor
@Priority(Interceptor.Priority.PLATFORM_BEFORE+200)
@Transactional
public class TransactionInterceptor implements Serializable {

    @PersistenceContext
    private EntityManager em;

    @AroundInvoke
    public Object contextInterceptor(InvocationContext ctx) throws Exception {

        Transactional transactional = ctx.getMethod().getAnnotation(Transactional.class);
        if(transactional==null) {
            transactional = ctx.getMethod().getDeclaringClass().getAnnotation(Transactional.class);
        }

        Transactional.TxType txType = transactional.value();
        boolean transactionStarted = false;

        if((txType.equals(Transactional.TxType.REQUIRES_NEW) || txType.equals(Transactional.TxType.REQUIRED)) &&
            !isTransactionActive()) {
            em.getTransaction().begin();
            transactionStarted = true;
        }

        if(txType.equals(Transactional.TxType.MANDATORY) && !isTransactionActive()) {
            throw new TransactionRequiredException("Transaction mandatory on " + ctx.getMethod().getName());
        }

        if((txType.equals(Transactional.TxType.NEVER) || txType.equals(Transactional.TxType.NOT_SUPPORTED)) && isTransactionActive()) {
            throw new IllegalStateException("Transaction not supported on " + ctx.getMethod().getName());
        }

        try {

            Object object = ctx.proceed();

            if(transactionStarted) {
                em.getTransaction().commit();
            }

            return object;

        } catch (Exception e) {
            if(transactionStarted) {

                List<Class> dontRollbackOn = Arrays.asList(transactional.dontRollbackOn());
                List<Class> rollBackOn = Arrays.asList(transactional.rollbackOn());

                if(dontRollbackOn.contains(e.getClass())) {
                    em.getTransaction().commit();
                } else if(rollBackOn.size()==0 || rollBackOn.contains(e.getClass())) {
                    em.getTransaction().rollback();
                } else {
                    em.getTransaction().commit();
                }
            }
            throw e;
        }
    }

    private boolean isTransactionActive() {
        return em.getTransaction().isActive();
    }
}
