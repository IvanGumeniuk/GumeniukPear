package com.gumeniuk.pear;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MyApplicationClass extends Application {

    SharedPreferences person;
    SharedPreferences.Editor edit;

    @Override
    public void onCreate() {
        super.onCreate();
        person = getSharedPreferences(getString(R.string.SPFileName),MODE_PRIVATE);
        edit = person.edit();
    }

    public void addNewPerson(EditText login, EditText password){
        edit.putString(login.getText().toString() , encryption(login.getText().toString(),password.getText().toString(),true));
        edit.apply();

        Toast.makeText(this, getString(R.string.newPerson)+login.getText().toString()+getString(R.string.wasRegistered), Toast.LENGTH_SHORT).show();
    }


    public boolean isPerson(EditText login) {
        return person.contains(login.getText().toString());
    }

    public boolean checkPassword(EditText login, EditText password){
        return person.getString(login.getText().toString(),"").equals(encryption(login.getText().toString(),password.getText().toString(),true));
    }

    public boolean checkingPerson(EditText login, EditText password, EditText rep_password){

        login.setText(login.getText().toString().trim());

        String log = login.getText().toString();
        String pass = password.getText().toString();

        for (int i=0;i<log.length();i++) {
            if(log.codePointAt(i)>255){
                Toast.makeText(this, "Login contains invalid symbols", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        for (int i=0;i<pass.length();i++) {
            if(pass.codePointAt(i)>255){
                Toast.makeText(this, "Password contains invalid symbols", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        if(person.contains(login.getText().toString())){
            Toast.makeText(this, R.string.alreadyExist, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(login.getText().length()<3){
            Toast.makeText(this, R.string.shortLogin, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(password.getText().length()<5){
            Toast.makeText(this, R.string.shortPassword, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!password.getText().toString().equals(rep_password.getText().toString())){
            Toast.makeText(this, R.string.PassDontMatch, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public String encryption(String log, String pass, boolean encrypt){

        char [] password = new char[pass.length()];
        String newPassword = "";
        int displacement = 0;

        for (int i=0;i<log.length();i++)
            displacement+=log.codePointAt(i);

        displacement%=255;

        if(encrypt)
            for (int i=0; i<pass.length();i++)
                password[i] = (char) ((pass.codePointAt(i) + displacement)%255);
        else
            for (int i=0; i<pass.length();i++)
                password[i]= (char)((pass.codePointAt(i)-displacement+255)%255);

        for (int i=0; i<password.length;i++)
            newPassword+=password[i];

        return newPassword;
    }


    public ArrayList<RecyclerItem> JSONData(ArrayList<RecyclerItem> list, String userName, boolean write_read){

            SharedPreferences preferences = getSharedPreferences(userName, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            JSONObject object = new JSONObject();
            JSONArray itemArray = new JSONArray();

        if(write_read) {
            try {
                for (int i = 0; i < list.size(); i++)
                    itemArray.put(i, list.get(i).getItemName());

                object.put(userName, itemArray);

            } catch (JSONException e) {}

            editor.putString(getString(R.string.UserData), object.toString());
            editor.apply();
        }
        else
            if(!write_read) {
                try {
                    object = new JSONObject(preferences.getString(getString(R.string.UserData), ""));

                    itemArray = object.getJSONArray(userName);

                    for (int i = 0; i < itemArray.length(); i++)
                        list.add(i, new RecyclerItem(itemArray.getString(i)));
                } catch (JSONException e) {
                }
                return list;
            }
        return null;
    }


}



