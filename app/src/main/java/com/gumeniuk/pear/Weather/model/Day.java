package com.gumeniuk.pear.Weather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Day {

    @SerializedName("maxtemp_c")
    @Expose
    protected Double maxtempC;
    @SerializedName("maxtemp_f")
    @Expose
    protected Double maxtempF;
    @SerializedName("mintemp_c")
    @Expose
    protected Double mintempC;
    @SerializedName("mintemp_f")
    @Expose
    protected Double mintempF;
    @SerializedName("avgtemp_c")
    @Expose
    protected Double avgtempC;
    @SerializedName("avgtemp_f")
    @Expose
    protected Double avgtempF;
    @SerializedName("maxwind_mph")
    @Expose
    protected Double maxwindMph;
    @SerializedName("maxwind_kph")
    @Expose
    protected Double maxwindKph;
    @SerializedName("totalprecip_mm")
    @Expose
    protected Double totalprecipMm;
    @SerializedName("totalprecip_in")
    @Expose
    protected Double totalprecipIn;
    @SerializedName("condition")
    @Expose
    protected Condition_ condition;

}
