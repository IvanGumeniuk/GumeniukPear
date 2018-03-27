package com.gumeniuk.pear;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.gumeniuk.pear.Database.MarkerInfo;

import java.util.ArrayList;
import java.util.UUID;

import io.realm.Realm;


public class MapsFragment extends Fragment {

    private GoogleMap googleMap;
    private MapView mapView;
    private MyApplicationClass app;
    private ArrayList<MarkerInfo> markersInfo;
    private ProgressDialog mProgressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        if(!hasConnection(getActivity()))
            Toast.makeText(getActivity(), R.string.check_internet, Toast.LENGTH_SHORT).show();
        
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        app = (MyApplicationClass) getActivity().getApplicationContext();
        markersInfo = app.getRealmMarkerData();

        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

            init();


        return view;
    }

    public void init(){
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                downloadMarkers(googleMap);

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        createMarker(googleMap, latLng);
                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(marker.getPosition()).zoom(10).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        clickMarker(marker);

                        return false;
                    }
                });

                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    googleMap.setMyLocationEnabled(true);
                } else {
                    Toast.makeText(getActivity(), R.string.you_have_to_accept_serv, Toast.LENGTH_LONG).show();
                }

            }

        });
    }

    private void clickMarker(final Marker marker) {
        final   AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Marker menu");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final Button btnEdit = new Button(getContext());
        final Button btnDelete = new Button(getContext());


        layout.addView(btnEdit);
        layout.addView(btnDelete);
        builder.setView(layout);

        builder.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog dialog = builder.create();

        btnEdit.setText(R.string.EditMarker);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMarker(marker, dialog);
            }
        });

        btnDelete.setText(R.string.DeleteMarker);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMarker(marker, dialog);
            }
        });

        dialog.show();
    }

    private void deleteMarker(final Marker marker, final AlertDialog Olddialog) {
        MarkerInfo myMarker = new MarkerInfo();
        for(int i=0; i<markersInfo.size();i++) {
            showProgressDialog(getActivity());
            if (marker.getPosition().latitude == markersInfo.get(i).getLat()
                    && marker.getPosition().longitude == markersInfo.get(i).getLng()) {
                myMarker = markersInfo.get(i);
                hideProgressDialog();
                break;
            }
        }
        if(myMarker == null) return;

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("You want ot remove marker. Are you sure?");

        final MarkerInfo finalMyMarker = myMarker;
        builder.setPositiveButton(getActivity().getString(R.string.Ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                app.removeMarkerFromRealm(finalMyMarker);
                marker.remove();
                Olddialog.dismiss();

                Toast.makeText(getActivity(), R.string.Deleted, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(getActivity().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void editMarker(final Marker marker, final AlertDialog oldDialog) {

        MarkerInfo myMarker = new MarkerInfo();
        int markerIter = 0;
        for(int i=0; i<markersInfo.size();i++) {
            showProgressDialog(getActivity());
            if (marker.getPosition().latitude == markersInfo.get(i).getLat()
                    && marker.getPosition().longitude == markersInfo.get(i).getLng()) {
                myMarker = markersInfo.get(i);
                markerIter = i;
                hideProgressDialog();
                break;
            }
        }
            if(myMarker == null) return;

        final   AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit marker");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputTitle = new EditText(getContext());
        final EditText inputDescription = new EditText(getContext());

        inputTitle.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        inputTitle.setHint(myMarker.getTitle());
        inputTitle.setHintTextColor(ContextCompat.getColor(getActivity(),R.color.grey));
        inputDescription.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        inputDescription.setHint(myMarker.getDescription());
        inputDescription.setHintTextColor(ContextCompat.getColor(getActivity(),R.color.grey));

        layout.addView(inputTitle);
        layout.addView(inputDescription);
        builder.setView(layout);

        final MarkerInfo finalMyMarker = myMarker;
        final int finalMarkerIter = markerIter;
        builder.setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                app.getRealm().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        finalMyMarker.setTitle(inputTitle.getText().toString().trim());
                        finalMyMarker.setDescription(inputDescription.getText().toString().trim());

                        markersInfo.add(finalMarkerIter,finalMyMarker);
                    }
                });

                app.setRealmMarkerData(finalMyMarker,true);
                LatLng latLng = new LatLng(finalMyMarker.getLat(),finalMyMarker.getLng());

                marker.remove();

                googleMap.addMarker(new MarkerOptions().position(latLng).title(inputTitle.getText().toString().trim()).snippet(inputDescription.getText().toString().trim()));
                oldDialog.dismiss();
                Toast.makeText(getContext(), R.string.MarkerWasDeleted, Toast.LENGTH_SHORT).show();
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
                markerInfo.setEnteringWay(app.getEntryWay());
                app.setRealmMarkerData(markerInfo, false);
                markersInfo.add(markerInfo);
                googleMap.addMarker(new MarkerOptions().position(latLng).title(inputTitle.getText().toString().trim()).snippet(inputDescription.getText().toString().trim()));
                Toast.makeText(getContext(), R.string.CreatedNewMarker, Toast.LENGTH_SHORT).show();
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



    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null;
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
