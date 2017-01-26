package com.course.threeactivitieslogregwel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSingIn, btnSingUp;
    EditText login, password;
    MyApplicationClass app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnSingIn = (Button) findViewById(R.id.btnSingIn);
        btnSingIn.setOnClickListener(this);

        btnSingUp = (Button) findViewById(R.id.btnSingUp);
        btnSingUp.setOnClickListener(this);

        login = (EditText) findViewById(R.id.textLogin);
        password = (EditText) findViewById(R.id.textPassword);

        app = ((MyApplicationClass)getApplicationContext());

    }

    @Override
    public void onClick(View v) {
        login.setText(login.getText().toString().trim());
        switch (v.getId()) {
            case R.id.btnSingIn:
                if (app.isPerson(login) && app.checkPassword(login,password)) {
                    Intent intent = new Intent(this, WelActivity.class);
                    intent.putExtra("person", login.getText().toString());
                    startActivity(intent);
                    finish();
                } else Toast.makeText(this, "Wrong login or password", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnSingUp:
                startActivity(new Intent(this, RegActivity.class));
                break;

            default:
                break;
        }

    }

}