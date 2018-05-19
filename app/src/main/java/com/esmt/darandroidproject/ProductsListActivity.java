package com.esmt.darandroidproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class ProductsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        Intent intent = getIntent();
        int idCategorie = intent.getIntExtra("idCategorie", 0);

        AppDatabase db = NavigationActivity.getDbInstance(getApplicationContext());
        List<Produit> produits = db.produitDao().getAllProducts();
        Toast.makeText(this, "Id received: " + produits.toString(), Toast.LENGTH_SHORT).show();
    }
}
