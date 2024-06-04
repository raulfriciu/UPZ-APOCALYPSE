package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.db.orm.FactorySession;
import edu.upc.dsa.db.orm.Session;
import edu.upc.dsa.exception.NonExistentItemException;
import edu.upc.dsa.exception.NotInInventoryException;
import edu.upc.dsa.models.Inventory;
import edu.upc.dsa.models.Item;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class InventoryDAOImpl implements IInventoryDAO {
    final static Logger logger = Logger.getLogger(String.valueOf(ItemDAOImpl.class));
    private static InventoryDAOImpl instance;
    public static InventoryDAOImpl getInstance() {
        if (instance==null) instance = new InventoryDAOImpl();
        return instance;
    }
    public List<Inventory> getInventory (String idUser) throws NonExistentItemException, SQLException, NotInInventoryException {
        Session session = null;
        IItemDAO itemDAO = new ItemDAOImpl();
        HashMap<String, String> user = new HashMap<>();
        user.put("idUser", idUser);
        List<Inventory> userItems= new ArrayList<>();
        try {
            session = FactorySession.openSession();
            List<Object> inInventory = session.findAll(Inventory.class, user);
            if (inInventory.size()!=0) {
                logger.info("The user with id: "+idUser+" has items in his inventory");
                for (Object object : inInventory) {
                    Inventory inventory = (Inventory) object;
                    try {
                        userItems.add(inventory);
                    } catch (Exception e) {
                        throw new NonExistentItemException();
                    }
                }
                return userItems;
            }
            throw new NotInInventoryException();
        }catch (Exception e){
        } finally {
            session.close();
        }
        return userItems;
    }

    public List<Inventory> getInventoryitems() {
        Session session = null;
        List<Inventory> inventoryitems=null;
        try {
            session = FactorySession.openSession();
            inventoryitems = session.findAll(Inventory.class);
        }
        catch (Exception e) {
            // LOG
        }
        finally {
            session.close();
        }
        return inventoryitems;
    }

}
