package com.gumeniuk.pear.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.gumeniuk.pear.MapsFragment;
import com.gumeniuk.pear.MyApplicationClass;
import com.gumeniuk.pear.R;
import com.gumeniuk.pear.WeatherFragment;
import com.gumeniuk.pear.WelFragment;

public class WelcActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MyApplicationClass app;

    private WelFragment welFragment;
    private WeatherFragment weatherFragment;
    private MapsFragment mapsFragment;
    private FragmentManager fmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welc);

        app = ((MyApplicationClass) getApplicationContext());

        welFragment = new WelFragment();
        weatherFragment = new WeatherFragment();
        mapsFragment = new MapsFragment();
        mapsFragment.setRetainInstance(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(app.getUserLogin());


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView head = (TextView) header.findViewById(R.id.headerText);
        head.setText(app.getUserLogin());

        fmanager = getSupportFragmentManager();
        if(app.isLaunch()){
            fmanager.beginTransaction().replace(R.id.fragment,welFragment).commit();
            app.setLaunch(false);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);

        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(app.getAdapter()!= null)
                    app.getAdapter().searchFilter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        int id = item.getItemId();

        if (id == R.id.session_finish) {
            FirebaseAuth.getInstance().signOut();
            app.logOut();
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }else
        if(id == R.id.showContacts)
        {
            if(app.getIsContacts()) return true;
            else{
                fmanager.beginTransaction().replace(R.id.fragment,welFragment).commit();
            }
        }else
        if(id == R.id.showWeather){
            fmanager.beginTransaction().replace(R.id.fragment,weatherFragment).commit();
            app.setIsContacts(false);
        }else
        if(id == R.id.showGoogleMaps){
            fmanager.beginTransaction().replace(R.id.fragment,mapsFragment).commit();
            app.setIsContacts(false);
        }


        return true;
    }
}
