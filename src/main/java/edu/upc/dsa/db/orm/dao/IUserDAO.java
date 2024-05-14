package edu.upc.dsa.db.orm.dao;


import java.util.List;
import edu.upc.dsa.db.orm.model.*;

public interface IUserDAO {

    public int addUser (String name, String email, String password);
    public User getUser(int employeeID);
    public void updateUser(int employeeID, String name, String email, String password);
    public void deleteUser(int employeeID);
    public List<User> getUser();
}