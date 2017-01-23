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
    SharedPreferences person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnSingIn = (Button)findViewById(R.id.btnSingIn);
        btnSingIn.setOnClickListener(this);

        btnSingUp = (Button)findViewById(R.id.btnSingUp);
        btnSingUp.setOnClickListener(this);

        login = (EditText)findViewById(R.id.textLogin);
        password = (EditText)findViewById(R.id.textPassword);

    }

    @Override
    public void onClick(View v) {
        person = getSharedPreferences("Person",MODE_PRIVATE);
        login.setText(login.getText().toString().trim());
        switch (v.getId()){
            case R.id.btnSingIn:
                  if(isPerson() && checkPassword())
                    {
                        Intent intent = new Intent(this,WelActivity.class);
                        intent.putExtra("person",login.getText().toString());
                       startActivity(intent);
                        finish();
                   }
                else Toast.makeText(this, "Wrong login or password", Toast.LENGTH_SHORT).show();
                break;

            case  R.id.btnSingUp:
                startActivity(new Intent(this,RegActivity.class));
                break;

            default:
                break;
        }

    }

    public boolean isPerson(){
        return person.contains(login.getText().toString());
    }

    public boolean checkPassword(){
        return person.getString(login.getText().toString(),"").equals(encryption(login.getText().toString(),password.getText().toString(),true));
    }

    public String encryption(String log, String pass, boolean encrypt){

        char [] abc = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
                'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                '1','2','3','4','5','6','7','8','9','0','!','#',' ','^','&','*','(',')','-','_','+','=','<','>',',','.'};
        char [] login = log.toCharArray();
        char [] password = pass.toCharArray();
        char [] newPassword = new char[password.length];

        int displacement = 0;

        for (int i=0;i<login.length;i++){
            for (int j=0;j<abc.length;j++){
                if(login[i]==abc[j]){
                    displacement+=j;
                    continue;
                }
            }
        }

        displacement%=abc.length;

        if(encrypt){                        //encrypting
            for (int i=0; i<password.length;i++)
                for (int j=0; j<abc.length;j++)
                    if(password[i]==abc[j])
                        newPassword[i] = abc[(j+displacement)%abc.length];
        }
        else{                               //decipherment
            for (int i=0; i<password.length;i++)
                for (int j=0; j<abc.length;j++)
                    if(password[i]==abc[j])
                        newPassword[i] = abc[(j-displacement+abc.length)%abc.length];
        }

        pass="";

        for (int i=0;i<newPassword.length;i++)
            pass+=newPassword[i];

        return pass;
    }

}
