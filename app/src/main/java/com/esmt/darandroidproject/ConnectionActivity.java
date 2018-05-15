package com.esmt.darandroidproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConnectionActivity extends AppCompatActivity {
    private EditText txtEmail, txtPassword;
    private String email, password;
    private Button btnConnection /*, btnShowToken */;
    private ProgressDialog progressDialog;

    private static final String LOG_TAG = ConnectionActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Vérification des informations de connexion en cours ...");

        txtEmail = (EditText) findViewById(R.id.txt_email);
        txtPassword = (EditText) findViewById(R.id.txt_password);
        btnConnection = (Button) findViewById(R.id.btn_connexion);

        btnConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = txtEmail.getText().toString();
                password = txtPassword.getText().toString();

                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);

                ConnexionTask task = new ConnexionTask();
                task.execute( params );
            }
        });

/*        btnShowToken = (Button) findViewById(R.id.btn_show_token);
        btnShowToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_name) ,Context.MODE_PRIVATE);
                String token = preferences.getString( getString(R.string.jwt) , "No token");
                Toast.makeText(ConnectionActivity.this, token, Toast.LENGTH_LONG).show();
            }
        });*/
    }

    private class ConnexionTask extends AsyncTask<Map<String, String>, Void, String> {
        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Map<String, String>... params) {
            String jsonResponse = QueryUtils.connectUser( params[0] );
            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String response) {
            progressDialog.hide();
            handleConnectionResponse( response );
//            Toast.makeText(ConnectionActivity.this, response, Toast.LENGTH_LONG).show();
        }
    }

    private void handleConnectionResponse(String jsonResponse) {
        try{
            JSONObject json = new JSONObject(jsonResponse);
            boolean result = json.getBoolean("success");
            if (result) {
                String jwt = json.getString("token");
                SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(getString(R.string.jwt), jwt);
                editor.commit();

                Intent i = new Intent(ConnectionActivity.this, HomeClientActivity.class);
                startActivity(i);
                finish();

            } else {
                Toast.makeText(this, "E-mail ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing JSON response");
        }
    }
}
