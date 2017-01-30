package com.course.threeactivitieslogregwel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegActivity extends AppCompatActivity {

    EditText login,password,rep_password;
    Button btnRegistration;
    MyApplicationClass app;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Registration");

        login = (EditText)findViewById(R.id.textNewLogin);
        login.requestFocus();
        password = (EditText)findViewById(R.id.textNewPassword);
        rep_password = (EditText)findViewById(R.id.textRepeatPassword);

        btnRegistration = (Button)findViewById(R.id.btnRegistration);

        app = ((MyApplicationClass)getApplicationContext());


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
