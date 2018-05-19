package com.esmt.darandroidproject;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
interface CategorieDao {
    @Insert
    public void create(List<Categorie> categories);

    @Query("SELECT * FROM categories")
    public  List<Categorie> getAllCategories();

    @Query("DELETE FROM categories")
    public  void deleteAll();

    @Query("SELECT * FROM categories WHERE id_categorie = :idCategorie")
    public Categorie findById(int idCategorie);
}

@Entity(tableName = "categories")
public class Categorie {
    @PrimaryKey @ColumnInfo(name = "id_categorie")
    private int id;
    private String nom;
    @Ignore
    private List<Produit> produits = new ArrayList<>();

    public List<Produit> getProduits() {
        return produits;
    }

    public Categorie() {
    }

    @Ignore
    public Categorie(int id) {
        this.id = id;
    }

    public void addProduit(Produit produit) {
        this.produits.add(produit);
    }

    public void setProduits(List<Produit> produits) {
        this.produits = produits;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        if (nom != null) {
            return nom;
        } else {
            return "DEFAULT TEMPORARY NAME";
        }
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
