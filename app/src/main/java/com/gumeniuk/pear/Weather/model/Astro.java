package com.gumeniuk.pear.Weather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Astro {

    @SerializedName("sunrise")
    @Expose
    protected String sunrise;
    @SerializedName("sunset")
    @Expose
    protected String sunset;
    @SerializedName("moonrise")
    @Expose
    protected String moonrise;
    @SerializedName("moonset")
    @Expose
    private String moonset;

}