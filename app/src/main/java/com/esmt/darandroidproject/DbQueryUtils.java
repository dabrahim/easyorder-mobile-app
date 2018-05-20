package com.esmt.darandroidproject;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DbQueryUtils {
    private DbQueryUtils () {
    }

    public static List<Produit> getAllProducts (Context context)  {
        AppDatabase db = AppDatabase.getInstance(context);
        return db.produitDao().getAllProducts();
    }

    public static Produit findProductById (Context context, int idProduit) {
        AppDatabase db = AppDatabase.getInstance(context);
        return db.produitDao().findById(idProduit);
    }

    public static void addProductToCart(Context context, Produit produit, int quantite) {
        AppDatabase db = AppDatabase.getInstance(context);

        Panier panier = findCartByProductId(context, produit);
        if( panier != null ) {
            panier.quantite = quantite;
            db.panierDao().update( panier );
            return;
        }

        panier = new Panier();
        panier.idProduit = produit.getId();
        panier.quantite = quantite;
        db.panierDao().add( panier );

        Toast.makeText(context, "Le panier a été mis à jour avec succès", Toast.LENGTH_SHORT).show();
    }

    public static Panier findCartByProductId (Context context, Produit produit) {
        AppDatabase db = AppDatabase.getInstance(context);
        return db.panierDao().findByProductId(produit.getId());
    }

    public static List<Produit> getCartProducts (Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        int idProduits [] = db.panierDao().getIdProduitsDansPanier();
        return db.produitDao().findProducts(idProduits);
    }

    public static List<Panier> getAllPaniers (Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        return db.panierDao().getAll();
    }
}
