package com.gumeniuk.pear;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.UUID;



public class MapsFragment extends Fragment {

    private GoogleMap googleMap;
    private MapView mapView;
    private MyApplicationClass app;
    private ArrayList<MarkerInfo> markersInfo;
    private ProgressDialog mProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_maps,container, false);

        app = (MyApplicationClass)getActivity().getApplicationContext();
        markersInfo = app.getRealmMarkerData();

        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                downloadMarkers(googleMap);

               googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        createMarker(googleMap,latLng);
                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(marker.getPosition()).zoom(12).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        return false;
                    }
                });


        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            Toast.makeText(getActivity(), "You have to accept to enjoy all app's services!", Toast.LENGTH_LONG).show();
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
            }
        }
/*
                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(-34, 151);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));


                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                */
            }
            
        });

        return view;
    }

    private void downloadMarkers(GoogleMap googleMap) {
        if(markersInfo == null) return;
        LatLng latLng;
        showProgressDialog(getContext());
        for(MarkerInfo markerInfo : markersInfo){
            latLng = new LatLng(markerInfo.getLat(), markerInfo.getLng());
            googleMap.addMarker(new MarkerOptions().position(latLng).title(markerInfo.getTitle()).snippet(markerInfo.getDescription()));
        }
        hideProgressDialog();
    }

    private void createMarker(final GoogleMap googleMap, final LatLng latLng) {
        final   AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("New marker");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputTitle = new EditText(getContext());
        final EditText inputDescription = new EditText(getContext());

        inputTitle.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        inputTitle.setHint("Title");
        inputTitle.setHintTextColor(ContextCompat.getColor(getActivity(),R.color.grey));
        inputDescription.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        inputDescription.setHint("Description");
        inputDescription.setHintTextColor(ContextCompat.getColor(getActivity(),R.color.grey));

        layout.addView(inputTitle);
        layout.addView(inputDescription);
        builder.setView(layout);

        builder.setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MarkerInfo markerInfo = new MarkerInfo(inputTitle.getText().toString().trim(),inputDescription.getText().toString().trim());
                markerInfo.setId(UUID.randomUUID().toString());
                markerInfo.setLat(latLng.latitude);
                markerInfo.setLng(latLng.longitude);
                markerInfo.setMarkerUserName(app.getUserLogin());
                app.setRealmMarkerData(markerInfo);
                markersInfo.add(markerInfo);
                googleMap.addMarker(new MarkerOptions().position(latLng).title(inputTitle.getText().toString().trim()).snippet(inputDescription.getText().toString().trim()));
                Toast.makeText(getContext(), "Created new marker", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
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


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}
