package com.gumeniuk.pear.Database;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Ваня on 08.02.2017.
 */

public class User extends RealmObject{

    @PrimaryKey
    private String id;
    @Required
    private String name;

    private RealmList<RecyclerItem> items;

    public User(){}

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItems(RealmList<RecyclerItem> items) {
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public RealmList<RecyclerItem> getItems() {
        return items;
    }
}
