package edu.upc.dsa.models;

public class Credenciales {
    private String name;
    private String password;
    public Credenciales(){}
    public Credenciales(String name, String password) {
        this.setName(name);
        this.setPassword(password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
