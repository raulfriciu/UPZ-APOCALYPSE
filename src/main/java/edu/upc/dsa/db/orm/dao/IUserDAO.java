package edu.upc.dsa.db.orm.dao;


import edu.upc.dsa.exception.MoneyException;
import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {

    int addUser (String name, String email, String password);
    User getUser(String userID);
    //public void updateUser(int employeeID, String name, String email, String password);
    void deleteUser(String employeeID);
    void buyItem(String idItem, String idUser) throws MoneyException,  SQLException;

    List<Item> getItems();


    User getUserByEmail(String email);


}