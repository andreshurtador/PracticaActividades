package com.therock.practicaactividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {
    private String correo, contrasena, repetirContrasena;
    private EditText eCorreo, eContrasena, eRepetirContrasena;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        eCorreo = (EditText) findViewById(R.id.eCorreo);
        eContrasena = (EditText) findViewById(R.id.eContrasena);
        eRepetirContrasena = (EditText) findViewById(R.id.eRepetirContrasena);

    }

    public void registrar(View view) {
        correo = eCorreo.getText().toString();
        contrasena = eContrasena.getText().toString();
        repetirContrasena = eRepetirContrasena.getText().toString();

        Intent intent = new Intent();
        intent.putExtra("correo", correo);
        intent.putExtra("contrasena", contrasena);
        setResult(RESULT_OK, intent);
        finish();


    }


}

