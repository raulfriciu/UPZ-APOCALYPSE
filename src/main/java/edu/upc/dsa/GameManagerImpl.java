package edu.upc.dsa;

import edu.upc.dsa.db.orm.dao.IUserDAO;
import edu.upc.dsa.db.orm.dao.UserDAOImpl;
import edu.upc.dsa.exception.EmailUsedException;
import edu.upc.dsa.exception.IncorrectPasswordException;
import edu.upc.dsa.exception.UserNotRegisteredException;
import edu.upc.dsa.models.Credenciales;
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
    protected List<User> logged;
    public List<User> getUsers(){
        return users;
    }
    private  GameManagerImpl() {
        this.users = new LinkedList<>();
        this.logged = new LinkedList<>();
        HMUser = new HashMap<>();
        HMUser = new HashMap<String, User>();
    }
    public static GameManager getInstance() {
        if (instance==null) instance = new GameManagerImpl();
        return instance;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public int size() {
        int ret = this.users.size();
        logger.info("size " + ret);

        return ret;
    }

    public int addUser(String name, String mail, String password) {
        User u = new User(name, mail, password);
        if (HMUser.containsKey(mail)) {
            logger.info("Mail ya en uso");
            return 1;
        }
        for (Map.Entry<String, User> e : HMUser.entrySet()) {
            if (e.getValue().getName() == name)
                return 2;
        }
        logger.info("new user " + u.getName());
        this.HMUser.put(u.getEmail(), u);
        logger.info("new user added");
        return 0;
    }

    //REGISTRAR USUARIO, datos del html, excepcion email
    @Override
    public User registrarUser(User user) throws EmailUsedException {
        String email = user.getEmail().trim().toLowerCase();  // Normalizar el email
        User u = HMUser.get(email);

        if (u == null)
        {
            String name= user.getName();

            String password= user.getPassword();
            IUserDAO userDAO = new UserDAOImpl();
            userDAO.addUser(name, email,password );
            HMUser.put(email, user);  // Añadir usuario al HashMap
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
        logger.info("login(" + credenciales + ")");
        // Validar el formato del correo electrónico
        if (!isValidEmail(credenciales.getEmail())) {
            logger.info("Formato de correo electrónico no válido");
            return null;
        }
        if (HMUser.containsKey(credenciales.getEmail())) {
            User usuario = HMUser.get(credenciales.getEmail());

            // Verificar la contraseña
            if (usuario.getPassword().equals(credenciales.getPassword())) {
                logger.info("Login successful for user: " + usuario.getName());
                return usuario;
            } else {
                logger.warn("Incorrect password for user: " + usuario.getName());
            }
        } else {
            logger.warn("User with email " + credenciales.getEmail() + " not found");
        }
        return null;
    }

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

    public int UserNumber() {
        return this.users.size();
    }

    public int LoggedNumber(){return this.logged.size();}

/*
    //LISTA USUARIOS, todos los usuarios
    public List<User> findAll(){
        return users;
    }
 */
}