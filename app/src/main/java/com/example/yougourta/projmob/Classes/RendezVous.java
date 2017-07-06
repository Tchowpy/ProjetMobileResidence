package com.example.yougourta.projmob.Classes;

import java.io.Serializable;

/**
 * Created by Yougourta on 3/23/17.
 */

public class RendezVous implements Serializable{
    private int idRDV;
    private Logement logementRDV;
    private Utilisateur visiteurRDV;
    private String dateRDV;
    private String heureRDV;
    private int etatRDV = 0;

    public RendezVous(Logement logementRDV, Utilisateur visiteurRDV, String jourRDV, String heureDebutRDV) {
        this.logementRDV = logementRDV;
        this.visiteurRDV = visiteurRDV;
        this.dateRDV = jourRDV;
        this.heureRDV = heureDebutRDV;
    }

    public RendezVous(){}

    public int getIdRDV() {
        return idRDV;
    }

    public void setIdRDV(int idRDV) {
        this.idRDV = idRDV;
    }

    public int getEtatRDV() {
        return etatRDV;
    }

    public void setEtatRDV(int etatRDV) {
        this.etatRDV = etatRDV;
    }

    public Logement getLogementRDV() {
        return logementRDV;
    }

    public void setLogementRDV(Logement logementRDV) {
        this.logementRDV = logementRDV;
    }

    public Utilisateur getVisiteurRDV() {
        return visiteurRDV;
    }

    public void setVisiteurRDV(Utilisateur visiteurRDV) {
        this.visiteurRDV = visiteurRDV;
    }

    public String getDateRDV() {
        return dateRDV;
    }

    public void setDateRDV(String jourRDV) {
        this.dateRDV = jourRDV;
    }

    public String getHeureRDV() {
        return heureRDV;
    }

    public void setHeureRDV(String heureDebutRDV) {
        this.heureRDV = heureDebutRDV;
    }

}
