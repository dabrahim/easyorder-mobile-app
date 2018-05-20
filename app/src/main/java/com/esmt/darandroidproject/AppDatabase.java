package com.esmt.darandroidproject;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Produit.class, Categorie.class, Utilisateur.class, Panier.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProduitDao produitDao();
    public abstract CategorieDao categorieDao();
    public abstract UserDao userDao();
    public abstract PanierDao panierDao();

    private static AppDatabase mInstance;

    public static AppDatabase getInstance(Context context) {
        if (mInstance == null) {
            mInstance =  Room.databaseBuilder(context, AppDatabase.class, "easyorder")
                    /* .addMigrations(new Migration(1, 2) {
                        @Override
                        public void migrate(SupportSQLiteDatabase database) {
                        }
                    })*/
                    .allowMainThreadQueries()
                    .build();
        }
        return mInstance;
    }
}
