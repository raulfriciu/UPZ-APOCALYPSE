package edu.upc.dsa.db.orm.dao;


import java.util.List;
import edu.upc.dsa.exception.MoneyException;
import edu.upc.dsa.GameManager;
import edu.upc.dsa.models.*;

public interface IUserDAO {

    public int addUser (String name, String email, String password);
    public User getUser(int userID);
    //public void updateUser(int employeeID, String name, String email, String password);
    public void deleteUser(int employeeID);
    public User buyItem (String item, String user) throws MoneyException;
    public List<Item> getItems();


    User getUserByEmail(String email);


}