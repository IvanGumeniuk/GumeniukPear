package com.gumeniuk.pear.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gumeniuk.pear.Database.RecyclerItem;
import com.gumeniuk.pear.MyApplicationClass;
import com.gumeniuk.pear.R;

import java.util.ArrayList;
import java.util.UUID;

public class RegActivity extends AppCompatActivity {

    EditText login,password,rep_password;
    Button btnRegistration, backToLogin;
    MyApplicationClass app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.Registration);

        login = (EditText)findViewById(R.id.textNewLogin);
        login.requestFocus();
        password = (EditText)findViewById(R.id.textNewPassword);
        rep_password = (EditText)findViewById(R.id.textRepeatPassword);

        btnRegistration = (Button)findViewById(R.id.btnRegistration);
        backToLogin = (Button)findViewById(R.id.backToLogin);

        app = ((MyApplicationClass)getApplicationContext());

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setText(login.getText().toString().trim());
                if(app.checkingPerson(login,password,rep_password))
                {
                    app.addNewPerson(login,password);
                    app.entering(login);
                    app.setUserLogin(login.getText().toString().trim());

                    ArrayList<RecyclerItem> items = new ArrayList<RecyclerItem>();
                    items.add(new RecyclerItem(UUID.randomUUID().toString(),"white pear",app.getUserLogin()));
                    items.add(new RecyclerItem(UUID.randomUUID().toString(),"black pear",app.getUserLogin()));
                    items.add(new RecyclerItem(UUID.randomUUID().toString(),"red pear",app.getUserLogin()));
                    items.add(new RecyclerItem(UUID.randomUUID().toString(),"blue pear",app.getUserLogin()));
                    items.add(new RecyclerItem(UUID.randomUUID().toString(),"green pear",app.getUserLogin()));
                    items.add(new RecyclerItem(UUID.randomUUID().toString(),"yellow pear",app.getUserLogin()));
                    items.add(new RecyclerItem(UUID.randomUUID().toString(),"pink pear",app.getUserLogin()));
                    items.add(new RecyclerItem(UUID.randomUUID().toString(),"grey pear",app.getUserLogin()));
                    items.add(new RecyclerItem(UUID.randomUUID().toString(),"purple pear",app.getUserLogin()));
                    items.add(new RecyclerItem(UUID.randomUUID().toString(),"orange pear",app.getUserLogin()));
                    app.setRealmData(items);
                    startActivity(new Intent(RegActivity.this, WelcActivity.class));
                    finish();
                }
            }
        });

    }
}
