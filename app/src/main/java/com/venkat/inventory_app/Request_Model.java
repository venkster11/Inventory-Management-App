package com.venkat.inventory_app;

public class Request_Model {

    private String docu_id;
    private String nameitem;
    private String username;
    private String uid;
    private int countavail;
    private int reqcount;

    public Request_Model(){

    }

    public Request_Model(String docu_id ,String nameitem, String username, String uid, int countavail, int reqcount ){
        this.docu_id=docu_id;
        this.nameitem = nameitem;
        this.username = username;
        this.uid = uid;
        this.countavail = countavail;
        this.reqcount= reqcount;
    }


    public String getDocu_id() {
        return docu_id;
    }

    public String getNameitem() {
        return nameitem;
    }

    public String getUsername() {
        return username;
    }

    public String getUid() {
        return uid;
    }

    public int getCountavail() {
        return countavail;
    }

    public int getReqcount() {
        return reqcount;
    }
}
