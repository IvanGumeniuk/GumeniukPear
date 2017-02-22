package com.gumeniuk.pear.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gumeniuk.pear.MyApplicationClass;
import com.gumeniuk.pear.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSingIn, btnSingUp, btnExit;
    EditText login, password;
    MyApplicationClass app;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = ((MyApplicationClass)getApplicationContext());

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in

                    Intent intent = new Intent(MainActivity.this, WelcActivity.class);
                    app.setUserLogin(user.getDisplayName());
                    app.setLaunch(true);
                    startActivity(intent);

                    Log.d("Firebase", "onAuthStateChanged:signed_in:" + user.getUid());
                    finish();
                } else {

                    Log.d("Firebase", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

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
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
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

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}