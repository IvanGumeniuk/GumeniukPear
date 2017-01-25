package com.course.threeactivitieslogregwel;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel);

        Toast.makeText(this, "WelActivity OnCreate", Toast.LENGTH_SHORT).show();


        EditText textWelcome = (EditText)findViewById(R.id.textWelcome);

        Intent intent = getIntent();

        getSupportActionBar().setTitle(intent.getStringExtra("person"));
        textWelcome.setText("Welcome "+intent.getStringExtra("person")+"!");

        Button btnQuit = (Button)findViewById(R.id.btnQuit);

        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });



    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "WelActivity OnRestart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "WelActivity OnStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "WelActivity OnResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "WelActivity OnPause", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "WelActivity OnStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "WelActivity OnDestroy", Toast.LENGTH_SHORT).show();
    }
}
