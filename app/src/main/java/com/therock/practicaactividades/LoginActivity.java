package com.therock.practicaactividades;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private int optLog;
    //0. no hay
    //1.facebook
    //2.google
    //3.correo y contrasena
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private LoginButton loginButton;
    private EditText eCorreo, eContrasena;
    private Button bIniciar;
    private String correoR = "", contrasenaR = "", correoF, urlF, nameF,urlG, correoG, nameG;
    private CallbackManager callbackManager;

    GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 1357;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //login usuario y contrasena
        eCorreo = (EditText) findViewById(R.id.eCorreo);
        eContrasena = (EditText) findViewById(R.id.eContrasena);
        bIniciar = (Button) findViewById(R.id.bIniciar);

        //extras para intercambiar informacion
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            correoR = extras.getString("correo");
            contrasenaR = extras.getString("password");
        }

//Inicio Login Google
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

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

//Inicio Login Facebook
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(LoginResult loginResult) {


                System.out.println("onSuccess");
                ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Procesando datos...");
                progressDialog.show();
                String accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken", accessToken);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LoginActivity", response.toString());
                        //Obtener datos de fb login
                        Bundle bFacebookData = getFacebookData(object);


                        correoF = bFacebookData.getString("email");
                        urlF = bFacebookData.getString("profile_pic");
                        nameF = bFacebookData.getString("first_name") + " " + bFacebookData.getString("last_name");

                        Log.v("lograremos", nameF);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        //optLog = 1;
                        //intent.putExtra("optlog", optLog);
                        prefs = getSharedPreferences("SP",Context.MODE_PRIVATE);
                        editor = prefs.edit();
                        editor.putInt("optlog",1);
                        editor.commit();
                        intent.putExtra("correo", correoR);
                        intent.putExtra("password", contrasenaR);
                        intent.putExtra("correoF", correoF);
                        intent.putExtra("urlF", urlF);
                        intent.putExtra("nameF", nameF);

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {

                Toast.makeText(getApplicationContext(), "Login Cancelado", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Ocurrio un error al ingresar!", Toast.LENGTH_SHORT).show();

            }
        });
    }
//Fin Facebook Login

    //Funcion signIn para iniciar en google
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    public void iniciar(View view) {

        if (TextUtils.isEmpty(eCorreo.getText().toString()) || TextUtils.isEmpty(eContrasena.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Compruebe Los Campos", Toast.LENGTH_SHORT).show();
        } else {
            if (correoR.equals(eCorreo.getText().toString()) && contrasenaR.equals(eContrasena.getText().toString())) {
                prefs = getSharedPreferences("SP",Context.MODE_PRIVATE);
                editor = prefs.edit();
                //optLog = 3;
                //almacenamos el valor de optLog
                editor.putInt("optlog",3);
                editor.commit();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("correo", correoR);
                intent.putExtra("password", contrasenaR);
                //intent.putExtra("optlog",optLog);
                startActivity(intent);
                finish();
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

        } else if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        } else {//login con facebook
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("google", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount account = result.getSignInAccount();
            //optLog = 2;
            nameG = account.getDisplayName();
            correoG = account.getEmail();
            urlG = account.getPhotoUrl().toString();

            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            prefs = getSharedPreferences("SP",Context.MODE_PRIVATE);
            editor = prefs.edit();
            //almacenamos el valor de optLog
            editor.putInt("optlog",2);
            //optLog = 2;
            //intent.putExtra("optlog",optLog);
            intent.putExtra("correo",correoR);
            intent.putExtra("password",contrasenaR);
            intent.putExtra("nameG",nameG);
            intent.putExtra("correoG",correoG);
            intent.putExtra("urlG",urlG);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(getApplicationContext(), "Login Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void registrese(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent, 1234);
    }
//Funcion para obtener los datos con facebook
    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=200");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            return bundle;
        } catch (JSONException e) {
            Log.d("logrado", "Error parsing JSON");
        }
        return null;
    }


}



