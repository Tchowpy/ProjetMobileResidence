package com.example.yougourta.projmob.Classes;

import java.io.Serializable;

/**
 * Created by Yougourta on 3/28/17.
 */

public class Commentaire implements Serializable{
    private Utilisateur utilisateur;
    private String contentu;
    private long id_lgt;

    public Commentaire(Utilisateur utilisateur, String contentu, long id_lgt) {
        this.utilisateur = utilisateur;
        this.contentu = contentu;
        this.id_lgt = id_lgt;
    }

    public long getId_lgt() {
        return id_lgt;
    }

    public void setId_lgt(long id_lgt) {
        this.id_lgt = id_lgt;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getContentu() {
        return contentu;
    }

    public void setContentu(String contentu) {
        this.contentu = contentu;
    }
}
