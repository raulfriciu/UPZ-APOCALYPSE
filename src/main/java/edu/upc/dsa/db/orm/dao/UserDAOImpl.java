package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.db.orm.FactorySession;
import edu.upc.dsa.db.orm.Session;
import edu.upc.dsa.models.*;

import java.util.HashMap;
import java.util.List;

public class UserDAOImpl implements IUserDAO {


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

}
