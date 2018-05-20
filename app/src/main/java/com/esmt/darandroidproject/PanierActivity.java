package com.esmt.darandroidproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanierActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panier);

        AppDatabase db = AppDatabase.getInstance(this);
        //Toast.makeText(this, DbQueryUtils.getCartProducts(this).toString(), Toast.LENGTH_SHORT).show();

        //List<Produit> produits = DbQueryUtils.getCartProducts(this);
        List<Panier> panierList = DbQueryUtils.getAllPaniers(this);
        int loo = 0;
        for(Panier panier : panierList) {
            int prix = db.produitDao().getPrixProduit(panier.idProduit);
            loo += panier.quantite * prix;
        }
        final int montant = loo;

        String data = "[";
        for (Panier panier : panierList) {
            data+= "{\"idProduit\":"+panier.idProduit+",\"quantite\":"+panier.quantite+"}";
            data+= ",";
        }
        data = data.substring(0, data.length() - 1);
        data += "]";

        TextView det = findViewById(R.id.cmd);
        det.setText("Montant total du panier : " + montant + "Francs");

        final String toshare  = data;

        //Toast.makeText(this, data, Toast.LENGTH_LONG).show();

        final Utilisateur user = db.userDao().findLastUser();

        Button btnPasserCommande = findViewById(R.id.btn_passer_commande);
        btnPasserCommande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> params = new HashMap<>();
                params.put("idUser", String.valueOf(user.getIdUser()));
                params.put("data", toshare);
                params.put("idFournisseur", String.valueOf(getIdFournisseur()));
                params.put("montant", String.valueOf(montant));

                PasserCommandeTask task = new PasserCommandeTask();
                task.execute(params);
            }
        });
    }

    private int getIdFournisseur () {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
        int idFournisseur = preferences.getInt(getString(R.string.fournisseur_id), 0);
        return idFournisseur;
    }

    class PasserCommandeTask extends AsyncTask<Map<String, String>, Void, String> {
        @Override
        protected void onPreExecute() {
            //Toast.makeText(PanierActivity.this, "request triggered", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(Map<String, String>... params) {
            return QueryUtils.sendOrder(params[0]);
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            //Toast.makeText(PanierActivity.this, jsonResponse, Toast.LENGTH_LONG).show();
            AppDatabase db = AppDatabase.getInstance(PanierActivity.this);
            db.panierDao().deleteAll();
            Intent i = new Intent(PanierActivity.this, NavigationActivity.class);
            startActivity(i);
            finish();
            Toast.makeText(PanierActivity.this, "La commande a été passée avec succès !", Toast.LENGTH_SHORT).show();
        }
    }

}
