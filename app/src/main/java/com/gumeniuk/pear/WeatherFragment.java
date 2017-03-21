package com.gumeniuk.pear;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
    private MyApplicationClass app;
    private TextView wTemp,wFeels,wCity,wSky;
    private List<Forecastday> posts;
    private ProgressDialog mProgressDialog;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.weather_fragment, container, false);

            showProgressDialog(getActivity());

        app = ((MyApplicationClass)getActivity().getApplicationContext());
        wTemp = (TextView)view.findViewById(R.id.weatherTemp);
        wFeels = (TextView)view.findViewById(R.id.weatherFeels);
        wCity = (TextView)view.findViewById(R.id.weatherCity);
        wSky = (TextView)view.findViewById(R.id.weatherSky);

        GPSTracker gpsTracker = new GPSTracker(getActivity(),getActivity());
        gpsTracker.getLocation();
        Log.d("lonlat","Weathe -  "+ gpsTracker.getLocation().toString());

        posts = new ArrayList<>();

        app.getForecastWeather().getData("8846dd25320e47fd935192031172002",gpsTracker.getLatitude()+","+gpsTracker.getLongitude()).enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                posts.addAll(response.body().getForecast().getForecastday());

            }
            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.check_internet, Toast.LENGTH_LONG).show();
            }
        });


        app.getCurrentWeather().getData("8846dd25320e47fd935192031172002", gpsTracker.getLatitude()+","+gpsTracker.getLongitude()).enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                try {
                    String city = response.body().getLocation().getName();
                    String country = response.body().getLocation().getCountry();
                    String status = response.body().getCurrent().getCondition().getText();
                    String tmpC = response.body().getCurrent().getTempC().toString();
                    String qw = response.body().getCurrent().getFeelslikeC().toString();

                    wCity.setText(city+", "+country);
                    wSky.setText(status);
                    wTemp.setText(tmpC+"°C");
                    wFeels.setText("Feels like "+qw+"°C");

                    Uri uri = Uri.parse("http:"+response.body().getCurrent().getCondition().getIcon());
                    SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.simpleDraweeView);
                    draweeView.setImageURI(uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.check_internet, Toast.LENGTH_LONG).show();
            }
        });

            hideProgressDialog();
        return view;
    }

    public void showProgressDialog(Context context) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


}
