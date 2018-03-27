package com.gumeniuk.pear.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.gumeniuk.pear.MyApplicationClass;
import com.gumeniuk.pear.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    Button btnSingIn, btnSingUp, btnExit;
    com.google.android.gms.common.SignInButton btnGoogle;
    EditText login, password;
    MyApplicationClass app;
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;
    public static GoogleApiClient mGoogleApiClient;
    private FirebaseAuth googleAuth;
    private FirebaseAuth.AuthStateListener googleAuthListener;
    private ProgressDialog mProgressDialog;
    private LoginButton btnFacebook;
    private CallbackManager mCallbackManager;
    private FirebaseAuth facebookAuth;
    private FirebaseAuth.AuthStateListener facebookAuthListener;
    private static boolean PERMISSIONS_GRANTED = false;
    private boolean alertIsWorking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = ((MyApplicationClass) getApplicationContext());
        FirebaseApp.initializeApp(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        if (app.isLogged() && !app.getEnteringLogin().equals("")) {
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

        btnGoogle = (com.google.android.gms.common.SignInButton) findViewById(R.id.btnGoogle);
        btnGoogle.setOnClickListener(this);

        login = (EditText) findViewById(R.id.textLogin);
        password = (EditText) findViewById(R.id.textPassword);


        googleAuth = FirebaseAuth.getInstance();
        googleAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && app.getEntryWay().equals("google")) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent intent = new Intent(MainActivity.this, WelcActivity.class);
                    app.setLaunch(true);
                    app.entering(user.getDisplayName());
                    startActivity(intent);
                    finish();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };


        facebookAuth = FirebaseAuth.getInstance();
        facebookAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && app.getEntryWay().equals("facebook")) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent intent = new Intent(MainActivity.this, WelcActivity.class);
                    app.setLaunch(true);
                    app.entering(user.getDisplayName());
                    startActivity(intent);
                    finish();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        mCallbackManager = CallbackManager.Factory.create();
        btnFacebook = (LoginButton) findViewById(R.id.btnFacebook);
        btnFacebook.setReadPermissions("email", "public_profile");
        btnFacebook.setOnClickListener(this);


    }

    private void checkPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_CONTACTS,
                        android.Manifest.permission.CALL_PHONE,
                        android.Manifest.permission.INTERNET,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.ACCESS_NETWORK_STATE,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.GET_ACCOUNTS},
                1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        alertIsWorking = false;
        for (int i = 0, j = 0, len = permissions.length; i < len; i++) {
            String permission = permissions[i];

            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                showPermissionAlert();
                if(alertIsWorking){
                    break;
                }

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    goToPermissionsSettings();
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{permission},
                            1);
                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }else{
                j++;
                if (j == len - 1)
                    PERMISSIONS_GRANTED = true;
                Log.d("qwerty",PERMISSIONS_GRANTED+" perm");
            }
        }
    }


    private void showPermissionAlert() {
        alertIsWorking =  true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions problem");
        builder.setMessage("Without permission you will not sing in into the app. \r\n Are you sure?")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //checkPermissions();
                        goToPermissionsSettings();
                    }
                })
                .setNegativeButton("I`m sure", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(MainActivity.this, "Now you can remove this app. " +
                                "Honestly this app is disaster", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
    }

    private void goToPermissionsSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 1);
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]

        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        facebookAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        FirebaseUser user = facebookAuth.getCurrentUser();
                        if (user != null) {
                            app.setUserLogin(user.getDisplayName());
                            app.setEntryWay("facebook");
                            app.setRealmData(app.readPhoneContacts());
                        }
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    //------------------------------facebook-------------------------------------
    @Override
    public void onStart() {
        super.onStart();
        googleAuth.addAuthStateListener(googleAuthListener);
        facebookAuth.addAuthStateListener(facebookAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleAuthListener != null) {
            googleAuth.removeAuthStateListener(googleAuthListener);
        }
        if (facebookAuthListener != null) {
            facebookAuth.removeAuthStateListener(facebookAuthListener);
        }
    }

    @Override
    public void onClick(View v) {
        login.setText(login.getText().toString().trim());
        switch (v.getId()) {
            case R.id.btnSingIn:
                checkPermissions();
                if ((app.isPerson(login) && app.checkPassword(login, password)) && PERMISSIONS_GRANTED) {
                    app.setEntryWay("default");
                    app.entering(login.getText().toString());
                    app.setUserLogin(login.getText().toString().trim());
                    startWelcome();
                } else Toast.makeText(this, R.string.WrongLogPass, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnGoogle:
                if (hasConnection(this)) {
                    app.setEntryWay("google");
                    showProgressDialog();
                    signIn();
                } else
                    Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnFacebook:
                if (hasConnection(this)) {
                    app.setEntryWay("facebook");
                    btnFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            Log.d(TAG, "facebook:onSuccess:" + loginResult);
                            handleFacebookAccessToken(loginResult.getAccessToken());
                        }

                        @Override
                        public void onCancel() {
                            Log.d(TAG, "facebook:onCancel");
                            hideProgressDialog();
                        }

                        @Override
                        public void onError(FacebookException error) {
                            Log.d(TAG, "facebook:onError", error);
                            hideProgressDialog();
                        }
                    });
                    showProgressDialog();
                } else
                    Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnSingUp:
                checkPermissions();
                if(PERMISSIONS_GRANTED) {
                    startActivity(new Intent(this, RegActivity.class));
                    finish();
                }
                break;
            case R.id.exit:
                finish();
                break;

            default:
                break;
        }

    }

    //-----------------------google---------------------------------------------
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (app.getEntryWay().equals("google")) {
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = result.getSignInAccount();
                    firebaseAuthWithGoogle(account);
                } else {
                    //   Toast.makeText(this, "Network connection problems", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                    // Google Sign In failed, update UI appropriately
                    // ...
                }

            }
        } else if (app.getEntryWay().equals("facebook")) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        googleAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        app.setUserLogin(acct.getDisplayName());
                        app.setEntryWay("google");
                        app.setRealmData(app.readPhoneContacts());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, R.string.Auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                        hideProgressDialog();
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, R.string.GooglePlaySer_error, Toast.LENGTH_SHORT).show();
        hideProgressDialog();
    }


    private void startWelcome() {
        Intent intent = new Intent(this, WelcActivity.class);
        app.setUserLogin(app.getEnteringLogin());
        app.setLaunch(true);
        startActivity(intent);
        finish();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public static boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null;
    }


}