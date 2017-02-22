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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.gumeniuk.pear.MyApplicationClass;
import com.gumeniuk.pear.R;

public class RegActivity extends AppCompatActivity {

    EditText login,password,rep_password;
    Button btnRegistration, backToLogin;
    MyApplicationClass app;
    private FirebaseAuth mAuth;


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

        mAuth = FirebaseAuth.getInstance();

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegActivity.this,MainActivity.class));
                finish();
            }
        });

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setText(login.getText().toString().trim());
                if(app.checkingPerson(login,password,rep_password)) {
                    app.addNewPerson(login, password);
                    app.entering(login);
                    app.setUserLogin(login.getText().toString().trim());
                    app.setRealmData(app.readPhoneContacts());
                    app.setLaunch(true);
                    startActivity(new Intent(RegActivity.this, WelcActivity.class));
                    finish();
                }

            }
        });

    }

    private void googleRegistration(){
        mAuth.createUserWithEmailAndPassword(login.getText().toString().trim(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Firebase_Reg", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
