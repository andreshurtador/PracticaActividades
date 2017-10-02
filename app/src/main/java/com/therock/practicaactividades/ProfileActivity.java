package com.therock.practicaactividades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import static android.R.attr.resource;


public class ProfileActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private int optLog;
    //1. facebook
    //2.google
    //3.correo y contrasena
    private String correoR, contrasenaR, correoG, nameG, urlG, nameF, urlF, correoF;
    private TextView tCorreoProfile, tContrasenaProfile;
    private ImageView profilePicture;
    private GoogleApiClient mGoogleApiClient;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle extras = getIntent().getExtras();
        correoR = extras.getString("correo");
        contrasenaR = extras.getString("password");
        //optLog = extras.getInt("optlog");
        nameG = extras.getString("nameG");
        correoG = extras.getString("correoG");
        urlG = extras.getString("urlG");
        nameF = extras.getString("nameF");
        urlF = extras.getString("urlF");
        correoF = extras.getString("correoF");

        tCorreoProfile = (TextView) findViewById(R.id.tCorreoProfile);
        tContrasenaProfile = (TextView) findViewById(R.id.tContrasenaProfile);


        tCorreoProfile.setText(correoR);
        tContrasenaProfile.setText(contrasenaR);


        profilePicture = (ImageView) findViewById(R.id.imageView);

        prefs = getSharedPreferences("SP" , Context.MODE_PRIVATE);
        editor = prefs.edit();
        final int optLog = prefs.getInt("optlog",0);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(), "Error de login en Google", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        if (optLog == 1) {
            tCorreoProfile.setText("Usuario :" + "\n" + nameF);
            tContrasenaProfile.setText("Correo" + "\n" + correoF);
            Glide.with(this).load(urlF).crossFade().placeholder(R.drawable.imagen_perfil).into(profilePicture);
        } else if (optLog == 2) {
            tCorreoProfile.setText("Usuario :" + "\n" + nameG);
            tContrasenaProfile.setText("Correo:" + "\n" + correoG);
            Glide.with(this).load(urlG).crossFade().placeholder(R.drawable.imagen_perfil).into(profilePicture);
        } else if (optLog == 3) {
            tCorreoProfile.setText("Usuario :" + "\n" + correoR);
            tContrasenaProfile.setText("Contraseña :" + "\n" + contrasenaR);
            profilePicture.setImageResource(R.drawable.imagen_perfil);

        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();

            tContrasenaProfile.setText(account.getDisplayName());
            tCorreoProfile.setText(account.getEmail());


            //profilePicture.setText(account.getId());
        } else {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        final Intent intent;
        switch (id) {
            case R.id.mprincipal:
                prefs = getSharedPreferences("SP", Context.MODE_PRIVATE);
                editor = prefs.edit();
                //almacenamos el valor de optLog
                editor.putInt("optlog",optLog);
                intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.putExtra("correo",correoR);
                intent.putExtra("password",contrasenaR);
               // intent.putExtra("optlog",optLog);
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

                intent = new Intent(ProfileActivity.this, LoginActivity.class);
                if (optLog == 1) {
                    prefs = getSharedPreferences("SP",Context.MODE_PRIVATE);
                    editor = prefs.edit();
                    //almacenamos el valor de optLog
                    editor.putInt("optlog",0);
                    editor.commit();
                    LoginManager.getInstance().logOut();
                    intent.putExtra("correo", correoR);
                    intent.putExtra("password", contrasenaR);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (optLog == 2) {
                    //cerrar sesion google
                    prefs = getSharedPreferences("SP",Context.MODE_PRIVATE);
                    editor = prefs.edit();
                    //almacenamos el valor de optLog
                    editor.putInt("optlog",0);
                    editor.commit();
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                intent.putExtra("correo", correoR);
                                intent.putExtra("password", contrasenaR);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });
                    if (optLog == 3) {
                        intent.putExtra("correo", correoR);
                        intent.putExtra("password", contrasenaR);
                        prefs = getSharedPreferences("SP",Context.MODE_PRIVATE);
                        editor = prefs.edit();
                        //almacenamos el valor de optLog
                        editor.putInt("optlog",0);
                        editor.commit();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    finish();
                    break;
                }

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}


