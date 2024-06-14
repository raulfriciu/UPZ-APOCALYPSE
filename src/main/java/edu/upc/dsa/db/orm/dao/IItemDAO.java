package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.User;

import java.sql.SQLException;
import java.util.List;
public interface IItemDAO {

    List<Item> getItems();
    Item getItem(int idItem) ;
    List<Item> getInventory(String email) throws SQLException;

    int buyItemForUser(User user, int idItem ) throws SQLException;
    int cancelItemForUser(User user, int idItem) throws SQLException;
}

