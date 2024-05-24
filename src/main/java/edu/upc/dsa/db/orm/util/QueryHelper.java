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

        String [] fields = edu.upc.dsa.db.orm.util.ObjectHelper.getFields(entity);

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
    public static String createQuerySELECTAll(Class theClass) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM ").append(theClass.getSimpleName());
        return sb.toString();
    }


    public static String createSelectFindAll(Class theClass, HashMap<String, String> params) {

        Set<Map.Entry<String, String>> set = params.entrySet();

        StringBuffer sb = new StringBuffer("SELECT * FROM "+theClass.getSimpleName()+" WHERE 1=1");
        for (String key: params.keySet()) {
            sb.append(" AND "+key+"=?");
        }


        return sb.toString();
    }

    public static String createQueryUPDATE(Class clase, String SET, String Where) {
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE ").append(clase.getSimpleName());
        if (Objects.equals(SET, "PASSWORD")){
            sb.append(" SET ").append(SET);
            sb.append(" = MD5(?) ");
            sb.append(" WHERE ");
            sb.append(Where);
            sb.append(" = ?");
        }
        else{
            sb.append(" SET ").append(SET);
            sb.append(" = ? ");
            sb.append(" WHERE ");
            sb.append(Where);
            sb.append(" = ?");
        }
        return sb.toString();
    }

    public static String createQueryREUPDATE(Class clase, String SET, String Where, String Where2) {
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE ").append(clase.getSimpleName());
        if (Objects.equals(SET, "PASSWORD")){
            sb.append(" SET ").append(SET);
            sb.append(" = MD5(?) ");
            sb.append(" WHERE ");
            sb.append(Where);
            sb.append(" = ?");
        }
        else{
            sb.append(" SET ").append(SET);
            sb.append(" = ? ");
            sb.append(" WHERE ");
            sb.append(Where);
            sb.append(" = ? ");
            sb.append(" AND ");
            sb.append(Where2);
            sb.append(" = ?");
        }
        return sb.toString();
    }

    public static String createQuerySelectWithParams(Class theClass, HashMap params) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM ").append(theClass.getSimpleName());
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
}