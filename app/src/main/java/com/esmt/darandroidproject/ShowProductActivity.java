package com.esmt.darandroidproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ShowProductActivity extends AppCompatActivity {
    private int quantite = 1;
    private int prix;
    private TextView txtQuantite, txtTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        txtQuantite = findViewById(R.id.txt_quantite);
        txtTotal = findViewById(R.id.txt_prix_total);

        Intent i = getIntent();
        int idProduit = i.getIntExtra("idProduit", 0);
        final Produit produit = DbQueryUtils.findProductById(getApplicationContext(),idProduit);
        toolbar.setTitle(produit.getTitre());

        ImageView img = findViewById(R.id.img_product);
        Picasso.get()
                .load("http://"+QueryUtils.IP_ADDRESS+"/easyorder/uploads/"+produit.getNomFichier())
                .into(img);

        setSupportActionBar(toolbar);
/*

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/


        TextView txtDescription = findViewById(R.id.txt_description_produit);
        txtDescription.setText(produit.getDescription());

        TextView txtPrix = findViewById(R.id.txt_prix_produit);
        txtPrix.setText("Prix: " + String.valueOf(produit.getPrix()));

        prix = produit.getPrix();

        Button addToCart = findViewById(R.id.btn_add_cart);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbQueryUtils.addProductToCart(getApplicationContext(), produit, quantite);
                Intent i = new Intent(ShowProductActivity.this, ProductsListActivity.class);
                i.putExtra("idCategorie", produit.getCategorie().getId());
                startActivity(i);
                finish();
            }
        });

        Panier panier = DbQueryUtils.findCartByProductId(this ,produit);
        if (panier != null) {
            txtQuantite.setText(String.valueOf(panier.quantite));
            txtTotal.setText(String.valueOf(panier.quantite * prix));
            quantite = panier.quantite;
            addToCart.setText("Mettre à jour le panier");
        }
    }

    public void increment(View v) {
        quantite++;
        refreshUi();
    }

    public void decrement(View v){
        if(quantite > 1) {
            quantite--;
            refreshUi();
        }
    }

    public void refreshUi () {
        txtTotal.setText(String.valueOf(quantite*prix));
        txtQuantite.setText(String.valueOf(quantite));
    }
}
