package edu.upc.dsa.db.orm.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class QueryHelper {

    public static String createQueryINSERT(Object entity) {

        StringBuffer sb = new StringBuffer("INSERT INTO ");
        sb.append(entity.getClass().getSimpleName()).append(" ");
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
        // INSERT INTO User (ID, lastName, firstName, address, city) VALUES (0, ?, ?, ?,?)
        return sb.toString();
    }

    public static String createQuerySELECT(Class theClass, String pk) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM ").append(theClass.getSimpleName());
        sb.append(" WHERE "+pk+"= ?");

        return sb.toString();
    }
    public static String createQuerySELECTobject(Object entity) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM ").append(entity.getClass().getSimpleName());
        sb.append(" WHERE ID = ?");

        return sb.toString();
    }
    public static String createQuerySELECTAll(Class theClass) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM ").append(theClass.getSimpleName());
        return sb.toString();
    }

    public static String createQueryUPDATE(Object entity) {
        String[] fields = ObjectHelper.getFields(entity);
        StringBuffer sb = new StringBuffer("UPDATE ");
        sb.append(entity.getClass().getSimpleName().toLowerCase());
        sb.append(" SET ");

        for (int i = 1; i < fields.length; i++) {
            if (i > 1) {
                sb.append(", ");
            }
            sb.append(fields[i]).append(" = ?");
        }

        sb.append(" WHERE ").append(fields[0]).append(" = ?");

        return sb.toString();
    }

    public static String createQuerySelectWithParams(Class theClass, HashMap params) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM ").append(theClass.getSimpleName().toLowerCase());
        sb.append(" WHERE (");

        params.forEach((k,v) ->{
            //k = k.substring(0, 1).toUpperCase() + k.substring(1);
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
        sb.append("SELECT * FROM ").append(entity.getClass().getSimpleName());
        sb.append(" WHERE emailUser = ?");
        return sb.toString();
    }
}