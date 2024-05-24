package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.db.orm.FactorySession;
import edu.upc.dsa.db.orm.Session;
import edu.upc.dsa.exception.MoneyException;
import edu.upc.dsa.models.Inventory;
import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

import static edu.upc.dsa.db.orm.FactorySession.getConnection;

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
    public User getUser(int userID) {
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
    public void deleteUser(int employeeID) {
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
    public User buyItem (String item, String user) throws MoneyException{

        Session session = null;
        User user1 = null;
        Item item1 = null;
        boolean inposession = false;

        try {
            session = FactorySession.openSession();
            user1 = (User)session.get(User.class, "NAME: ", user);
            logger.info(user1.getName());
            item1 = (Item) session.get(Item.class, "ITEM: ", item); // Retrieves the item by its name
            List<Inventory> list = new ArrayList<>();

            if (user1.getMoney()>= item1.getPrice())
            {
                double balance = user1.getMoney()- item1.getPrice();  // Checks if the user has enough money
                session.update(User.class, "MONEY", String.valueOf(balance),"NAME: ",user);  // Updates the user's balance
                list = (List<Inventory>)session.getList(Inventory.class, "NAME: ", user);   // Retrieves the user's inventory
                int i=0;
                while (i< list.size())
                {
                    if (list.get(i).getItem().equals(item))
                    {
                        inposession = true;
                        int qty = list.get(i).getQuantity() +1;
                        session.reupdate(Inventory.class, "QUANTITY", String.valueOf(qty),"USER: ",user, "ITEM", item);   // Updates the quantity if item is already in inventory
                    }
                    i++;
                }
                if (inposession == false)
                {
                    session.save(new Inventory());   // Adds the new item to the inventory if not already present
                    session.reupdate(Inventory.class, "QUANTITY", String.valueOf(1),"USER: ",user, "ITEM: ", item);
                }

                user1= (User)session.get(User.class,"USER", user); // Updates user details after purchase


            }

        }
        catch (Exception e) {
            logger.info("Not enough money to buy item");
            throw new MoneyException();
        }
        finally {
            session.close();
        }
        return user1;
    }

}
