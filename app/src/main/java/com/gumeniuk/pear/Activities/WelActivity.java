package com.gumeniuk.pear.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.gumeniuk.pear.MyApplicationClass;
import com.gumeniuk.pear.R;

public class WelActivity extends AppCompatActivity {

    public Toolbar toolbar;
    private MyApplicationClass app;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel);

        Intent intent = getIntent();

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(intent.getStringExtra(getString(R.string.SPFileName)));

        app = ((MyApplicationClass) getApplicationContext());
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


}
