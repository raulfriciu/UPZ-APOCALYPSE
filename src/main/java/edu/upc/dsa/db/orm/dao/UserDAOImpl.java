package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.db.orm.FactorySession;
import edu.upc.dsa.db.orm.Session;
import edu.upc.dsa.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

import static edu.upc.dsa.db.orm.FactorySession.getConnection;

public class UserDAOImpl implements IUserDAO {
    final static Logger logger = Logger.getLogger(UserDAOImpl.class);

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

/*
    public void updateUser(int employeeID, String name, String email, String password) {
        User user = this.getUser(employeeID);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        Session session = null;
        try {
            session = FactorySession.openSession();
            session.update(user);
        }
        catch (Exception e) {
            // LOG
        }
        finally {
            session.close();
        }
    }
 */

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

/*
    public List<User> getUser() {
        Session session = null;
        List<User> userList =null;
        try {
            session = FactorySession.openSession();
            userList = session.findAll(User.class);
        }
        catch (Exception e) {
            // LOG
        }
        finally {
            session.close();
        }
        return userList;
    }

 */

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


    public List<User> getEmployeeByDept(int deptID) {

        // SELECT e.name, d.name FROM Employees e, DEpt d WHERE e.deptId = d.ID AND e.edat>87 AND ........

//        Connection c =

        Session session = null;
        List<User> userList =null;
        try {
            session = FactorySession.openSession();


            HashMap<String, Integer> params = new HashMap<String, Integer>();
            params.put("deptID", deptID);

            userList = session.findAll(User.class, params);
        }
        catch (Exception e) {
            // LOG
        }
        finally {
            session.close();
        }
        return userList;
    }

    /*

    public void customQuery(xxxx) {
        Session session = null;
        List<Employee> employeeList=null;
        try {
            session = FactorySession.openSession();
            Connection c = session.getConnection();
            c.createStatement("SELECT * ")

        }
*/
    public User buyItem (String item, String user){
        Session session = null;
        User user1 = null;
        Item item1 = null;
        boolean inposession = false;

        try {
            session = FactorySession.openSession();
            user1 = (User)session.get(User.class, "NAME: ", user);
            logger.info(user1.getName());
            item1 = (Item) session.get(Item.class, "ITEM: ", item);
            List<Inventory> list = new ArrayList<>();

            if (user1.getMoney()>= item1.getPrice())
            {
                double balance = user1.getMoney()- item1.getPrice();
                session.update(User.class, "MONEY", String.valueOf(balance),"NAME: ",user);
                list = (List<Inventory>)session.getList(Inventory.class, "NAME: ", user);
                int i=0;
                while (i< list.size())
                {
                    if (list.get(i).getItem().equals(item))
                    {
                        inposession = true;
                        int qty = list.get(i).getQuantity() +1;
                        session.reupdate(Inventory.class, "QUANTITY", String.valueOf(qty),"USER: ",user, "ITEM", item);
                    }
                    i++;
                }
                if (inposession == false)
                {
                    session.save(new Inventory());
                    session.reupdate(Inventory.class, "QUANTITY", String.valueOf(1),"USER: ",user, "ITEM: ", item);
                }

                user1= (User)session.get(User.class,"USER", user);

            }
            else
            {
                logger.info("Not enough money to buy item");

            }
        }
        catch (Exception e) {

        }
        finally {
            session.close();
        }
        return user1;
    }

}
