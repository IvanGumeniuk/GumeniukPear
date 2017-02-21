package com.gumeniuk.pear.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gumeniuk.pear.MyApplicationClass;
import com.gumeniuk.pear.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSingIn, btnSingUp, btnExit;
    EditText login, password;
    MyApplicationClass app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = ((MyApplicationClass)getApplicationContext());

        if(app.isLogged() && !app.getEnteringLogin().equals("")){
            startWelcome();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.Login);

        btnSingIn = (Button) findViewById(R.id.btnSingIn);
        btnSingIn.setOnClickListener(this);

        btnSingUp = (Button) findViewById(R.id.btnSingUp);
        btnSingUp.setOnClickListener(this);

        btnExit = (Button) findViewById(R.id.exit);
        btnExit.setOnClickListener(this);

        login = (EditText) findViewById(R.id.textLogin);
        password = (EditText) findViewById(R.id.textPassword);
    }


    @Override
    public void onClick(View v) {
        login.setText(login.getText().toString().trim());
        switch (v.getId()) {
            case R.id.btnSingIn:
                if (app.isPerson(login) && app.checkPassword(login,password)) {
                    app.entering(login);
                    app.setUserLogin(login.getText().toString().trim());
                    startWelcome();
                } else Toast.makeText(this, R.string.WrongLogPass, Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnSingUp:
                startActivity(new Intent(this, RegActivity.class));
                finish();
            case R.id.exit:
                finish();
                break;

            default:
                break;
        }

    }

    private void startWelcome(){
        Intent intent = new Intent(this, WelcActivity.class);
        app.setUserLogin(app.getEnteringLogin());
        app.setLaunch(true);
        startActivity(intent);
        finish();
    }

}