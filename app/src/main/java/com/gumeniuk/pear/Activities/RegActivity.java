package com.gumeniuk.pear.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gumeniuk.pear.MyApplicationClass;
import com.gumeniuk.pear.R;

public class RegActivity extends AppCompatActivity {

    EditText login,password,rep_password;
    Button btnRegistration, backToLogin;
    MyApplicationClass app;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
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
                    finish();
                }
            }
        });

    }
}
