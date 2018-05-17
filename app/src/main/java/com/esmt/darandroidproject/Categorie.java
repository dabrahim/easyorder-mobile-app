package com.esmt.darandroidproject;

import java.util.ArrayList;
import java.util.List;

public class Categorie {
    private int id;
    private String nom;
    private List<Produit> produits = new ArrayList<>();

    public List<Produit> getProduits() {
        return produits;
    }

    public Categorie (){
    }

    public Categorie (int id){
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
            return  "DEFAULT TEMPORARY NAME";
        }
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


}
