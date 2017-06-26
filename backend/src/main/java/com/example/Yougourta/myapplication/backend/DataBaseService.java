package com.example.Yougourta.myapplication.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yougourta on 6/25/17.
 */

public class DataBaseService {
    public static final String
            className = "com.mysql.jdbc.Driver";
    public static final  String
            chaine = "jdbc:mysql://localhost:8889/projet_db";
    public static final String username = "root";
    public static final String password = "0000";

    public Connection connecter(){
        Connection con = null;
        try
        {
            Class.forName(className);
            con = DriverManager.getConnection(chaine, username, password);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return con;
    }

    public List<Logement> getLogementList()
    {

        String query_lgt ="select * from logement, utilisateur where " +
                "logement.proprio_lgt = utilisateur.id_user";
        List<Logement> logementList = new ArrayList<Logement>();
        Connection connection = connecter();
        try
        {
            Statement st_lgt = connection.createStatement();
            ResultSet rs_lgt = st_lgt.executeQuery(query_lgt);
            while (rs_lgt.next())
            {
                Logement lgt = new Logement();
                //recuperer les disponibilites pour le logement
                //String query_disponibilite ="select * from disponibilite where disponibilite.proprio_dispo = ?";

                ArrayList<Disponibilite> disponibilites = new ArrayList<Disponibilite>();
                PreparedStatement st_disponibilites = connection.prepareStatement("select * from disponibilite where disponibilite.proprio_dispo = ?");
                st_disponibilites.setString(1, rs_lgt.getString("proprio_lgt"));
                ResultSet rs_disponibilites = st_disponibilites.executeQuery();
                while (rs_disponibilites.next())
                {
                    Disponibilite disponibilite = new Disponibilite();
                    disponibilite.setJourDispo(rs_disponibilites.getString("jour_dispo"));
                    disponibilite.setHeureDebutDispo(rs_disponibilites.getString("heure_debut_dispo"));
                    disponibilite.setHeureFinDispo(rs_disponibilites.getString("heure_fin_dispo"));
                    disponibilites.add(disponibilite);
                }

                //recuperer les disponibilites pour le logement
                //String query_commentaire ="select * from commentaire where commentaire.logement_comment = ?";

                ArrayList<Commentaire> commentaires = new ArrayList<Commentaire>();

                PreparedStatement st_commentaires = connection.prepareStatement("select * from commentaire where commentaire.logement_comment = ?");
                st_commentaires.setLong(1, rs_lgt.getLong("id_lgt"));
                ResultSet rs_commentaires = st_commentaires.executeQuery();
                while (rs_commentaires.next())
                {
                    Commentaire commentaire = new Commentaire();
                    commentaire.setContentu(rs_commentaires.getString("contenu_comment"));
                    commentaire.setUtilisateur(getUserComment(rs_commentaires.getLong("posteur_comment")));
                    commentaires.add(commentaire);
                }

                lgt.setIdLogement(rs_lgt.getLong("id_lgt"));
                lgt.setJoursVisiteLogement(disponibilites);
                lgt.setCommentairesLogement(commentaires);
                lgt.setTitreLogement(rs_lgt.getString("titre_lgt"));
                lgt.setPrixLogement(rs_lgt.getString("prix_lgt"));
                lgt.setTypeLogement(rs_lgt.getString("type_lgt"));
                lgt.setSurfaceLogement(rs_lgt.getString("surf_lgt"));
                lgt.setNb_chambreLogement(rs_lgt.getString("chbr_lgt"));
                lgt.setAdrLogement(rs_lgt.getString("adr_lgt"));
                lgt.setDetailLogement(rs_lgt.getString("desc_lgt"));
                lgt.setLatitude(rs_lgt.getFloat("latitude_lgt"));
                lgt.setLongetude(rs_lgt.getFloat("longitude_lgt"));
                lgt.setNoteLogement(rs_lgt.getString("note_actuelle_lgt"));
                lgt.setMainImg(rs_lgt.getString("img_lgt"));

                logementList.add(lgt);
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (connection != null)
        {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return logementList;
    }

    public Utilisateur getUserComment(long id_comment)
    {
        Utilisateur user = new Utilisateur();
        String query="select * from utilisateur where id_user = "+id_comment;
        Connection connection = connecter();
        try
        {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next())
            {
                user.setIdUser(rs.getString("id_user"));
                user.setMdpUser(rs.getString("mdp_user"));
                user.setAdrUser(rs.getString("adr_user"));
                user.setEmailUser(rs.getString("email_user"));
                user.setTelUser(rs.getString("tel_user"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (connection != null)
        {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    /** Sissou 3zizou **/

    public boolean  insertComment(Commentaire comment)
    {

        String query="insert into commentaire(contenu_comment, logement_comment, posteur_comment) values(?,?,?)";
        Connection connection = connecter();
        PreparedStatement prst = null;
        int i = -1;

        try
        {
            prst = connection.prepareStatement(query);

            prst.setString(1, comment.getContentu());
            prst.setLong(2, comment.getId_lgt());
            prst.setString(3, comment.getUtilisateur().getIdUser());
            i = prst.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (connection != null)
        {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (i != -1);
    }




}
