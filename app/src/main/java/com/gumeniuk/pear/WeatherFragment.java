package com.gumeniuk.pear;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gumeniuk.pear.Weather.model.Forecastday;
import com.gumeniuk.pear.Weather.model.Weather;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WeatherFragment extends Fragment {

    private View view;
    MyApplicationClass app;
    TextView textWeather;
    List<Forecastday> posts;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.weather_fragment, container, false);

        app = ((MyApplicationClass)getActivity().getApplicationContext());
        textWeather = (TextView)view.findViewById(R.id.textWeather);

        posts = new ArrayList<>();

        app.getForecastWeather().getData("8846dd25320e47fd935192031172002","Lviv").enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                posts.addAll(response.body().getForecast().getForecastday());

            }
            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        Toast.makeText(getActivity(), String.valueOf(app.getLatitude())
                +","+String.valueOf(app.getLongitude()) , Toast.LENGTH_SHORT).show();

        app.getCurrentWeather().getData("8846dd25320e47fd935192031172002", "Lviv").enqueue(new Callback<Weather>() {
       // app.getCurrentWeather().getData("8846dd25320e47fd935192031172002", "40.730610, -73.935242").enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                try {
                    String city = response.body().getLocation().getName();
                    String country = response.body().getLocation().getCountry();
                    String status = response.body().getCurrent().getCondition().getText();
                    String tmpC = response.body().getCurrent().getTempC().toString();
                    String qw = response.body().getCurrent().getFeelslikeC().toString();


                    textWeather.setText(city+" "+country+" "+status+" "+tmpC+" "+qw+"\n"+"http:"+response.body().getCurrent().getCondition().getIcon());
                    
                    Uri uri = Uri.parse("http:"+response.body().getCurrent().getCondition().getIcon());
                    SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.simpleDraweeView);
                    draweeView.setImageURI(uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        return view;
    }
}
