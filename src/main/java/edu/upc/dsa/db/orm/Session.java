package edu.upc.dsa.db.orm;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface Session<E> {
    void save(Object entity);                                           // Crud
    void close();

    Object get(Class theClass, String pk, Object value);
    Object getInventory(Class theClass, String[] pks, Object[] values);
    Object getByID(Object theObject, int id) throws SQLException;
    void update(Object object) throws SQLException;
    void delete(Object object) throws SQLException;

    List<Object> findAll(Class theClass);                               // cR
    List<Object> findAllByEmail(Object theObject, String email);
    List<Object> findAll(Class theClass, HashMap<String,String> params) throws SQLException;
    List<Object> getList(Class theClass, String key, Object value);
    List<Object> query(String query, Class theClass, HashMap params);
}