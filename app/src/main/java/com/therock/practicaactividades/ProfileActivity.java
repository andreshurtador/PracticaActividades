package com.therock.practicaactividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.mprincipal:
                intent = new Intent( ProfileActivity.this,MainActivity.class);
                intent.putExtra("correo", correoR);
                intent.putExtra("contrasena", contrasenaR);
                startActivity(intent);
                finish();
                break;
            case R.id.mPerfil:
                Toast.makeText(this, "Esta en Mi Perfil", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mcerrar:
                intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}


