package com.example.yougourta.projmob.Classes;

import java.io.Serializable;

/**
 * Created by Nadji AZRI on 21/04/2017.
 */

public class MesRdvEnAttenteSingleRow implements Serializable {

    String nom;
    String logement;
    String date;
    String heure;

    public MesRdvEnAttenteSingleRow(String nom, String logement, String date, String heure) {
        this.nom = nom;
        this.logement = logement;
        this.date = date;
        this.heure = heure;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLogement() {
        return logement;
    }

    public void setLogement(String logement) {
        this.logement = logement;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }
}
