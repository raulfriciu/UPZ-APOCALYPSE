package edu.upc.dsa.models;

public class Item {
    String name;
    String description;
    int price;
    String image;


    public Item() {};

    public Item(String name, String description, int price, String image){
        this.name = name;
        this.description = description;
        this.price = price;
        this.image=image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
