package edu.upc.dsa.db.orm.dao;


import java.util.List;
import edu.upc.dsa.exception.MoneyException;
import edu.upc.dsa.GameManager;
import edu.upc.dsa.models.*;

public interface IUserDAO {

    int addUser (String name, String email, String password);

    User getUser(int userID);
    List<Item> getItems();
    User buyItem (String item, String user) throws MoneyException;

    User getUserByEmail(String email);

    //public void updateUser(int employeeID, String name, String email, String password);

    void deleteUser(int employeeID);

}