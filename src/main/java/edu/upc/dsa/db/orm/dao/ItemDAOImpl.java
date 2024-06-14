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

    //GET ITEM, selecciona un objeto por su id de la database
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

    //LISTA ITEMS, devuelve una lista de todos los objetos
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

    //LISTA ITEMS INVENTARIO, devuelve una lista de todos los objetos que tiene el usuario, con ese email
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

    //GET PRICE POR ID, devuelve el precio del objeto, con ese id
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

    //COMPRAR OBJETO, compra el objeto con ese id y lo añade al inventario de user, restando el precio del objeto
    public int buyItemForUser(User user, int idItem) throws SQLException {
        Session session = null;
        int error = -1;
        try {
            int priceItem = getPriceByID(idItem);
            int userMoney = user.getMoney();
            int finalMoney = userMoney - priceItem;

            session = FactorySession.openSession();

            String[] keys = {"idItem", "emailUser"};
            Object[] values = {idItem, user.getEmail()};
            Inventory existingItem = (Inventory) session.getInventory(Inventory.class, keys, values);
            if (existingItem != null) {
                error = 9;
            } else {
                if (finalMoney >= 0) {
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

    //DEVUELVE OBJETO, elimina el objeto con ese id del inventario de user, devolviendo el precio del objeto
    public int cancelItemForUser(User user, int idItem) throws SQLException {
        Session session = null;
        int error = -1;
        try {
            session = FactorySession.openSession();

            String[] keys = {"idItem", "emailUser"};
            Object[] values = {idItem, user.getEmail()};
            Inventory existingItem = (Inventory) session.getInventory(Inventory.class, keys, values);

            if (existingItem != null) {
                int priceItem = getPriceByID(idItem);

                int userMoney = user.getMoney();
                int finalMoney = userMoney + priceItem;
                user.setMoney(finalMoney);
                session.update(user);

                session.delete(existingItem);

                error = 0;
            } else {
                error = 8;
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


