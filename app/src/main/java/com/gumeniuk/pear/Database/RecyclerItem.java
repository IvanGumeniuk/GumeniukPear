package com.gumeniuk.pear.Database;

import android.support.annotation.NonNull;

import java.util.Comparator;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RecyclerItem extends RealmObject implements Comparator<RecyclerItem> {

    @PrimaryKey
    private String id;
    private String itemName;
    private String itemUserName;
    private String itemPhoneNumber;
    private String enteringWay;

    public RecyclerItem(){}

    public RecyclerItem(String id, String itemName,String itemPhoneNumber , String itemUserName, String enteringWay) {
        this.id = id;
        this.itemName = itemName;
        this.itemPhoneNumber = itemPhoneNumber;
        this.itemUserName = itemUserName;
        this.enteringWay = enteringWay;
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

    public String getEnteringWay() {
        return enteringWay;
    }

    public void setEnteringWay(String enteringWay) {
        this.enteringWay = enteringWay;
    }


    @Override
    public int compare(RecyclerItem recyclerItem, RecyclerItem t1) {
        return recyclerItem.getItemName().compareTo(t1.getItemName());
    }
}
