package com.gumeniuk.pear.Weather.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Condition_ {

    @SerializedName("text")
    @Expose
    protected String text;
    @SerializedName("icon")
    @Expose
    protected String icon;
    @SerializedName("code")
    @Expose
    protected Integer code;

}
