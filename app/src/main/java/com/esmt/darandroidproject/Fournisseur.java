package com.esmt.darandroidproject;

import java.util.List;

public class Fournisseur extends Utilisateur {
    private String nomSociete;
    private String nomImageProfil;
    private List<Fournisseur> fournisseurs;

    public List<Fournisseur> getFournisseurs() {
        return fournisseurs;
    }

    public void addFournisseur(Fournisseur fournisseur){
        this.fournisseurs.add(fournisseur);
    }

    public void setFournisseurs(List<Fournisseur> fournisseurs) {
        this.fournisseurs = fournisseurs;
    }

    public String getNomSociete() {
        return nomSociete;
    }

    public void setNomSociete(String nomSociete) {
        this.nomSociete = nomSociete;
    }

    public String getNomImageProfil() {
        return nomImageProfil;
    }

    public void setNomImageProfil(String nomImageProfil) {
        this.nomImageProfil = nomImageProfil;
    }

    public Fournisseur (){}
    public Fournisseur (int idFournisseur){
        super.setIdUser(idFournisseur);
    }
}
