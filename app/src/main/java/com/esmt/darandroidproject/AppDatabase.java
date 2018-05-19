package com.esmt.darandroidproject;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Produit.class, Categorie.class, Utilisateur.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProduitDao produitDao();
    public abstract CategorieDao categorieDao();
    public abstract UserDao userDao();
}
