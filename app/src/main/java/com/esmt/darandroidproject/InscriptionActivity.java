package com.esmt.darandroidproject;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class InscriptionActivity extends AppCompatActivity {
    private EditText txtEmail, txtPassword, txtPasswordConfirmation, txtNom, txtPrenom, txtTelephone;
    private String email, password, passwordConfirmation, nom, prenom, telephone;
    private Button btnInscription;
    private HashMap<String, String> params;
    private ProgressDialog progressDialog;

    private static final String LOG_TAG = InscriptionActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Inscription en cours ...");

        txtEmail = (EditText) findViewById(R.id.txt_email);
        txtPassword = (EditText) findViewById(R.id.txt_password);
        txtPasswordConfirmation = (EditText) findViewById(R.id.txt_password_confirm);
        txtNom = (EditText) findViewById(R.id.txt_nom);
        txtPrenom = (EditText) findViewById(R.id.txt_prenom);
        txtTelephone = (EditText) findViewById(R.id.txt_telephone);

        params = new HashMap<>();

        btnInscription = (Button) findViewById(R.id.btn_inscription);
        btnInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = txtEmail.getText().toString();
                password = txtPassword.getText().toString();
                passwordConfirmation = txtPasswordConfirmation.getText().toString();
                nom = txtNom.getText().toString();
                prenom = txtPrenom.getText().toString();
                telephone = txtTelephone.getText().toString();

                params.put("email", email);
                params.put("password", password);
                params.put("password_confirmation", passwordConfirmation);
                params.put("nom", nom);
                params.put("prenom", prenom);
                params.put("telephone", telephone);
                params.put("coordGeo", "SAMPLE DATA");

                //Processing request
                RegistrationTask task = new RegistrationTask();
                task.execute( params );
            }
        });

    }

    private class RegistrationTask extends AsyncTask<HashMap<String, String>, Void, String> {
        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(HashMap<String, String>... params) {
            String response = QueryUtils.registerClient( params[0] );
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            progressDialog.dismiss();
            try{
                JSONObject jsonObject = new JSONObject(response);
                boolean isSuccess = jsonObject.getBoolean("success");
                if (isSuccess) {
                    Intent i = new Intent(InscriptionActivity.this, ConnectionActivity.class);
                    startActivity(i);
                } else {
                    String message = jsonObject.getString("message");
                    Toast.makeText(InscriptionActivity.this, message, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e){
                Log.e(LOG_TAG, "Error parsing response", e);
            }
        }
    }

}
