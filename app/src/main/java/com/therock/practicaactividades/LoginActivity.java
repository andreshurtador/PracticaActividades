package com.therock.practicaactividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText eCorreo, eContrasena;
    private Button bIniciar;
    private String correoR, contrasenaR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        eCorreo = (EditText) findViewById(R.id.eCorreo);
        eContrasena = (EditText) findViewById(R.id.eContrasena);
        bIniciar = (Button) findViewById(R.id.bIniciar);
    }

    public void iniciar(View view) {
        if (correoR.equals(eCorreo.getText().toString()) && contrasenaR.equals(eContrasena.getText().toString())) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("correo", correoR);
            intent.putExtra("contrasena", contrasenaR);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Compruebe los campos", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            correoR = data.getExtras().getString("correo");
            contrasenaR = data.getExtras().getString("contrasena");
            Toast.makeText(this, correoR, Toast.LENGTH_SHORT).show();
            Log.d("correo", correoR);//Verificar en el monitor las variables
            Log.d("contrasena", contrasenaR);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void registrese(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent, 1234);
    }
}
