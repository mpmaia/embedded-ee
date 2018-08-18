package me.sigtrap.embeddedee.sample.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("test")
public class TestResource {

    @GET
    public String test() {
        return "Test";
    }
}
