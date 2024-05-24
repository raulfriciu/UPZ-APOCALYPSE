package edu.upc.dsa.db.orm;

import java.util.HashMap;
import java.util.List;

public interface Session<E> {
    void save(Object entity);                                           // Crud
    void close();

    public Object get(Class theClass, String pk, Object value);
    void update(Class theClass, String SET, String valueSET, String WHERE, String valueWHERE);
    void reupdate(Class theClass, String SET, String valueSET, String WHERE, String valueWHERE, String WHERE2, String valueWHERE2);// crUd

    void delete(Object object);                                         // cruD
    List<Object> findAll(Class theClass);                               // cR
    List<Object> findAll(Class theClass, HashMap params);
    List<Object> getList(Class theClass, String key, Object value);
    List<Object> query(String query, Class theClass, HashMap params);
}