package com.course.threeactivitieslogregwel;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class WelActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private ArrayList<RecyclerItem> listItems;
    private FloatingActionButton floatButton;
    private String dialogEditPressed = "";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel);

        Intent intent = getIntent();
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(intent.getStringExtra(getString(R.string.SPFileName)));




        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        floatButton = (FloatingActionButton)findViewById(R.id.float_btn);

        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressFloatingButton();
            }
        });

        listItems = new ArrayList<>();
        //Generate sample data

        for (int i = 0; i<10; i++) {
            listItems.add(new RecyclerItem("Pear " + (i + 1)));
        }

        //Set adapter
        adapter = new MyAdapter(listItems, this);
        recyclerView.setAdapter(adapter);



    }

            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                Toast.makeText(WelActivity.this, "onQuerryTextChange", Toast.LENGTH_SHORT).show();

                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.search_menu,menu);

                Toast.makeText(WelActivity.this, "listener", Toast.LENGTH_SHORT).show();

                MenuItem menuItem = menu.findItem(R.id.search);
                SearchView searchView = (SearchView) menuItem.getActionView();

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        adapter.filter(newText);
                        return false;
                    }
                });

                return super.onCreateOptionsMenu(menu);
            }

    public void onPressFloatingButton(){

        final   AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New item");

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialogEditPressed = input.getText().toString().trim();
                listItems.add(new RecyclerItem(dialogEditPressed));
                adapter.notifyDataSetChanged();
                Toast.makeText(WelActivity.this, "Created new item "+dialogEditPressed, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

}
