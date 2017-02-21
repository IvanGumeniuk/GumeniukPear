package com.gumeniuk.pear.Weather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecastday {

    @SerializedName("date")
    @Expose
    protected String date;
    @SerializedName("date_epoch")
    @Expose
    protected Integer dateEpoch;
    @SerializedName("day")
    @Expose
    protected Day day;
    @SerializedName("astro")
    @Expose
    protected Astro astro;
    @SerializedName("hour")
    @Expose
    protected List<Hour> hour = null;

}