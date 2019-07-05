package com.venkat.inventory_app.Model;

public class Logs_Model {

    private int countitem;
    private String item_name;
    private String status;
    private String username;

    public Logs_Model(){}

    public Logs_Model(int countitem, String item_name, String status, String username){
        this.countitem = countitem;
        this.item_name = item_name;
        this.status = status;
        this.username = username;
    }

    public int getCountitem() {
        return countitem;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }
}
