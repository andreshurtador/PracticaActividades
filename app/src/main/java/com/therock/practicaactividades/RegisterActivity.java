package com.therock.practicaactividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static boolean isEmailValid(String email) {
        boolean isValid;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        } else {
            isValid = false;
        }
        return isValid;
    }

    public void registrar(View view) {
        correo = eCorreo.getText().toString();
        contrasena = eContrasena.getText().toString();
        repetirContrasena = eRepetirContrasena.getText().toString();

        if (!TextUtils.isEmpty(correo) && !TextUtils.isEmpty(contrasena) && !TextUtils.isEmpty(repetirContrasena)) {
            if (isEmailValid(correo) == true) {
                if (contrasena.equals(repetirContrasena)) {
                    Intent intent = new Intent();
                    intent.putExtra("correo", correo);
                    intent.putExtra("contrasena", contrasena);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    eContrasena.setText("");
                    eRepetirContrasena.setText("");
                    Toast.makeText(getApplicationContext(), "Compruebe Contrasena", Toast.LENGTH_SHORT).show();
                    eCorreo.requestFocus();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Correo No Valido", Toast.LENGTH_SHORT).show();
                eCorreo.requestFocus();
            }
        } else {
            if (TextUtils.isEmpty(correo) || TextUtils.isEmpty(contrasena) || TextUtils.isEmpty(repetirContrasena)) {
                Toast.makeText(getApplicationContext(), "Compruebe Los Campos", Toast.LENGTH_SHORT).show();
            }

        }
    }


}



