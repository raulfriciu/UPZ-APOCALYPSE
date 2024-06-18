package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.db.orm.FactorySession;
import edu.upc.dsa.db.orm.Session;
import edu.upc.dsa.db.orm.SessionImpl;
import edu.upc.dsa.exception.MoneyException;
import edu.upc.dsa.models.Inventory;
import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.User;

import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

import org.apache.log4j.Logger;


public class UserDAOImpl implements IUserDAO {
    final static Logger logger = Logger.getLogger(UserDAOImpl.class);

    //AÑADIR USUARIO, crea y guarda user en la database
    public int addUser(String name, String email, String password) {
        Session session = null;
        try {
            session = FactorySession.openSession();
            User user = new User(name, email, password);
            session.save(user);
        } catch (Exception e) {
            // LOG
        } finally {
            session.close();
        }

        return 0;
    }

    //GET USUARIO, selecciona un user por su id de la database
    public User getUser(int userID) {
        Session session = null;
        User user = null;
        try {
            session = FactorySession.openSession();
            user = (User) session.get(User.class, "ID", userID);
        } catch (Exception e) {
            // LOG
        } finally {
            session.close();
        }

        return user;
    }

    //LISTA USUARIOS, selecciona todos los usuarios y los almacena en una lista
    public List<User> getUsers() {

        Session sesion = null;
        List<User> listaUsuarios = null;
        try {
            sesion = FactorySession.openSession();
            listaUsuarios = sesion.findAll(User.class);
        } catch (Exception e) {

        } finally {
            sesion.close();
        }
        return listaUsuarios;
    }

    //GET USER POR SU EMAIL, selecciona un user por su email de la database
    public User getUserByEmail(String email) {
        Session session = null;
        User user = null;
        try {
            session = FactorySession.openSession();
            user = (User) session.get(User.class, "email", email);
        } catch (Exception e) {
            e.printStackTrace();
            // LOG
        } finally {
            session.close();
        }

        return user;
    }

    //ELIMINA USER, elimina un user por su id de la database
    public void deleteUser(String email) {
        User user = this.getUserByEmail(email);
        Session session = null;
        try {
            session = FactorySession.openSession();
            session.delete(user);
        } catch (Exception e) {
            // LOG
        } finally {
            session.close();
        }
    }

    //ACTUALIZA USER, pasado como parametro sus datos
    public void updateUser(String email, String nameNew, String passwordNew) throws SQLException {
        Session session = null;
        try {
            session = FactorySession.openSession();
            User user = (User) session.get(User.class, "email", email);
            if (user != null && (user.getName() != null || user.getPassword() != null)) {
                user.setName(nameNew);
                user.setPassword(passwordNew);
                session.update(user);
                logger.info("Successfully updated");
            } else {
                logger.info("User with email " + email + " not found or no update parameters provided");
                throw new SQLException("User not found");
            }
        } catch (SQLException e) {
            logger.error("Couldn't update user", e);
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    //LISTA OBJETOS, selecciona los objetos de la database
    public List<Item> getItems() {

        Session session = null;
        List<Item> items = null;
        try {
            session = FactorySession.openSession();
            items = session.findAll(User.class);
        } catch (Exception e) {
            // LOG
        } finally {
            session.close();
        }
        return items;
    }

}

