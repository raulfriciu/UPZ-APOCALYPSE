package edu.upc.dsa.db.orm;

import edu.upc.dsa.db.orm.util.ObjectHelper;
import edu.upc.dsa.db.orm.util.QueryHelper;
import edu.upc.dsa.models.User;

import java.sql.*;
import java.util.*;


public class SessionImpl implements Session {
    private final Connection conn;

    public SessionImpl(Connection conn) {
        this.conn = conn;
    }

    public void save(Object entity) {


        // INSERT INTO Partida () ()
        String insertQuery = QueryHelper.createQueryINSERT(entity);
        // INSERT INTO User (ID, lastName, firstName, address, city) VALUES (0, ?, ?, ?,?)


        PreparedStatement pstm = null;

        try {
            pstm = conn.prepareStatement(insertQuery);
            pstm.setObject(1, 0);
            int i = 2;

            for (String field: ObjectHelper.getFields(entity)) {
                pstm.setObject(i++, ObjectHelper.getter(entity, field));
            }

            pstm.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void close() {
        try {
            this.conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



    public Object get(Class theClass, String pk, Object value) {
        String selectQuery  = QueryHelper.createQuerySELECT(theClass, pk);
        ResultSet rs;
        PreparedStatement pstm = null;
        boolean empty = true;

        try {
            pstm = conn.prepareStatement(selectQuery);
            pstm.setObject(1, value); //son los ?
            rs = pstm.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();

            int numberOfColumns = rsmd.getColumnCount();

            Object o = theClass.newInstance();

            Object valueColumn = null;
            while (rs.next()){
                for (int i=1; i<=numberOfColumns; i++){
                    String columnName = rsmd.getColumnName(i);
                    ObjectHelper.setter(o, columnName, rs.getObject(i));
                    System.out.println(columnName);
                    System.out.println(rs.getObject(i));
                    valueColumn = rs.getObject(i);
                    //if (valueColumn!=null) ObjectHelper.setter(o, columnName, rs.getObject(i));
                }
            }
            return o;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void update(Object object) throws SQLException {
        String updateQuery = QueryHelper.createQueryUPDATE(object);
        System.out.println("Generated UPDATE Query: " + updateQuery);

        PreparedStatement pstm = null;
        String[] fields = ObjectHelper.getFields(object);

        try {
            pstm = conn.prepareStatement(updateQuery);

            // Set parameters for the fields to be updated
            for (int i = 1; i < fields.length; i++) {
                String field = fields[i];
                Object value = ObjectHelper.getter(object, field);
                System.out.println("Setting parameter " + i + ": " + value);
                pstm.setObject(i, value);
            }

            // Set the ID as the last parameter
            Object idValue = ObjectHelper.getter(object, fields[0]);
            System.out.println("Setting ID parameter " + fields.length + ": " + idValue);
            pstm.setObject(fields.length, idValue);

            // Execute the update
            pstm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pstm != null) {
                pstm.close();
            }
        }
    }

    public void delete(Object object) {

    }

    public List<Object> findAll(Class theClass) {
        String query = QueryHelper.createQuerySELECTAll(theClass);
        PreparedStatement pstm =null;
        ResultSet rs;
        List<Object> list = new LinkedList<>();
        try {
            pstm = conn.prepareStatement(query);
            pstm.executeQuery();
            rs = pstm.getResultSet();

            ResultSetMetaData metadata = rs.getMetaData();
            int numberOfColumns = metadata.getColumnCount();

            while (rs.next()){
                Object o = theClass.newInstance();
                for (int j=1; j<=numberOfColumns; j++){
                    String columnName = metadata.getColumnName(j);
                    ObjectHelper.setter(o, columnName, rs.getObject(j));
                }
                list.add(o);

            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Object> getList(Class theClass, String key, Object value) {

        String selectQuery =  QueryHelper.createQuerySELECT(theClass, key);
        ResultSet rs;
        PreparedStatement pstm;
        List<Object> list = new LinkedList<>();

        try {
            pstm = conn.prepareStatement(selectQuery);
            pstm.setObject(1, value);
            rs = pstm.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();

            int numberOfColumns = rsmd.getColumnCount();
            while (rs.next()){
                Object o = theClass.newInstance();
                for (int i=1; i<=numberOfColumns; i++){
                    String columnName = rsmd.getColumnName(i);
                    ObjectHelper.setter(o, columnName, rs.getObject(i));
                }
                list.add(o);
            }
            return list;


        }
        catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Object> findAll(Class theClass, HashMap params) {
        String query = QueryHelper.createQuerySelectWithParams(theClass, params);
        //System.out.println("Consulta SQL generada: " + query);
        PreparedStatement pstm = null;

        try {
            pstm = conn.prepareStatement(query);


            int i = 1;
            for(Object v : params.values()){
                pstm.setObject(i++, v);
            }

            ResultSet rs = pstm.getResultSet();

            ResultSetMetaData metadata = rs.getMetaData();
            int numberOfColumns = metadata.getColumnCount();
            List<Object> l = new LinkedList<>();


            while (rs.next()){
                Object o = theClass.newInstance();
                for (int j=1; j<=numberOfColumns; j++){
                    String columnName = metadata.getColumnName(j);
                    ObjectHelper.setter(o, columnName, rs.getObject(j));
                }
                l.add(o);
            }


            return l;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        return null;
    }

    public List<Object> query(String query, Class theClass, HashMap params) {
        return null;
    }
    public List<Object> findAllByEmail(Object theObject, String email) {
        String selectQuery = QueryHelper.createQuerySELECTAllByEmail(theObject);
        PreparedStatement pstm = null;
        List<Object> ListObject = new ArrayList<Object>();
        try {
            pstm = conn.prepareStatement(selectQuery);
            pstm.setObject(1, email);
            pstm.executeQuery();
            ResultSet rs = pstm.getResultSet();
            while (rs.next()) {
                Class theClass = theObject.getClass();
                Object object = theClass.newInstance();
                for (int i=1;i<=rs.getMetaData().getColumnCount();i++)
                    ObjectHelper.setter(object,rs.getMetaData().getColumnName(i),rs.getObject(i));
                ListObject.add(object);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return ListObject;
    }
    public Object getByID(Object theObject, int id) throws SQLException {
        String selectQuery = QueryHelper.createQuerySELECTobject(theObject);
        PreparedStatement pstm = null;
        try {
            pstm = conn.prepareStatement(selectQuery);
            pstm.setObject(1, id);
            pstm.executeQuery();
            ResultSet rs = pstm.getResultSet();
            if (rs.next()){
                for (int i=1;i<=rs.getMetaData().getColumnCount();i++)
                    ObjectHelper.setter(theObject,rs.getMetaData().getColumnName(i),rs.getObject(i));
            }
            return theObject;

        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
