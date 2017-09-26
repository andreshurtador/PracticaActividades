package com.therock.practicaactividades;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class ProfileActivity extends AppCompatActivity {

    private String correoR, contrasenaR;
    private TextView tCorreoProfile, tContrasenaProfile,tlabelMicontrasena;
    private ImageView profilePicture;
    private GoogleApiClient mGoogleApiClient;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile);

        Bundle extras = getIntent().getExtras();
        correoR = extras.getString("correo");
        contrasenaR = extras.getString("contrasena");

        tCorreoProfile = (TextView) findViewById(R.id.tCorreoProfile);
        tContrasenaProfile = (TextView) findViewById(R.id.tContrasenaProfile);
        tlabelMicontrasena = (TextView) findViewById(R.id.tlabelMicontrasena);

        tCorreoProfile.setText(correoR);
        tContrasenaProfile.setText(contrasenaR);


        profilePicture = (ImageView) findViewById(R.id.imageView);

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
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()){
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();

            tContrasenaProfile.setText(account.getDisplayName());
            tCorreoProfile.setText(account.getEmail());
            tlabelMicontrasena.setText("Mi usuario");

            //profilePicture.setText(account.getId());
        }else{

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
                intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.putExtra("correo", correoR);
                intent.putExtra("contrasena", contrasenaR);
                startActivity(intent);
                finish();
                break;

            case R.id.mcerrar:
                intent = new Intent(ProfileActivity.this, LoginActivity.class);
                //cerrar sesion google
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()){
                            startActivity(intent);
                        }
                    }
                });



                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}


