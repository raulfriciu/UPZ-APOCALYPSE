package edu.upc.dsa;

import edu.upc.dsa.db.orm.dao.*;
import edu.upc.dsa.exception.EmailUsedException;
import edu.upc.dsa.exception.IncorrectPasswordException;
import edu.upc.dsa.exception.UserNotRegisteredException;
import edu.upc.dsa.models.*;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

public class GameManagerImpl implements GameManager {
    private static Map<String, User> HMUser = new HashMap<>();
    protected HashMap<String, Denuncia> lDenuncias  = new HashMap<String, Denuncia>();

    private static GameManager instance;
    final static Logger logger = Logger.getLogger(GameManagerImpl.class);

    protected List<User> users;
    protected List<Item> items;
    protected List<Question> LQuestions = new ArrayList<>();
    protected List<User> logged;

    public List<User> getUsers() {
        return users;
    }

    private GameManagerImpl() {
        this.users = new LinkedList<>();
        this.items = new LinkedList<>();
        this.logged = new LinkedList<>();
        HMUser = new HashMap<>();

    }

    public static GameManager getInstance() {
        if (instance == null) instance = new GameManagerImpl();
        return instance;
    }
/*
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

 */

    public int size() {
        int ret = this.users.size();
        logger.info("size " + ret);

        return ret;
    }

    //REGISTRAR USUARIO, datos del html, excepcion email
    public int registrarUser(User user) {
        IUserDAO usuarioDAO = new UserDAOImpl();
        List<User> lUsuarios = usuarioDAO.getUsers();
        for (User u : lUsuarios) {
            if (u.getEmail().equals(user.getEmail())){
                return 1;
            }
        }
        int res = usuarioDAO.addUser(user.getName(),user.getEmail(),user.getPassword());
        if (res == 0) {
            logger.info("Registrado correctamente en la base de datos");
            return res;
        }
        return 0;
    }

    //LOGIN USUARIO, login user sin credenciales OK, excepcion password y sin registrar
    public User login(Credenciales credenciales) throws UserNotRegisteredException, IncorrectPasswordException {
        try {
            IUserDAO userDAO = new UserDAOImpl();
            HashMap<String, String> credentialsHash = new HashMap<>();
            credentialsHash.put("email", credenciales.getEmail());
            credentialsHash.put("password", credenciales.getPassword());
            User userLogIn = userDAO.getUserByEmail(credenciales.getEmail());
            logger.info(userLogIn.getEmail());
            logger.info(userLogIn.getPassword());
            if (userLogIn.getPassword().equals(credenciales.getPassword())) {
                logger.info("Successful login " + credenciales.getEmail());
                return userLogIn;
            } else if (userLogIn.getEmail() == null) {
                logger.info("User not registered");
                throw new UserNotRegisteredException();
            }
        } catch (Exception e) {
            logger.info("Error");
        }
        logger.warn("Incorrect password");
        throw new IncorrectPasswordException();
    }

    //LISTA OBJETOS, selecciona lista de la database
    public List<Item> Shop() {
        IItemDAO itemDAO = new ItemDAOImpl();
        List<Item> lItemDAO = itemDAO.getItems();
        logger.info("Lista Objetos Correcta");
        return lItemDAO;
    }

    //DELETE USUARIO, elimina el usuario con el email y la contraseña recibidos
    public void deleteUser(Credenciales credenciales) throws UserNotRegisteredException, IncorrectPasswordException {
        try {
            IUserDAO userDAO = new UserDAOImpl();
            User userToDelete = userDAO.getUserByEmail(credenciales.getEmail());

            if (userToDelete == null || !userToDelete.getEmail().equals(credenciales.getEmail())) {
                logger.info("User not registered");
                throw new UserNotRegisteredException();
            } else if (userToDelete.getPassword().equals(credenciales.getPassword())) {
                userDAO.deleteUser(credenciales.getEmail());
                logger.info("User successfully deleted: " + credenciales.getEmail());
            } else {
                logger.warn("Incorrect password for user: " + credenciales.getEmail());
                throw new IncorrectPasswordException();
            }
        } catch (EmailUsedException e) {
            logger.error("EmailUsedException occurred: " + e.getMessage());
            throw new UserNotRegisteredException();
        }
    }


    //AÑADIR DENUNCIA, al mapa lDenuncias
    public void addDenuncia(Denuncia denuncia){
        lDenuncias.put(denuncia.getSender(), denuncia);
        logger.info("Denuncia añadida");
    }

    //GET DENUNCIA, devuelve una lista de todas las denuncias almacenadas en lDenuncias
    public List<Denuncia> getDenuncias(){
        return new ArrayList<>(this.lDenuncias.values());
    }

    //LISTA FAQ, lista protegida de preguntas frecuentes
    protected List<FAQ> PreguntasFrequentes = new ArrayList<>();

    //GET FAQ, devuelve una nueva lista con FAQ almacenadas en PreguntasFrequentes
    public List<FAQ> getPreguntasFrequentes() {
        return new ArrayList<>(this.PreguntasFrequentes);
    }

    //AÑADE FAQ, a lista de FAQ a la lista PreguntasFrequentes
    public void addPreguntasFrequentes(List<FAQ> faqs) {
        this.PreguntasFrequentes.addAll(faqs);
        logger.info("Pregunta añadida");
    }

    //AÑADIR CONSULTA, a la lista LQuestions, devuelve 0
    public int realizarConsulta(Question p) {
        this.LQuestions.add(p);
        logger.info("Consulta Realizada");
        return 0;
    }

    //LISTA CONSULTAS, devuelve una lista de todas las consultas almacenadas en LQuestions
    public List<Question> getLQuestions() {
        return new ArrayList<>(this.LQuestions);
    }

}