package com.esmt.darandroidproject;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
interface PanierDao {
    @Insert
    public void add(Panier... panier);

    @Query("SELECT * FROM paniers")
    public List<Panier> getAll();

    @Update
    public void update(Panier... paniers);

    @Query("SELECT * FROM paniers WHERE id_produit = :idProduit")
    public Panier findByProductId(int idProduit);

    @Query("DELETE FROM paniers")
    public void deleteAll();

    @Query("SELECT id_produit FROM paniers")
    public int[] getIdProduitsDansPanier();
}

@Entity(tableName = "paniers")
public class Panier {
    @PrimaryKey @ColumnInfo(name = "id_produit")
    public int idProduit;
    public int quantite;
}
