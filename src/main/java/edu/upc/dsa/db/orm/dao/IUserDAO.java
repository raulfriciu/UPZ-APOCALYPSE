package edu.upc.dsa.db.orm.dao;


import edu.upc.dsa.exception.EmailUsedException;
import edu.upc.dsa.exception.MoneyException;
import edu.upc.dsa.exception.NotInInventoryException;
import edu.upc.dsa.models.Inventory;
import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.User;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public interface IUserDAO {

    int addUser (String name, String email, String password);
    User getUser(int userID);
    User getUserByEmail(String email) throws EmailUsedException;
    void deleteUser(String email);
    void updateUser(String email, String name, String password) throws SQLException;

    List<Item> getItems();
    List<User> getUsers();

}