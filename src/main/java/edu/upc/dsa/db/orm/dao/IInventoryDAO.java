package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.exception.NonExistentItemException;
import edu.upc.dsa.exception.NotInInventoryException;
import edu.upc.dsa.models.Inventory;

import java.sql.SQLException;
import java.util.List;

public interface IInventoryDAO {
    public List<Inventory> getInventoryitems();
    public List<Inventory> getInventory(int idUser) throws SQLException, NonExistentItemException, NotInInventoryException;

}
