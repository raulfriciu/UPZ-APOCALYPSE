package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.models.Item;
<<<<<<< HEAD
=======
import edu.upc.dsa.models.User;
>>>>>>> c090a840db8f76b5898d03c380f3a787fe72808a

import java.util.List;
public interface IItemDAO {

    public List<Item> getItems();
    Item getItemByName(String name);

}

