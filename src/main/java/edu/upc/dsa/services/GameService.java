package edu.upc.dsa.services;

import edu.upc.dsa.GameManager;
import edu.upc.dsa.GameManagerImpl;
import edu.upc.dsa.db.orm.dao.*;

import edu.upc.dsa.exception.*;
import edu.upc.dsa.models.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.LinkedList;
import java.util.List;

@Api(value = "/game", description = "Endpoint to Game Service")
@Path("/game")
public class GameService {
    final static Logger logger = Logger.getLogger(GameService.class);
    private GameManager gm;
    private IUserDAO userDAO;
    private IItemDAO itemDAO;

    public GameService() throws EmailUsedException {
        this.gm = GameManagerImpl.getInstance();
        this.userDAO = new UserDAOImpl(); // Inicialización manual
        this.itemDAO=new ItemDAOImpl();
        List<FAQ> preguntasFrequentes = new LinkedList<>();
        preguntasFrequentes.add(new FAQ("¿Como se llaman los creadores?", "Pere, Andrea y Raul"));
        preguntasFrequentes.add(new FAQ("¿Porque crearon este juego?", "Nos gusta programar"));
        gm.addPreguntasFrequentes(preguntasFrequentes);

        /*if (gm.findAll().size()==0) {
            this.gm.registrarUser(new User("Juan","juan356@gmail.com", "pWmJ85"));
            this.gm.registrarUser(new User("Pedro","pedritoperales@yahoo.com" ,"PLANQE77777DFjfhhh"));
            this.gm.registrarUser(new User("Antonio", "antonio5perez@hotmail.com","85difhhfffff"));
        }*/
    }

    @GET
    @ApiOperation(value = "Lista de usuarios", notes = "View usuarios")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = User.class, responseContainer = "List"),
    })
    @Path("/usuarios/lUsuarios")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {

        List<User> users = userDAO.getUsers();
        GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users) {};
        return Response.status(201).entity(entity).build();
    }

    @POST
    @ApiOperation(value = "Registrar usuario", notes = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User successfully registered", response = User.class),
            @ApiResponse(code = 404, message = "El email ya está registrado"),
            @ApiResponse(code = 403, message = "Empty credentials"),
            @ApiResponse(code = 500, message = "Error inesperado")
    })
    @Path("/usuarios/register")
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(User user) {
        if (user.getName() == null || user.getName().isEmpty() ||
                user.getEmail() == null || user.getEmail().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty()) {
            return Response.status(403).entity(user).build();
        }
        int n = this.gm.registrarUser(user);
        if (n==1) return Response.status(404).build();
        if (n==0) return Response.status(201).build();

        return Response.status(500).build();
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
    @ApiOperation(value = "Comprar Objeto", notes = "Buy items")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 403, message = "No tienes suficiente dinero"),
            @ApiResponse(code = 409, message = "Objeto ya en el inventario"),
            @ApiResponse(code = 503, message = "Error")


    })
    @Path("/tienda/comprarObjeto/{email}/{idItem}")
    public Response BuyObject(@PathParam("email") String email, @PathParam("idItem") int idItem) {

        try {
            User user = userDAO.getUserByEmail(email);
            int error = itemDAO.buyItemForUser(user, idItem);
            if (error == 0) {
                return Response.status(201).build();
            } else if (error == 6) {
                return Response.status(403).build();
            }else if (error == 9) {
                return Response.status(409).build();
            }else
                return Response.status(503).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(503).build();
        }

    }

    @PUT
    @ApiOperation(value = "Cancelar Compra", notes = "Cancel item purchase")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Compra cancelada exitosamente"),
            @ApiResponse(code = 404, message = "Objeto no encontrado en el inventario"),
            @ApiResponse(code = 503, message = "Error")
    })
    @Path("/tienda/cancelarCompra/{email}/{idItem}")
    public Response cancelarCompra(@PathParam("email") String email, @PathParam("idItem") int idItem) {
        try {
            User user = userDAO.getUserByEmail(email);
            int error = itemDAO.cancelItemForUser(user, idItem);
            if (error == 0) {
                return Response.status(200).build();
            } else if (error == 8) {
                return Response.status(404).build();
            } else {
                return Response.status(503).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(503).build();
        }
    }

    @GET
    @ApiOperation(value = "Visualizar inventario", notes = "Inventario")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Item.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Error en los datos"),
            @ApiResponse(code = 503, message = "Exception")

    })
    @Path("/inventory/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInventory(@PathParam("email") String email) {
        try {
            List<Item> inventory = this.itemDAO.getInventory(email);
            if (inventory == null) {
                return Response.status(401).build();
            }
            GenericEntity<List<Item>> entity = new GenericEntity<List<Item>>(inventory) {};
            return Response.status(201).entity(entity).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(503).build();
        }

    }

    @POST
    @ApiOperation(value = "Eliminar usuario", notes = "Delete user using credentials")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "Usuario no encontrado"),
            @ApiResponse(code = 401, message = "Incorrect credentials")
    })
    @Path("/usuarios/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(Credenciales credenciales) throws IncorrectPasswordException, UserNotRegisteredException {
        try {
            this.gm.deleteUser(credenciales);
            return Response.status(201).build();
        } catch (IncorrectPasswordException e) {
            return Response.status(401).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }

    @PUT
    @ApiOperation(value = "Actualizar usuario", notes = "Update user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "Usuario no encontrado")

    })
    @Path("/usuarios/actualizar/{email}/{name}/{password}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(
            @PathParam("email") String email,
            @PathParam("name") String name,
            @PathParam("password") String password) {
        try {
            this.userDAO.updateUser(email, name, password);
            return Response.status(201).build();
        } catch (SQLException e) {
            return Response.status(404).build();
        }
    }

    @GET
    @ApiOperation(value = "Lista de denuncias", notes = "devuelve la lista de denuncias")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Éxito", response = Denuncia.class, responseContainer="List"),
    })
    @Path("/denuncia/getDenuncias")
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
    @Path("/denuncia/addDenuncia")
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

    @GET
    @ApiOperation(value = "get all FAQs", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = FAQ.class, responseContainer="List"),
    })

    @Path("/FAQs")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPreguntasFrequentes() {
        List<FAQ> faqs = this.gm.getPreguntasFrequentes();

        GenericEntity<List<FAQ>> entity = new GenericEntity<List<FAQ>>(faqs) {};
        return Response.status(201).entity(entity).build();
    }
}

