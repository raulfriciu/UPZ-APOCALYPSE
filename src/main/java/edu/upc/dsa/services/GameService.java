package edu.upc.dsa.services;


import edu.upc.dsa.GameManager;
import edu.upc.dsa.GameManagerImpl;
import edu.upc.dsa.exception.EmailUsedException;
import edu.upc.dsa.exception.IncorrectPasswordException;
import edu.upc.dsa.exception.UserNotRegisteredException;
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
import org.apache.log4j.Logger;

@Api(value = "/game", description = "Endpoint to Game Service")
@Path("/game")
public class GameService {
    final static Logger logger = Logger.getLogger(GameService.class);
    private GameManager gm;

    public GameService() throws EmailUsedException {
        this.gm = GameManagerImpl.getInstance();
        /*if (gm.findAll().size()==0) {
            this.gm.registrarUser(new User("Juan","juan356@gmail.com", "pWmJ85"));
            this.gm.registrarUser(new User("Pedro","pedritoperales@yahoo.com" ,"PLANQE77777DFjfhhh"));
            this.gm.registrarUser(new User("Antonio", "antonio5perez@hotmail.com","85difhhfffff"));
        }

         */
    }

    @GET
    @ApiOperation(value = "get all Users", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = User.class, responseContainer="List"),
    })
    @Path("/usuarios")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuarios() {
        List<User> Users = this.gm.getallusers();
        GenericEntity<List<User>> entity = new GenericEntity<List<User>>(Users) {};
        return Response.status(201).entity(entity).build();
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
        if (user.getName() == null || user.getName().isEmpty() ||
                user.getEmail() == null || user.getEmail().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty()) {
            return Response.status(500).entity(user).build();
        }
        try{
            this.gm.registrarUser(new User(user.getName(), user.getEmail(), user.getPassword()));
            return Response.status(201).entity(user).build();
        }
        catch (EmailUsedException e){
            e.printStackTrace();
            return Response.status(404).entity(user).build();
        }

    }

    @POST
    @ApiOperation(value = "User login", notes = "log in using credentials")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= User.class),
            @ApiResponse(code = 404, message = "User not registered"),
            @ApiResponse(code = 401, message = "Incorrect credentials")

    })

    @Path("/usuarios/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response Login(Credenciales credenciales) throws IncorrectPasswordException, UserNotRegisteredException {
        try {
            User user = this.gm.Login(credenciales);
            return Response.status(201).entity(user).build();
        } catch (UserNotRegisteredException e) {
            return Response.status(404).build();
        } catch (IncorrectPasswordException e) {
            return Response.status(401).build();
        }}

    @DELETE
    @ApiOperation(value = "delete user", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 301, message = "Contraseña incorrecta")
    })
    @Path("/delete/{email}&{password}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("email") String email, @PathParam("password") String password) {
        Credenciales credenciales = new Credenciales(email, password);
        if (gm.getUser(email) == null) return Response.status(404).build();
        if(this.gm.deleteUser(credenciales) == 1)
            return Response.status(201).build();
        //En caso de un valor inesperado, devolver código de Internal Server Error
        return Response.status(500).build();
    }
    @PUT
    @ApiOperation(value = "Actualizar usuario", notes = "Actualiza la información del usuario")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Actualización exitosa"),
            @ApiResponse(code = 404, message = "Usuario no encontrado"),
            @ApiResponse(code = 301, message = "Contraseña incorrecta"),
            @ApiResponse(code = 5, message = "Correo electrónico ya en uso")
    })
    @Path("/actualizar/{mail}/{newPassword}/{newName}/{newMail}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarUsuario(
            @PathParam("mail") String mail,
            @PathParam("newPassword") String newPassword,
            @PathParam("newName") String newName,
            @PathParam("newMail") String newMail) {
        User usuarioActualizado = this.gm.updateUser(mail, newName, newPassword, newMail);

        if (usuarioActualizado != null) {
            return Response.status(201).entity(usuarioActualizado).build(); // Retornar código 201 para indicar actualización exitosa
        } else {
            return Response.status(404).build(); // Retornar código 404 para indicar que el usuario no fue encontrado
        }
    }
}