package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.db.orm.FactorySession;
import edu.upc.dsa.db.orm.Session;
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
        int userID = 0;
        try {
            session = FactorySession.openSession();
            User user = new User(name, email, password);
            session.save(user);
        }
        catch (Exception e) {
            // LOG
        }
        finally {
            session.close();
        }

        return userID;
    }

    //GET USUARIO, selecciona un user por su id de la database
    public User getUser(String userID) {
        Session session = null;
        User user = null;
        try {
            session = FactorySession.openSession();
            user = (User)session.get(User.class, "id", userID);
        }
        catch (Exception e) {
            // LOG
        }
        finally {
            session.close();
        }

        return user;
    }

    //GET USER POR SU EMAIL, selecciona un user por su email de la database
    public User getUserByEmail(String email) {
        Session session = null;
        User user = null;
        try {
            session = FactorySession.openSession();
            user = (User)session.get(User.class, "email", email);
        }
        catch (Exception e) {
            e.printStackTrace();
            // LOG
        }
        finally {
            session.close();
        }

        return user;
    }

    //ELIMINA USER, elimina un user por su id de la database
    public void deleteUser(String employeeID) {
        User user = this.getUser(employeeID);
        Session session = null;
        try {
            session = FactorySession.openSession();
            session.delete(user);
        }
        catch (Exception e) {
            // LOG
        }
        finally {
            session.close();
        }

    }

    //LISTA OBJETOS, selecciona los objetos de la database
    public List<Item> getItems() {

        Session session = null;
        List<Item> items=null;
        try {
            session = FactorySession.openSession();
            items = session.findAll(User.class);
        }
        catch (Exception e) {
            // LOG
        }
        finally {
            session.close();
        }
        return items;
    }



    //COMPRA OBJETO, selecciona el item por el nombre, excepcion dinero insuficiente
    public void buyItem(String idItem, String idUser) throws MoneyException,  SQLException {

        logger.info("Buying item "+ idItem + " for User " +idUser);
        Session session = null;
        IItemDAO itemDAO = new ItemDAOImpl();
        Item item = itemDAO.getItem(idItem);
        User user = getUser(idUser);
        logger.info(item.getPrice());
        try {
            session = FactorySession.openSession();
            user.compraItem(item);
            logger.info("Objeto comprado");
            session.update(user);
            Inventory inventory = new Inventory(idItem, idUser);
            session.save(inventory);

        } catch (MoneyException e) {
            logger.warn("No tienes suficiente dinero");
            throw new MoneyException();
        } catch(SQLException e){
            logger.warn("Objeto ya en el inventario");
            throw new SQLException();
        }
        finally {

            session.close();
        }
    }

}
