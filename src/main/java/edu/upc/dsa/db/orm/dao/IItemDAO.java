package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.User;

import java.util.List;
public interface IItemDAO {

    public List<Item> getItems();
    Item getItemByName(String name);

}

