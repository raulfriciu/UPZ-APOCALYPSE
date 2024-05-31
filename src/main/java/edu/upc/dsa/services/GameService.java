package edu.upc.dsa.services;


import edu.upc.dsa.GameManager;
import edu.upc.dsa.GameManagerImpl;
import edu.upc.dsa.db.orm.dao.IUserDAO;
import edu.upc.dsa.exception.EmailUsedException;
import edu.upc.dsa.exception.IncorrectPasswordException;
import edu.upc.dsa.exception.MoneyException;
import edu.upc.dsa.exception.UserNotRegisteredException;
import edu.upc.dsa.models.Credenciales;
import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/game", description = "Endpoint to Game Service")
@Path("/game")
public class GameService {
    final static Logger logger = Logger.getLogger(GameService.class);
    private GameManager gm;
    private IUserDAO userDAO;

    public GameService() throws EmailUsedException {
        this.gm = GameManagerImpl.getInstance();

        /*if (gm.findAll().size()==0) {
            this.gm.registrarUser(new User("Juan","juan356@gmail.com", "pWmJ85"));
            this.gm.registrarUser(new User("Pedro","pedritoperales@yahoo.com" ,"PLANQE77777DFjfhhh"));
            this.gm.registrarUser(new User("Antonio", "antonio5perez@hotmail.com","85difhhfffff"));
        }

         */
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
        try {
            this.gm.registrarUser(new User(user.getName(), user.getEmail(), user.getPassword()));
            return Response.status(201).entity(user).build();
        } catch (EmailUsedException e) {
            e.printStackTrace();
            return Response.status(404).entity(user).build();
        }

    }

    @POST
    @ApiOperation(value = "User login", notes = "log in using credentials")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = User.class),
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
        }
    }

    @GET
    @ApiOperation(value = "Lista de objetos", notes = "View items")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Item.class, responseContainer = "List"),
    })
    @Path("/tienda/objetos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getShop() {

        List<Item> items = this.gm.Shop();
        GenericEntity<List<Item>> entity = new GenericEntity<List<Item>>(items) {
        };
        return Response.status(201).entity(entity).build();
    }
    @PUT
    @ApiOperation(value = "Comprar objeto", notes = "Buy items")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 403, message = "No tienes suficiente dinero")
    })
    @Path("/tienda/comprarObjeto/{idItem}/{idUser}")
    public Response buyItems(@PathParam("idItem") String idItem, @PathParam("idUser") String idUser) {
        try {
            this.userDAO.buyItem(idItem, idUser);
            return Response.status(201).build();
        } catch (MoneyException e) {
            return Response.status(403).build();
        }
    }
}
