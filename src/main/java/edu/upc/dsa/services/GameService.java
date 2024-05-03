package edu.upc.dsa.services;


import edu.upc.dsa.GameManager;
import edu.upc.dsa.GameManagerImpl;
import edu.upc.dsa.exception.EmailUsedException;
import edu.upc.dsa.models.Credenciales;
import edu.upc.dsa.models.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

@Api(value = "/game", description = "Endpoint to Game Service")
@Path("/game")
public class GameService {
    private GameManager gm;

    public GameService() throws EmailUsedException {
        this.gm = GameManagerImpl.getInstance();
        if (gm.findAll().size()==0) {
            this.gm.registrarUser(new User("Juan","juan356@gmail.com", "pWmJ85"));
            this.gm.registrarUser(new User("Pedro","pedritoperales@yahoo.com" ,"PLANQE77777DFjfhhh"));
            this.gm.registrarUser(new User("Antonio", "antonio5perez@hotmail.com","85difhhfffff"));
        }
    }

    @POST
    @ApiOperation(value = "Registrar usuario", notes = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User successfully registered", response = User.class),
            @ApiResponse(code = 404, message = "This email address is already in use"),
            @ApiResponse(code = 500, message = "Empty credentials")
    })
    @Path("/usuarios/register")
    @Produces(MediaType.APPLICATION_JSON)
    public Response Register(User user) throws EmailUsedException {
        if (user.getName().equals("")  || user.getEmail().equals("") || user.getPassword().equals("")) return Response.status(500).entity(user).build();
        try{
            this.gm.registrarUser(new User(user.getName(), user.getEmail(), user.getPassword()));
            return Response.status(201).entity(user).build();
        }
        catch (EmailUsedException e){
            return Response.status(222).entity(user).build();
        }

    }

    @GET
    @ApiOperation(value = "Listado usuarios", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = User.class, responseContainer = "List"),
    })
    @Path("/usuarios/lUsuarios")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {

        List<User> users = this.gm.findAll();

        GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users) {};
        return Response.status(201).entity(entity).build();

    }

    @DELETE
    @ApiOperation(value = "Eliminar usuario", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 500, message = "User not found")
    })
    @Path("/usuarios/delUser/{name}&{password}")
    public Response deleteUser(@PathParam("name") String name, @PathParam("password") String password) {
        User t = this.gm.getUser(name, password);
        if (t == null) return Response.status(500).build();
        else this.gm.deleteUser(name, password);
        return Response.status(201).build();
    }

    @PUT
    @ApiOperation(value = "Actualizar usuario", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @Path("/usuarios/actualizarUsuario/{email}/{newPassword}/{newName}/{newEmail}")
    public Response updateUser(User user) {

        User t = this.gm.updateUser(user);

        if (t == null) return Response.status(404).build();

        return Response.status(201).build();
    }

}