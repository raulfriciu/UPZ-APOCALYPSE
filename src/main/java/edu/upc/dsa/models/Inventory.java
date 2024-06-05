package edu.upc.dsa.models;

public class Inventory {
    public int item;
    public int user;
    public Inventory() {}
    public Inventory(int item, int user)
    {
        this.item = item;
        this.user = user;
    }


    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
}
