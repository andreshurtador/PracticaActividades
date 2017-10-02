package com.therock.practicaactividades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;



public class MainActivity extends AppCompatActivity{
    private int optLog;
    //0. no hay
    //1. facebook
    //2.google
    //3.correo y contrasena
    private String correoR, contrasenaR,correoG,nameG,urlG,nameF,urlF,correoF;
    GoogleApiClient mGoogleApiClient;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bundle extras = getIntent().getExtras();
        //correoR = extras.getString("correo");
        //contrasenaR = extras.getString("contrasena");

        Bundle extras = this.getIntent().getExtras();
        if(extras !=null) {
            correoR = extras.getString("correo");
            contrasenaR = extras.getString("password");
            //optLog = extras.getInt("optlog");
            nameG = extras.getString("nameG");
            correoG = extras.getString("correoG");
            urlG = extras.getString("urlG");
            nameF = extras.getString("nameF");
            urlF = extras.getString("urlF");
            correoF = extras.getString("correoF");

        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(),"Error de login en Google",Toast.LENGTH_SHORT).show();
                    }
                } )
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        final Intent intent;
        prefs = getSharedPreferences("SP" , Context.MODE_PRIVATE);
        editor = prefs.edit();
        switch (id) {

            case R.id.mPerfil:
                prefs = getSharedPreferences("SP", Context.MODE_PRIVATE);
                editor = prefs.edit();
                editor.putInt("optlog",optLog);
                intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("correo",correoR);
                intent.putExtra("password",contrasenaR);
                //intent.putExtra("optlog",optLog);
                intent.putExtra("nameG",nameG);
                intent.putExtra("correoG",correoG);
                intent.putExtra("urlG",urlG);
                intent.putExtra("nameF",nameF);
                intent.putExtra("urlF",urlF);
                intent.putExtra("correoF",correoF);
                startActivity(intent);
                finish();
                break;

            case R.id.mcerrar:
                intent = new Intent(MainActivity.this, LoginActivity.class);
                if(optLog==1){
                    prefs = getSharedPreferences("SP", Context.MODE_PRIVATE);
                    editor = prefs.edit();
                    editor.putInt("optlog",0);
                    editor.commit();
                    LoginManager.getInstance().logOut();
                    intent.putExtra("correo",correoR);
                    intent.putExtra("password",contrasenaR);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else if(optLog==2){
                    //cerrar sesion google
                    prefs = getSharedPreferences("SP",Context.MODE_PRIVATE);
                    editor = prefs.edit();
                    //almacenamos el valor de optLog
                    editor.putInt("optlog",0);
                    editor.commit();
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()){
                                intent.putExtra("correo",correoR);
                                intent.putExtra("password",contrasenaR);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                             }
                            else {
                                Toast.makeText(getApplicationContext(),"No se pudo cerrar session",Toast.LENGTH_SHORT).show();}

                        }
                    });
            }else if(optLog==3){
                    prefs = getSharedPreferences("SP",Context.MODE_PRIVATE);
                    editor = prefs.edit();
                    //almacenamos el valor de optLog
                    editor.putInt("optlog",0);
                    editor.commit();
                    intent.putExtra("correo",correoR);
                    intent.putExtra("password",contrasenaR);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    }
                    finish();
                    break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
