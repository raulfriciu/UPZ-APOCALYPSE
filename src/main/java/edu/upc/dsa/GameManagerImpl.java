package edu.upc.dsa;

import edu.upc.dsa.db.orm.dao.IUserDAO;
import edu.upc.dsa.db.orm.dao.UserDAOImpl;
import edu.upc.dsa.exception.EmailUsedException;
import edu.upc.dsa.exception.IncorrectPasswordException;
import edu.upc.dsa.exception.UserNotRegisteredException;
import edu.upc.dsa.models.Credenciales;
import edu.upc.dsa.models.User;
import org.apache.log4j.Logger;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GameManagerImpl implements GameManager {
    private static GameManager instance;
    final static Logger logger = Logger.getLogger(GameManagerImpl.class);
    private HashMap<String, User> HMUser;
    protected List<User> users;

    private  GameManagerImpl() {
        this.users = new LinkedList<>();
        HMUser = new HashMap<String, User>();
    }
    public static GameManager getInstance() {
        if (instance==null) instance = new GameManagerImpl();
        return instance;
    }

    public int size() {
        int ret = this.users.size();
        logger.info("size " + ret);

        return ret;
    }

    //REGISTRAR USUARIO, datos del html, excepcion email
    @Override
    public User registrarUser(User user) throws EmailUsedException {
        User u = HMUser.get(user.getEmail());
        if (u == null)
        {
            String name= user.getName();
            String email= user.getEmail();
            String password= user.getPassword();
            IUserDAO userDAO = new UserDAOImpl();
            userDAO.addUser(name, email,password );
            logger.info("User registered");
            return user;
        } else
        {
            logger.info("This email is already being used");
            throw new EmailUsedException();
        }
    }

    //LOGIN USUARIO, datos del html, excepcion email
    public User Login(Credenciales credenciales) throws UserNotRegisteredException, IncorrectPasswordException {
        try {
            IUserDAO userDAO = new UserDAOImpl();
            HashMap<String, String> credentialsHash = new HashMap<>();
            credentialsHash.put("email", credenciales.getEmail());
            credentialsHash.put("password", credenciales.getPassword());
            User userLogIn = userDAO.getUserByEmail(credenciales.getEmail());
            logger.info(userLogIn.getEmail());
            logger.info(userLogIn.getPassword());
            if (userLogIn.getPassword().equals(credenciales.getPassword())) {
                logger.info("Succesful login " + credenciales.getEmail());
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
/*
    //LISTA USUARIOS, todos los usuarios
    public List<User> findAll(){
        return users;
    }

    //GET USER, obten usuario por su nombre y constraseña
    public User getUser(String name, String password) {
        logger.info("getUser("+name+")");
        logger.info("getUser("+password+")");
        for (User t: this.users) {
            if (t.getName().equals(name) & t.getPassword().equals(password)) {
                logger.info("getUser("+name+"): "+t);
                logger.info("getUser("+password+"): "+t);

                return t;
            }
        }

        logger.warn("not found " + name );
        return null;
    }

    //DELETE USUARIO, elimina el usuario con el nombre y la contraseña recibidos
    public void deleteUser(String name, String password) {

        User t = this.getUser(name, password);
        if (t==null) {
            logger.warn("not found " + t);
        }
        else logger.info(t+" deleted ");

        this.users.remove(t);

    }

    //GET USER BY EMAIL, obten usuario por email
    public User getUserByEmail(String email) {
        logger.info("getUserByEmail("+email+")");
        for (User t: this.users) {
            if (t.getName().equals(email)) {
                logger.info("getUserByEmail("+email+"): "+t);
                return t;
            }
        }
        logger.warn("not found " + email );
        return null;
    }

    //UPDATE USUARIO, recibe un usuario y actualiza sus datos
    public User updateUser(User p) {
        User t = this.getUserByEmail(p.getEmail());

        if (t!=null) {
            logger.info(p+" rebut!!!! ");

            t.setName(p.getName());
            t.setEmail(p.getEmail());
            t.setPassword(p.getPassword());

            logger.info(t+" updated ");
        }
        else {
            logger.warn("not found "+p);
        }

        return t;
    }

 */
}