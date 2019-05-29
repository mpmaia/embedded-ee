package me.sigtrap.embeddedee.sample.resources;

import me.sigtrap.embeddedee.sample.dao.UserDAO;
import me.sigtrap.embeddedee.sample.model.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("test")
@RequestScoped
public class TestResource {


    @Inject
    private UserDAO userDAO;

    @GET
    public String test() {
        return "Test";
    }

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsers() {
        return userDAO.list();
    }
}
