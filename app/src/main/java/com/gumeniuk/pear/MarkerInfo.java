package com.gumeniuk.pear;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Ваня on 28.02.2017.
 */

public class MarkerInfo extends RealmObject {
    @PrimaryKey
    String id;
    double lat;
    double lng;
    String title;
    String description;
    String markerUserName;

    public MarkerInfo() {
    }

    public MarkerInfo(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMarkerUserName(String markerUserName) {
        this.markerUserName = markerUserName;
    }

    public String getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getMarkerUserName() {
        return markerUserName;
    }
}
