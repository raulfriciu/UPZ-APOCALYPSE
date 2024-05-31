package edu.upc.dsa;

import edu.upc.dsa.db.orm.dao.IItemDAO;
import edu.upc.dsa.db.orm.dao.IUserDAO;
import edu.upc.dsa.db.orm.dao.ItemDAOImpl;
import edu.upc.dsa.db.orm.dao.UserDAOImpl;
import edu.upc.dsa.exception.EmailUsedException;
import edu.upc.dsa.exception.IncorrectPasswordException;
import edu.upc.dsa.exception.UserNotRegisteredException;
import edu.upc.dsa.models.Credenciales;
import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.Inventory;
import edu.upc.dsa.models.FAQ;
import edu.upc.dsa.models.User;
import org.apache.log4j.Logger;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameManagerImpl implements GameManager {
    private static Map<String, User> HMUser = new HashMap<>();
    private static GameManager instance;
    final static Logger logger = Logger.getLogger(GameManagerImpl.class);

    protected List<User> users;
    protected List<Item> items;
    protected List<User> logged;

    public List<User> getUsers() {
        return users;
    }

    private GameManagerImpl() {
        this.users = new LinkedList<>();
        this.items = new LinkedList<>();
        this.logged = new LinkedList<>();
        HMUser = new HashMap<>();
        HMUser = new HashMap<String, User>();
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
    public int UserNumber() {
        return this.users.size();
    }

    public int LoggedNumber(){return this.logged.size();}


    //REGISTRAR USUARIO, datos del html, excepcion email
    @Override
    public User registrarUser(User user) throws EmailUsedException {
        String email = user.getEmail().trim().toLowerCase();  // Normaliza email
        User u = HMUser.get(email);

        if (u == null) {
            String name = user.getName();

            String password = user.getPassword();
            IUserDAO userDAO = new UserDAOImpl();
            userDAO.addUser(name, email, password);
            HMUser.put(email, user);
            logger.info("User registered");
            return user;
        } else {
            logger.info("This email is already being used");
            throw new EmailUsedException();
        }
    }

    //LOGIN USUARIO, login user sin credenciales OK, excepcion password y sin registrar
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
    //LISTA OBJETOS, selecciona lista de la database
    public List<Item> Shop() {
        IItemDAO itemDAO = new ItemDAOImpl();
        List<Item> lItemDAO = itemDAO.getItems();
        logger.info("Lista Objetos Correcta");
        return lItemDAO;
    }
/*
    //DELETE USUARIO, elimina el usuario con el nombre y la contraseña recibidos
    public int deleteUser(Credenciales credenciales) {
        // Verificar si el usuario existe
        if (HMUser.containsKey(credenciales.getEmail())) {
            User usuario = HMUser.get(credenciales.getEmail());
            // Verificar la contraseña
            if (usuario.getPassword().equals(credenciales.getPassword())) {
                HMUser.remove(credenciales.getEmail());
                logger.info("delete(" + credenciales + ")" + usuario);
                return 1;
            } else {
                logger.warn("Incorrect password for user: " + usuario.getName());
                return 301; // Código para contraseña incorrecta
            }
        } else {
            logger.warn("User with email " + credenciales.getEmail() + " not found");
            return 404; // Código para usuario no encontrado
        }
    }

    //UPDATE USUARIO, recibe un usuario y actualiza sus datos
    public User updateUser(String mail, String newName, String newPassword, String newMail) {
        logger.info("actualizar(" + mail + ")");
        // Verificar si el usuario existe
        if (HMUser.containsKey(mail)) {
            User usuario = HMUser.get(mail);
            // Verificar que la nueva contraseña sea diferente de la contraseña actual
            if (newPassword != null && !newPassword.isEmpty() && !usuario.getPassword().equals(newPassword)) {
                usuario.setPassword(newPassword);
            }
            // Actualizar la información del usuario
            if (newName != null && !newName.isEmpty()) {
                usuario.setName(newName);
            }
            if (newMail != null && !newMail.isEmpty()) {
                // Verificar si el nuevo correo es diferente del anterior
                if (!newMail.equals(mail)) {
                    // Verificar si el nuevo correo ya está en uso por otro usuario
                    if (!HMUser.containsKey(newMail)) {
                        // Eliminar la entrada antigua y agregar la entrada actualizada con la nueva clave
                        HMUser.remove(mail);
                        HMUser.put(newMail, usuario);
                        usuario.setEmail(newMail);  // Actualizar el correo en el objeto usuario
                    } else {
                        logger.warn("El nuevo correo electrónico ya está en uso");
                        return null; // Retornar null para indicar que el nuevo correo ya está en uso
                    }
                }
            }
            logger.info("Usuario actualizado exitosamente: " + usuario.getName());
            return usuario; // Retornar el objeto Usuario actualizado
        } else {
            logger.warn("Usuario con correo electrónico " + mail + " no encontrado");
            return null; // Retornar null para indicar que el usuario no fue encontrado
        }
    }

    public User getUser(String email) {
        logger.info("getName(" + email + ")");

        if (HMUser.containsKey(email)) {
            User U = HMUser.get(email);
            logger.info("Encontrado(" + email + ")" + U);
            return U;
        }
        logger.warn(email + "not found");
        return null;
    }

    public List<User> getallusers() {
        return new ArrayList<>(this.HMUser.values());
    }

    public Credenciales getCredenciales(User U) {
        logger.info("getCredenciales(" + U + ")");
        Credenciales VOC = new Credenciales(U.getEmail(), U.getPassword());
        return VOC;
    }

    //LISTA OBJETOS, selecciona lista de la database
    public List<Item> Shop() {
        IItemDAO itemDAO = new ItemDAOImpl();
        List<Item> lItemDAO = itemDAO.getItems();
        logger.info("Lista Objetos Correcta");
        return lItemDAO;
    }

    public int LoggedNumber(){return this.logged.size();}


    //LISTA USUARIOS, todos los usuarios
    public List<User> findAll(){
        return users;
    }
 */

    protected List<FAQ> PreguntasFrequentes = new ArrayList<>();
    public List<FAQ> getPreguntasFrequentes() {
        return new ArrayList<>(this.PreguntasFrequentes);
    }
    public void addPreguntasFrequentes(List<FAQ> faqs) {
        this.PreguntasFrequentes.addAll(faqs);
    }
}