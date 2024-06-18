package edu.upc.dsa.db.orm.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class QueryHelper {

    public static String createQueryINSERT(Object entity) {

        StringBuffer sb = new StringBuffer("INSERT INTO ");
        sb.append(entity.getClass().getSimpleName().toLowerCase()).append(" ");
        sb.append("(");

        String [] fields = ObjectHelper.getFields(entity);

        sb.append("ID");
        for (String field: fields) {
            if (!field.equals("ID")) sb.append(", ").append(field);
        }
        sb.append(") VALUES (?");

        for (String field: fields) {
            if (!field.equals("ID"))  sb.append(", ?");
        }
        sb.append(")");
        return sb.toString();
    }

    public static String createQuerySELECT(Class theClass, String pk) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM ").append(theClass.getSimpleName().toLowerCase());
        sb.append(" WHERE "+pk+"= ?");

        return sb.toString();
    }
    public static String createQuerySELECTinventory(Class theClass, String[] pks) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(theClass.getSimpleName().toLowerCase());
        sb.append(" WHERE ");
        for (int i = 0; i < pks.length; i++) {
            sb.append(pks[i]).append(" = ?");
            if (i < pks.length - 1) {
                sb.append(" AND ");
            }
        }
        return sb.toString();
    }

    public static String createQuerySELECTobject(Object entity) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM ").append(entity.getClass().getSimpleName().toLowerCase());
        sb.append(" WHERE ID = ?");

        return sb.toString();
    }
    public static String createQuerySELECTAll(Class theClass) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM ").append(theClass.getSimpleName().toLowerCase());
        return sb.toString();
    }

    public static String createQueryUPDATE(Object object) {
        String[] fields = ObjectHelper.getFields(object);
        String tableName = object.getClass().getSimpleName().toLowerCase();
        StringBuilder sb = new StringBuilder("UPDATE ").append(tableName).append(" SET ");

        sb.append("name = ?, ");
        sb.append("password = ? ");
        sb.append("WHERE email = ?");

        return sb.toString();
    }

    public static String createQuerySelectWithParams(Class theClass, HashMap params) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM ").append(theClass.getSimpleName().toLowerCase());
        sb.append(" WHERE (");

        params.forEach((k,v) ->{
            if(k.equals("password")){
                sb.append(k).append(" = MD5(").append("?").append(") AND ");
            }else {
                sb.append(k).append(" = ").append("?").append(" AND ");
            }
        });
        sb.delete(sb.length()-4, sb.length()-1);
        sb.append(")");

        return sb.toString();
    }

    public static String createQuerySELECTAllByEmail(Object entity) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM ").append(entity.getClass().getSimpleName().toLowerCase());
        sb.append(" WHERE emailUser = ?");
        return sb.toString();
    }
}