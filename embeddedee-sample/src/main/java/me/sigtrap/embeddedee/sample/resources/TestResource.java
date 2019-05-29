package me.sigtrap.embeddedee.sample.resources;

import me.sigtrap.embeddedee.sample.dao.UserDAO;
import me.sigtrap.embeddedee.sample.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Path("test")
@RequestScoped
public class TestResource {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserDAO userDAO;

    @GET
    public String test() {
        return "Test";
    }

    @GET
    @Path("/users")
    public List<User> getUsers() {
        userDAO.list();
        return null;
        /*CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root);
        TypedQuery<User> query = em.createQuery(criteriaQuery);
        return query.getResultList();*/
    }
}
