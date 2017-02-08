package com.gumeniuk.pear;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Toast;

import com.gumeniuk.pear.Database.RecyclerItem;
import com.gumeniuk.pear.Database.User;

import java.util.ArrayList;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;


public class MyApplicationClass extends Application {

    SharedPreferences person;
    SharedPreferences.Editor edit;
    private MyAdapter adapter;
    private String userLogin;
    private Realm realm;
    private RealmConfiguration config;

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    public Realm getRealm() {
        return realm;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public MyAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(MyAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        person = getSharedPreferences(getString(R.string.SPFileName),MODE_PRIVATE);
        edit = person.edit();
//------------------------------------------------------------------------

        Realm.init(this);
         config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();

//----------------------------------------------------------------------------
    }


    public void entering(EditText login){
        edit.putBoolean(getString(R.string.logged),true);
        edit.putString(getString(R.string.user),login.getText().toString().trim());
        edit.apply();
    }

    public String getEnteringLogin(){
        return person.getString(getString(R.string.user),"");
    }

    public boolean isLogged(){
        if(person.getBoolean(getString(R.string.logged), true)) return true;
        return false;
    }

    public void logOut(){
        edit.putBoolean(getString(R.string.logged),false);
        edit.putString(getString(R.string.user),"");
        edit.apply();
    }

    public void addNewPerson(EditText login, EditText password){
        edit.putString(login.getText().toString() , encryption(login.getText().toString(),password.getText().toString(),true));
        edit.apply();

        realm.beginTransaction();
        User user =  realm.createObject(User.class, UUID.randomUUID().toString());
        user.setName(login.getText().toString().trim());
        realm.commitTransaction();

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
                Toast.makeText(this, R.string.LoginContainInvalidSymbols, Toast.LENGTH_LONG).show();
                return false;
            }
        }

        for (int i=0;i<pass.length();i++) {
            if(pass.codePointAt(i)>255){
                Toast.makeText(this, R.string.PasswordContainInvalidSymbols, Toast.LENGTH_LONG).show();
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


    public void setRealmData(final ArrayList <RecyclerItem> items){

        final ArrayList<RecyclerItem> recyclerItems = getRealmData();
        recyclerItems.addAll(items);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmList<RecyclerItem> realmList = new RealmList<RecyclerItem>();
                realmList.addAll(recyclerItems);
                realm.copyToRealmOrUpdate(realmList);
            }
        });
    }

    public ArrayList<RecyclerItem> getRealmData(){
        ArrayList<RecyclerItem> recyclerItems = new ArrayList<>();

        realm.beginTransaction();
        RealmResults<RecyclerItem> result = realm.where(RecyclerItem.class).equalTo("itemUserName", getUserLogin()).findAll();
        recyclerItems.addAll(result);
        realm.commitTransaction();
        return recyclerItems;
    }

    public void removeFromRealmData(final String id){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<RecyclerItem> result = realm.where(RecyclerItem.class).equalTo("id", id).findAll();
                result.deleteAllFromRealm();
            }
        });
    }

    public void updateRealmData(final RecyclerItem item, final String newName){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RecyclerItem result = realm.where(RecyclerItem.class).equalTo("id", item.getId()).findFirst();
                result.setItemName(newName);
            }
        });
    }

    public RecyclerItem getRecyclerItemFromRealmData(final String id){
        final RecyclerItem[] result = {null};
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
               result[0] = realm.where(RecyclerItem.class).equalTo("id", id ).findFirst();

            }
        });
        return result[0];
    }

    
    
 /*   public ArrayList<RecyclerItem> JSONData(ArrayList<RecyclerItem> list, String userName, boolean record_read){
            SharedPreferences preferences = getSharedPreferences(userName, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            JSONObject object = new JSONObject();
            JSONArray itemArray = new JSONArray();


          //if record_read == true then data will be recorded to JSON
           //     else data will be read from JSON
        if(record_read) {
            try {
                for (int i = 0; i < list.size(); i++)
                    itemArray.put(i, list.get(i).getItemName());

                object.put(userName, itemArray);

            } catch (JSONException e) {}

            editor.putString(getString(R.string.UserData), object.toString());
            editor.apply();
        }
        else
            if(!record_read) {
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
    */

}



