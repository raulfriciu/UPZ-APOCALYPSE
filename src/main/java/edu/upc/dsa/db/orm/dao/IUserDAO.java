package edu.upc.dsa.db.orm.dao;


import java.util.List;
import edu.upc.dsa.models.*;

public interface IUserDAO {

    public int addUser (String name, String email, String password);
    public User getUser(int userID);
    //public void updateUser(int employeeID, String name, String email, String password);
    public void deleteUser(int employeeID);
    public List <User> getEmployeeByDept(int deptId);

    User getUserByEmail(String email);
}