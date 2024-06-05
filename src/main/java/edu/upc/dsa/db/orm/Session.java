package edu.upc.dsa.db.orm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Session<E> {
    void save(Object entity);                                           // Crud
    void close();

    Object get(Class theClass, String pk, Object value);
    void update(Object object, int userId) throws SQLException;
    void reupdate(Class theClass, String SET, String valueSET, String WHERE, String valueWHERE, String WHERE2, String valueWHERE2);// crUd
    void delete(Object object);                                         // cruD

    List<Object> findAll(Class theClass);                               // cR
    List<Object> findAll(Class theClass, HashMap<String,String> params) throws SQLException;
    List<Object> getList(Class theClass, String key, Object value);
    List<Object> query(String query, Class theClass, HashMap params);


}