package com.course.threeactivitieslogregwel;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class WelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel);


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
}
