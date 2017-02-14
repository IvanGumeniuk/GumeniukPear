package com.gumeniuk.pear.Database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RecyclerItem extends RealmObject {

    @PrimaryKey
    private String id;
    private String itemName;
    private String itemUserName;
    private String itemPhoneNumber;

    public RecyclerItem(){}

    public RecyclerItem(String id, String itemName,String itemPhoneNumber , String itemUserName) {
        this.id = id;
        this.itemName = itemName;
        this.itemPhoneNumber = itemPhoneNumber;
        this.itemUserName = itemUserName;
    }

    public void setItemPhoneNumber(String itemPhoneNumber) {
        this.itemPhoneNumber = itemPhoneNumber;
    }

    public String getItemPhoneNumber() {

        return itemPhoneNumber;
    }

    public String getItemUserName() {

        return itemUserName;
    }

    public void setItemUserName(String itemUserName) {
        this.itemUserName = itemUserName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
