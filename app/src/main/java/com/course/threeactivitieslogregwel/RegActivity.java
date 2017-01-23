package com.course.threeactivitieslogregwel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegActivity extends AppCompatActivity {

    EditText login,password,rep_password;
    Button btnRegistration;
    SharedPreferences person;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        login = (EditText)findViewById(R.id.textNewLogin);
        login.requestFocus();
        password = (EditText)findViewById(R.id.textNewPassword);
        rep_password = (EditText)findViewById(R.id.textRepeatPassword);

        btnRegistration = (Button)findViewById(R.id.btnRegistration);

        person = getSharedPreferences("Person",MODE_PRIVATE);


        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkingPerson())
                    newPerson();
            }
        });
    }

    public void newPerson(){
        SharedPreferences.Editor edit = person.edit();
        edit.putString(login.getText().toString() , encryption(login.getText().toString(),password.getText().toString(),true));
        edit.apply();

        Toast.makeText(this, "New person "+login.getText().toString()+" was registered", Toast.LENGTH_SHORT).show();

       // startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    public boolean checkingPerson(){

        char [] abc = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
                'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                '1','2','3','4','5','6','7','8','9','0','!','#','^','&','*','(',')','-','_','+','=','<','>',',','.'};

        login.setText(login.getText().toString().trim());

        char [] log = login.getText().toString().toCharArray();
        char [] pass = password.getText().toString().toCharArray();
        boolean flag;

        for (int i=0;i<log.length;i++) {
            flag = false;

            for (int j = 0; j < abc.length; j++)
                if(log[i]==(abc[j])) flag = true;

            if(!flag) {
                Toast.makeText(this, "Login contains invalid symbols \n" +
                        "You can use A-Z a-z !#^&*()_-+=,.<>", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        for (int i=0;i<pass.length;i++) {
            flag = false;

            for (int j = 0; j < abc.length; j++)
                if(pass[i]==(abc[j])) flag = true;

            if(!flag) {
                Toast.makeText(this, "Password contains invalid symbols \n" +
                        "You can use A-Z a-z !#^&*()_-+=,.<>", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        if(person.contains(login.getText().toString())){
            Toast.makeText(this, "This person already exists", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(login.getText().length()<5){
            Toast.makeText(this, "Login is too short (min 5 symbols)", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(password.getText().length()<5){
            Toast.makeText(this, "Password is too short (min 5 symbols)", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!password.getText().toString().equals(rep_password.getText().toString())){
            Toast.makeText(RegActivity.this, "Passwords don`t match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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
