package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.db.orm.FactorySession;
import edu.upc.dsa.db.orm.Session;
import edu.upc.dsa.models.Item;
import java.util.List;
import org.apache.log4j.Logger;


public class ItemDAOImpl implements IItemDAO{
    final static Logger logger = Logger.getLogger(ItemDAOImpl.class);
    public Item getItem(String idItem) {
        Session session = null;
        Item item = null;
        try {
            session = FactorySession.openSession();
            item = (Item) session.get(Item.class, "idItem", idItem);
        } finally {
            //session.save(item);
            session.close();
        }

        return item;
    }

    public List<Item> getItems() {
        Session session = null;
        List<Item> items=null;
        try {
            session = FactorySession.openSession();
            items = session.findAll(Item.class);
        }
        catch (Exception e) {
            // LOG
        }
        finally {
            session.close();
        }
        return items;
    }

}

