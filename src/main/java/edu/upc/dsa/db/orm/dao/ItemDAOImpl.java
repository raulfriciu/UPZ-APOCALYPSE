package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.db.orm.FactorySession;
import edu.upc.dsa.db.orm.Session;
import edu.upc.dsa.models.Item;
import java.util.List;
import org.apache.log4j.Logger;


public class ItemDAOImpl implements IItemDAO {
    final static Logger logger = Logger.getLogger(ItemDAOImpl.class);

    @Override
    public Item getItem(int idItem) {
        Session session = null;
        Item item = null;
        try {
            session = FactorySession.openSession();
            item = (Item) session.get(Item.class, "ID", idItem);
        } catch (Exception e) {
            logger.error("Error getting item", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return item;
    }

    @Override
    public List<Item> getItems() {
        Session session = null;
        List<Item> items = null;
        try {
            session = FactorySession.openSession();
            items = session.findAll(Item.class);
        } catch (Exception e) {
            logger.error("Error getting items", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return items;
    }
}


