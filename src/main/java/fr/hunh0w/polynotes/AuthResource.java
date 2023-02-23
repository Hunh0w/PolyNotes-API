package fr.hunh0w.polynotes;


import java.security.Principal;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import fr.hunh0w.polynotes.auth.GenerateToken;
import fr.hunh0w.polynotes.auth.User;
import fr.hunh0w.polynotes.auth.models.RegisterModel;
import fr.hunh0w.polynotes.utils.JsonUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jose4j.json.internal.json_simple.JSONObject;

@Path("/")
@RequestScoped
public class AuthResource {

    @Inject
    JsonWebToken jwt;

    @POST
    @PermitAll
    @Path("/login")
    @Produces(MediaType.TEXT_PLAIN)
    public String login(@Context SecurityContext ctx) throws Exception {
        GenerateToken.exec();
        Principal caller =  ctx.getUserPrincipal();
        String name = caller == null ? "anonymous" : caller.getName();
        boolean hasJWT = jwt.getClaimNames() != null;
        if(hasJWT){

        }
        String helloReply = String.format("hello + %s, isSecure: %s, authScheme: %s, hasJWT: %s", name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJWT);
        return helloReply;
    }

    @Path("/register")
    @PermitAll
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(RegisterModel registerModel){

        String error = registerModel.getError();
        if(error != null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(JsonUtils.getErrorMessage(error))
                    .build();
        }

        User user = User.findByRegisterModel(registerModel);
        if(user != null)
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(JsonUtils.getErrorMessage("User already exists"))
                    .build();

        user = new User(registerModel.firstname, registerModel.lastname, registerModel.email, registerModel.password);
        user.persist();
        return Response.status(Response.Status.CREATED).build();
    }

}