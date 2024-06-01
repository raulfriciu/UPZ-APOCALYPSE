package edu.upc.dsa.db.orm.dao;


import edu.upc.dsa.exception.MoneyException;
import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.User;

import java.util.List;
<<<<<<< HEAD
=======
import edu.upc.dsa.exception.MoneyException;
import edu.upc.dsa.GameManager;
import edu.upc.dsa.models.*;
>>>>>>> c090a840db8f76b5898d03c380f3a787fe72808a

public interface IUserDAO {

    public int addUser (String name, String email, String password);
    public User getUser(int userID);
    //public void updateUser(int employeeID, String name, String email, String password);
    public void deleteUser(int employeeID);
    public User buyItem (String item, String user) throws MoneyException;
    public List<Item> getItems();


    User getUserByEmail(String email);


}