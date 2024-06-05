package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.models.Item;

import java.util.List;
public interface IItemDAO {

    List<Item> getItems();
    Item getItem(int idItem) ;

}

