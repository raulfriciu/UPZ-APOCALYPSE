package edu.upc.dsa.services;

import edu.upc.dsa.GameManager;
import edu.upc.dsa.GameManagerImpl;
import edu.upc.dsa.db.orm.dao.IInventoryDAO;
import edu.upc.dsa.db.orm.dao.IUserDAO;

import edu.upc.dsa.db.orm.dao.InventoryDAOImpl;
import edu.upc.dsa.db.orm.dao.UserDAOImpl;
import edu.upc.dsa.exception.*;
import edu.upc.dsa.models.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.auth.In;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Api(value = "/game", description = "Endpoint to Game Service")
@Path("/game")
public class GameService {
    final static Logger logger = Logger.getLogger(GameService.class);
    private GameManager gm;
    private IUserDAO userDAO;
    private IInventoryDAO inventoryDAO;

    public GameService() throws EmailUsedException {
        this.gm = GameManagerImpl.getInstance();
        this.userDAO = new UserDAOImpl(); // Inicialización manual
        this.inventoryDAO=new InventoryDAOImpl();

        /*if (gm.findAll().size()==0) {
            this.gm.registrarUser(new User("Juan","juan356@gmail.com", "pWmJ85"));
            this.gm.registrarUser(new User("Pedro","pedritoperales@yahoo.com" ,"PLANQE77777DFjfhhh"));
            this.gm.registrarUser(new User("Antonio", "antonio5perez@hotmail.com","85difhhfffff"));
        }*/
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
    public Response register(User user) throws EmailUsedException, SQLIntegrityConstraintViolationException {
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
        }catch (SQLIntegrityConstraintViolationException e) {
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
    public Response login(Credenciales credenciales) throws IncorrectPasswordException, UserNotRegisteredException {
        try {
            User user = this.gm.login(credenciales);
            return Response.status(201).entity(user).build();
        } catch (UserNotRegisteredException e) {
            return Response.status(404).build();
        } catch (IncorrectPasswordException e) {
            return Response.status(401).build();
        }}

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
            @ApiResponse(code = 403, message = "No tienes suficiente dinero"),
            @ApiResponse(code = 409, message = "Objeto ya en el inventario")
    })
    @Path("/tienda/comprarObjeto/{idItem}/{idUser}")
    public Response buyItems(@PathParam("idItem") int idItem, @PathParam("idUser") int idUser) {
        try {
            userDAO.buyItem(idItem, idUser);
            return Response.status(201).build();
        } catch (MoneyException e) {
            return Response.status(403).build();
        }catch (SQLException e) {
            return Response.status(409).build();
        }
    }

    @GET
    @ApiOperation(value = "Visualizar inventario", notes = "Inventorio")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 401, message = "User has an empty inventory"),
            @ApiResponse(code = 500, message = "SQL Exception")
    })
    @Path("/inventory/{idUser}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInventory(@PathParam("idUser") int idUser) {

        try {
            List<Inventory> inventory = this.inventoryDAO.getInventory(idUser);
            GenericEntity<List<Inventory>> entity = new GenericEntity<List<Inventory>>(inventory) {
            };
            return Response.status(201).entity(entity).build();
        } catch (SQLException e) {
            return Response.status(500).build();
        } catch (NotInInventoryException e) {
            return Response.status(401).build();
        } catch (NonExistentItemException e) {
            throw new RuntimeException(e);
        }

    }

    @DELETE
    @ApiOperation(value = "delete user", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 301, message = "Contraseña incorrecta")
    })
    @Path("/usuarios/delete/{email}/{password}")
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
    @Path("/usuarios/actualizar/{email}/{newPassword}/{newName}/{newMail}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarUsuario(
            @PathParam("email") String mail,
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
    @GET
    @ApiOperation(value = "Lista de denuncias", notes = "devuelve la lista de denuncias")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Éxito", response = Denuncia.class, responseContainer="List"),
    })
    @Path("denuncia/getDenuncias")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDenuncias() {
        List<Denuncia> lDen = gm.getDenuncias();

        GenericEntity<List<Denuncia>> entity = new GenericEntity<List<Denuncia>>(lDen) {};
        return Response.status(201).entity(entity).build();
    }
    @PUT
    @ApiOperation(value = "Enviar denuncia", notes = "Envia una denuncia")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Actualización exitosa")
    })
    @Path("denuncia/addDenuncia")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDenuncia(Denuncia denuncia) {
        gm.addDenuncia(denuncia);
        return Response.status(201).build();
    }
    @POST
    @ApiOperation(value = "Consulta sobre la app", notes = "consulta")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Question.class),
    })
    @Path("/question/addConsulta")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response realizarConsulta(Question consulta) {
        int n = this.gm.realizarConsulta(consulta);
        if (n==0) return Response.status(201).build();

        //En caso de un valor inesperado, devolver código de Internal Server Error
        return Response.status(500).build();
    }
    @GET
    @ApiOperation(value = "get all Questions", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Question.class, responseContainer="List"),
    })
    @Path("/question/getConsultas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConsultas() {
        List<Question> consultas = this.gm.getLQuestions();
        GenericEntity<List<Question>> entity = new GenericEntity<List<Question>>(consultas) {};
        return Response.status(201).entity(entity).build();

    }
}

