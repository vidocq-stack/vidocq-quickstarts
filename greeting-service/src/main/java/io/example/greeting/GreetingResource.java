package io.example.greeting;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

/**
 * Jakarta REST 4.0 resource (Cassini). The route table is generated at
 * compile time; @RequestScoped is added automatically by Vauban's
 * RestScopeExtension.
 */
@Path("/api/greetings")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
public class GreetingResource {

    @Inject
    GreetingService service;

    /** GET /api/greetings            -> {"message":"Hello, World!","recipient":"World"} */
    /** GET /api/greetings?name=Alice -> {"message":"Hello, Alice!","recipient":"Alice"} */
    @GET
    public Greeting greet(@QueryParam("name") String name) {
        return service.greet(name);
    }

    /** GET /api/greetings/Bob -> {"message":"Hello, Bob!","recipient":"Bob"} */
    @GET
    @Path("/{name}")
    public Greeting greetPath(@PathParam("name") String name) {
        return service.greet(name);
    }
}
