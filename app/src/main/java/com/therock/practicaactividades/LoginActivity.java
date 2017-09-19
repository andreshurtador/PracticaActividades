package com.therock.practicaactividades;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private LoginButton loginButton;
    private EditText eCorreo, eContrasena;
    private Button bIniciar;
    private String correoR = "", contrasenaR = "";
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eCorreo = (EditText) findViewById(R.id.eCorreo);
        eContrasena = (EditText) findViewById(R.id.eContrasena);
        bIniciar = (Button) findViewById(R.id.bIniciar);


        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email"));

        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                goToMain();
                Toast.makeText(getApplicationContext(), "Login Exitoso", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Login Cancelado", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Login Error", Toast.LENGTH_SHORT).show();
            }
        });


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.therock.practicaactividades",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public void goToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("correo", correoR);
        intent.putExtra("contrasena", contrasenaR);
        startActivity(intent);
        finish();
    }


    public void iniciar(View view) {

        if (TextUtils.isEmpty(eCorreo.getText().toString()) || TextUtils.isEmpty(eContrasena.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Compruebe Los Campos", Toast.LENGTH_SHORT).show();
        } else {
            if (correoR.equals(eCorreo.getText().toString()) && contrasenaR.equals(eContrasena.getText().toString())) {
                goToMain();
            } else {
                Toast.makeText(this, "Compruebe los campos", Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 1234 && resultCode == RESULT_OK) {
            correoR = data.getExtras().getString("correo");
            contrasenaR = data.getExtras().getString("contrasena");

        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void registrese(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent, 1234);
    }

}



