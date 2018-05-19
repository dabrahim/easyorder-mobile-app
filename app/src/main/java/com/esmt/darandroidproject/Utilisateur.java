package com.esmt.darandroidproject;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;

import java.util.List;
@Dao
interface UserDao {
    @Insert
    public void create(Utilisateur... users);

    @Query("DELETE FROM utilisateurs")
    public void deleteAll();

    @Query("SELECT * FROM utilisateurs LIMIT 1")
    public Utilisateur findLastUser();
}

@Entity(tableName = "utilisateurs")
public class Utilisateur {
    @ColumnInfo(name = "id_user") @PrimaryKey
    private int idUser;
    private String email;
    private String password;
    @ColumnInfo(name = "date_inscription")
    private String dateInscription;
    private String type = "USER";
    private String telephone;
    private int solde;

    public Utilisateur (){
    }

    public int getIdUser() {

        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(String dateInscription) {
        this.dateInscription = dateInscription;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getSolde() {
        return solde;
    }

    public void setSolde(int solde) {
        this.solde = solde;
    }
}
