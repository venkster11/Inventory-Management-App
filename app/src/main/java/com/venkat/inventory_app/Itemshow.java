package com.venkat.inventory_app;

import android.content.Intent;

public class Itemshow {
    private String item_name;
    private String user_name;
    private int count;

    public Itemshow(){

    }

    public Itemshow(String item_name, String user_name, int count)
    {
        this.item_name=item_name;
        this.user_name=user_name;
        this.count=count;
    }


    public String getItem_name() {
        return item_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public int getCount() {
        return count;
    }
}
