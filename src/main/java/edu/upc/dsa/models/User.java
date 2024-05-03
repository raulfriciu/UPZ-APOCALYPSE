package edu.upc.dsa.models;

import edu.upc.dsa.util.RandomUtils;

public class User {

    String idUser;
    String name;
    String email;
    String password;
    static int lastId;

    public User() {
        this.idUser = RandomUtils.getId();
    }

    public User(String name, String email, String password) {
        this();
        this.name=name;
        this.email=email;
        this.password=password;

    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User [idUser="+ idUser +", name=" + name + ", email=" + email +", password=" + password +"]";
    }

}