package com.therock.practicaactividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private String correoR, contrasenaR;
    private TextView tCorreoProfile, tContrasenaProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle extras = getIntent().getExtras();
        correoR = extras.getString("correo");
        contrasenaR = extras.getString("contrasena");
        tCorreoProfile = (TextView) findViewById(R.id.tCorreoProfile);
        tContrasenaProfile = (TextView) findViewById(R.id.tContrasenaProfile);
        tCorreoProfile.setText(correoR);
        tContrasenaProfile.setText(contrasenaR);
    }

}