package fr.hunh0w.polynotes.resources;


import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.hunh0w.polynotes.auth.TokenGenerator;
import fr.hunh0w.polynotes.auth.crypto.CryptoManager;
import fr.hunh0w.polynotes.auth.models.LoginModel;
import fr.hunh0w.polynotes.entities.User;
import fr.hunh0w.polynotes.auth.models.RegisterModel;
import fr.hunh0w.polynotes.utils.JsonUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestQuery;

import java.util.Locale;

@Path("/")
@RequestScoped
public class AuthResource {

    /* function(@Context SecurityContext securityContext){
         Principal caller =  ctx.getUserPrincipal();
         String name = caller == null ? "anonymous" : caller.getName();
         boolean hasJWT = jwt.getClaimNames() != null;
    */

    @Inject
    JsonWebToken jwt;

    /**
     *
     * @param loginModel
     * @return
     * @throws Exception
     */
    @POST
    @PermitAll
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginModel loginModel) {
        if(loginModel == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        String error = loginModel.getError();
        if(error != null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(JsonUtils.getErrorMessage(error))
                    .build();
        }

        User user = User
                .find("{'email': ?1, 'password': ?2}", loginModel.email.toLowerCase(Locale.ROOT), CryptoManager.sha384(loginModel.password))
                .firstResult();

        if(user == null)
            return Response.status(Response.Status.UNAUTHORIZED).build();

        String token = TokenGenerator.getToken(user);
        return Response.ok()
                .header("Authorization", "Bearer "+token)
                .header("Access-Control-Expose-Headers", "Authorization")
                .build();
    }

    /**
     * TODO: Spam & CSRF Protection
     * TODO: Email confirmation
     *
     * @param registerModel
     * @return
     */
    @Path("/register")
    @PermitAll
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(RegisterModel registerModel){
        if(registerModel == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        String error = registerModel.getError();
        if(error != null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(JsonUtils.getErrorMessage(error))
                    .build();
        }

        User user = User.findByRegisterModel(registerModel);
        if(user != null)
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(JsonUtils.getErrorMessage("User already exists"))
                    .build();

        user = new User(registerModel.firstname, registerModel.lastname, registerModel.email, registerModel.password);
        user.persist();

        System.out.println(TokenGenerator.getToken(user));
        return Response.status(Response.Status.CREATED).build();
    }

}