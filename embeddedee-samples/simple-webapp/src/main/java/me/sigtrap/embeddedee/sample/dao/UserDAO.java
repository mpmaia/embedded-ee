package me.sigtrap.embeddedee.sample.dao;

import me.sigtrap.embeddedee.sample.model.User;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
public class UserDAO {

    @PersistenceContext
    private EntityManager em;

    public List<User> list() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root);
        TypedQuery<User> query = em.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public User getById(long id) {
        return em.find(User.class, id);
    }

    @Transactional
    public User persist(User u) {
        em.persist(u);
        return u;
    }

    @Transactional
    public  void delete(User u) {
        em.remove(u);
    }
}
