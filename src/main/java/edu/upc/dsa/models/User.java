package edu.upc.dsa.models;

import edu.upc.dsa.exception.MoneyException;
import edu.upc.dsa.util.RandomUtils;

public class User {

    String name;
    String email;
    String password;
    int money;

    public static int getLastId() {
        return lastId;
    }
    static int lastId;

    public User() {};

    public User(String name, String email, String password) {
        this();
        this.name=name;
        this.email=email;
        this.password=password;
        this.money=500;
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

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
    public void compraItem(Item item) throws MoneyException {
        if(item.getPrice()>this.money){
            throw new MoneyException();
        }
        this.money = this.money - item.getPrice();
    }
    public void update(String name, String surname, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }
    @Override
    public String toString() {
        return "User [name=" + name + ", email=" + email +", password=" + password +"]";
    }

}