package com.venkat.inventory_app;

public class Borrowed_Model {

    private String docuID;
    private String item_name;
    private int mycount;

    public Borrowed_Model(){}

    public Borrowed_Model(String docuID, String item_name, int mycount){
        this.docuID = docuID;
        this.item_name = item_name;
        this.mycount = mycount;
    }

    public String getDocuID() {
        return docuID;
    }

    public String getItem_name() {
        return item_name;
    }

    public int getMycount() {
        return mycount;
    }
}
