package edu.upc.dsa.models;

public class Inventory {
    public int idItem;
    public String emailUser;
    public Inventory() {}
    public Inventory( int idItem, String emailUser)
    {
        this.idItem = idItem;
        this.emailUser = emailUser;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }
}
