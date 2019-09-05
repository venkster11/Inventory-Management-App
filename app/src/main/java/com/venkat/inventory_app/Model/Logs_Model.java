package com.venkat.inventory_app.Model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Logs_Model {

    private int countitem;
    private String item_name;
    private String status;
    private String username;
    private @ServerTimestamp
    Date timestamp;

    public Logs_Model(){}

    public Logs_Model(int countitem, String item_name, String status, String username,Date timestamp){
        this.countitem = countitem;
        this.item_name = item_name;
        this.status = status;
        this.username = username;
        this.timestamp=timestamp;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public String getUsername() {
        return username;
    }
}
