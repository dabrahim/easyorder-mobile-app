package com.esmt.darandroidproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ProductsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        Intent intent = getIntent();
        int idCategorie = intent.getIntExtra("idCategorie", 0);
        Toast.makeText(this, "Id received: " + idCategorie, Toast.LENGTH_SHORT).show();
    }
}
