package com.course.threeactivitieslogregwel;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class WelActivity extends FragmentActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<RecyclerItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel);

        Intent intent = getIntent();

        initToolbar();
        toolbar.setTitle(intent.getStringExtra(getString(R.string.SPFileName)));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();
        //Generate sample data

        for (int i = 0; i<10; i++) {
            listItems.add(new RecyclerItem("Item " + (i + 1)));
        }

        //Set adapter
        adapter = new MyAdapter(listItems, this);
        recyclerView.setAdapter(adapter);

    }

    private void initToolbar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        toolbar.inflateMenu(R.menu.search_menu);

    }

}
