package com.esmt.darandroidproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeClientActivity extends AppCompatActivity {
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_client);

        logoutButton = (Button) findViewById(R.id.btn_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(getString(R.string.jwt));
                editor.commit();
                Toast.makeText(HomeClientActivity.this, "Déconnexion réussie", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(HomeClientActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
