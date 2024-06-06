package edu.upc.dsa.db.orm.dao;


import edu.upc.dsa.exception.MoneyException;
import edu.upc.dsa.exception.NotInInventoryException;
import edu.upc.dsa.models.Inventory;
import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {

    int addUser (String name, String email, String password);
    User getUser(int userID);
    User getUserByEmail(String email);
    void deleteUser(int employeeID);

    List<Item> getItems();

}