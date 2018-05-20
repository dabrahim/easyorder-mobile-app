package com.esmt.darandroidproject;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
interface ProduitDao {
    @Insert
    public void createProducts(List<Produit> produits);

    @Query("SELECT * FROM produits")
    public List<Produit> getAllProducts();

    @Query("DELETE FROM produits")
    public void deleteAllProducts();

    @Query("SELECT * FROM produits WHERE id_produit = :idProduit")
    public Produit findById(int idProduit);

    @Query("SELECT * FROM produits WHERE id_produit IN (:idProduits)")
    public List<Produit> findProducts(int ... idProduits);

    @Query("SELECT prix FROM produits WHERE id_produit = :idProduit")
    public int getPrixProduit(int idProduit);


}

@Entity(tableName = "produits")
public class Produit {
    @PrimaryKey @ColumnInfo(name = "id_produit")
    private int id;
    private String titre;
    private String description;
    private int prix;
    @ColumnInfo(name = "nom_fichier")
    private String nomFichier;
    @Ignore
    private Fournisseur fournisseur;
    @Ignore
    private Categorie categorie;
    private int id_fournisseur;
    private  int id_categorie;

    public int getId_categorie() {
        return id_categorie;
    }

    public void setId_categorie(int id_categorie) {
        this.id_categorie = id_categorie;
        this.categorie = new Categorie(id_categorie);
    }

    public void setId_fournisseur(int id_fournisseur) {
        this.id_fournisseur = id_fournisseur;
        this.fournisseur = new Fournisseur(id_fournisseur);
    }

    public int getId_fournisseur() {
        return id_fournisseur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
        this.id_fournisseur = fournisseur.getIdUser();
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
        this.id_categorie = categorie.getId();
    }
}
