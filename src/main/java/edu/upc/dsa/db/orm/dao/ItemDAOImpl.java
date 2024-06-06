package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.db.orm.FactorySession;
import edu.upc.dsa.db.orm.Session;
import edu.upc.dsa.models.Inventory;
import edu.upc.dsa.models.Item;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.upc.dsa.models.User;
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
    public List<Item> getInventory(String email) throws SQLException {
        Session session = null;
        List<Item> items = new ArrayList<Item>();
        try {
            session = FactorySession.openSession();
            List<Inventory> compras = session.findAllByEmail(new Inventory(), email);

            for (Inventory inventario : compras)
            {
                Item objeto = new Item();
                int idItem = inventario.getIdItem();
                objeto = (Item) session.getByID(objeto, idItem);
                items.add(objeto);

            }

        } catch (Exception e) {
            e.printStackTrace();
            items = null;
        } finally {
            session.close();
            return items;
        }
    }
    public int getPriceByID (int idItem)throws SQLException{
        Session session = null;
        Item item = new Item();
        try {
            session = FactorySession.openSession();
            item = (Item) session.getByID(item,idItem);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            session.close();
        }

        return item.getPrice();
    }
    public int buyItemForUser(User user, int idItem) throws SQLException {
        Session session = null;
        int error = -1;
        try {
            int priceItem = getPriceByID(idItem);
            int userMoney = user.getMoney();
            int finalMoney = userMoney - priceItem;

            if (finalMoney >= 0) {
                session = FactorySession.openSession();

                // Actualizamos el dinero del usuario
                user.setMoney(finalMoney);
                session.update(user);

                // Guardamos el nuevo objeto en el inventario
                Inventory inventario = new Inventory(idItem, user.getEmail());
                session.save(inventario);

                error = 0;
            } else {
                error = 6;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return error;
    }


}


