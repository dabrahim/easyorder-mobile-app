package com.esmt.darandroidproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button btnConnexion, btnInscription/*, btnShowToken*/;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (getToken() != null) {
            Intent i = new Intent(MainActivity.this, NavigationActivity.class);
            startActivity(i);
            finish();
        }

        btnConnexion = (Button) findViewById(R.id.btn_connexion);
        btnInscription = (Button) findViewById(R.id.btn_inscription);

        btnInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, InscriptionActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ConnectionActivity.class);
                startActivity(i);
                finish();
            }
        });

/*        btnShowToken = (Button) findViewById(R.id.btn_show_token);
        btnShowToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_name) ,Context.MODE_PRIVATE);
                String token = preferences.getString( getString(R.string.jwt) , "No token");
                Toast.makeText(MainActivity.this, token, Toast.LENGTH_LONG).show();
            }
        });*/
    }

    private String getToken () {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_name) ,Context.MODE_PRIVATE);
        String token = preferences.getString( getString(R.string.jwt) , "");
        return (token.equals("")) ? null : token;
    }
}
