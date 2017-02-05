package com.gumeniuk.pear;

public class RecyclerItem {

    private String itemName;

    public RecyclerItem(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public RecyclerItem(){

    }

    @Override
    public String toString() {
        return "RecyclerItem{" +
                "itemName='" + itemName + '\'' +
                '}';
    }
}
