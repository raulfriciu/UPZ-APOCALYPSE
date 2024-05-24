package edu.upc.dsa.models;

public class Inventory {
    public String item;
    public String user;
    public int quantity;
    public Inventory() {}
    public Inventory(String item, String user)
    {
        this.item = item;
        this.user = user;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
