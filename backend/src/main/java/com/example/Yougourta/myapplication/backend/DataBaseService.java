package com.example.Yougourta.myapplication.backend;

import java.sql.Array;
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

                //recuprer l'utilisateur qui a poste le commentaire
                ArrayList<Commentaire> commentaires = new ArrayList<Commentaire>();
                PreparedStatement st_commentaires = connection.prepareStatement("select * from commentaire where commentaire.logement_comment = ?");
                st_commentaires.setLong(1, rs_lgt.getLong("id_lgt"));
                ResultSet rs_commentaires = st_commentaires.executeQuery();
                while (rs_commentaires.next())
                {
                    Commentaire commentaire = new Commentaire();
                    commentaire.setContentu(rs_commentaires.getString("contenu_comment"));
                    commentaire.setUtilisateur(getUserLgt(rs_commentaires.getString("posteur_comment")));
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
                lgt.setNoteLogement(rs_lgt.getFloat("note_actuelle_lgt"));
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

    public boolean  insertRdv(RendezVous rdv)
    {

        String query="insert into rendezvous(lgt_rdv, client_rdv, date_rdv, heure_rdv, etat_rdv) values(?,?,?,?,?)";
        Connection connection = connecter();
        PreparedStatement prst = null;
        int i = -1;

        try
        {
            prst = connection.prepareStatement(query);

            prst.setLong(1, rdv.getLogementRDV().getIdLogement());
            prst.setString(2, rdv.getVisiteurRDV().getIdUser());
            prst.setString(3, rdv.getDateRDV());
            prst.setString(4, rdv.getHeureRDV());
            prst.setInt(5, rdv.getEtatRDV());

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

    public ArrayList<RendezVous> getRdvs(String user_id)
    {

        Connection connection = connecter();
        ArrayList<RendezVous> rdvs = new ArrayList<RendezVous>();
        try
        {
            PreparedStatement st = connection.prepareStatement("select * from rendezvous join logement on rendezvous.lgt_rdv = logement.id_lgt where rendezvous.etat_rdv = 0 and logement.proprio_lgt = ?");
            st.setString(1, user_id);
            ResultSet rs = st.executeQuery();
            while (rs.next())
            {
                RendezVous rdv = new RendezVous();
                Logement logement = getLogement(rs.getLong("lgt_rdv"));
                Utilisateur visiteur = getUserLgt(rs.getString("client_rdv"));

                rdv.setIdRDV(rs.getInt("id_rdv"));
                rdv.setVisiteurRDV(visiteur);
                rdv.setLogementRDV(logement);
                rdv.setEtatRDV(rs.getInt("etat_rdv"));
                rdv.setDateRDV(rs.getString("date_rdv"));
                rdv.setHeureRDV(rs.getString("heure_rdv"));
                rdvs.add(rdv);
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
        return rdvs;
    }

    public Logement getLogement(Long id_logement)
    {

        Connection connection = connecter();
        Logement lgt = new Logement();
        try
        {
            PreparedStatement st_lgt = connection.prepareStatement("select * from logement where id_lgt = ?");
            st_lgt.setLong(1, id_logement);
            ResultSet rs_lgt = st_lgt.executeQuery();

            while (rs_lgt.next())
            {
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

                //recuprer l'utilisateur qui a poste le commentaire
                ArrayList<Commentaire> commentaires = new ArrayList<Commentaire>();
                PreparedStatement st_commentaires = connection.prepareStatement("select * from commentaire where commentaire.logement_comment = ?");
                st_commentaires.setLong(1, rs_lgt.getLong("id_lgt"));
                ResultSet rs_commentaires = st_commentaires.executeQuery();
                while (rs_commentaires.next())
                {
                    Commentaire commentaire = new Commentaire();
                    commentaire.setContentu(rs_commentaires.getString("contenu_comment"));
                    commentaire.setUtilisateur(getUserLgt(rs_commentaires.getString("posteur_comment")));
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
                lgt.setNoteLogement(rs_lgt.getFloat("note_actuelle_lgt"));
                lgt.setnbnotesLogement(rs_lgt.getInt("nb_notes_lgt"));
                lgt.setMainImg(rs_lgt.getString("img_lgt"));
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
        return lgt;
    }

    public Utilisateur getUserLgt(String user_id)
    {
        Connection connection = connecter();
        Utilisateur user = new Utilisateur();
        try
        {
            PreparedStatement st = connection.prepareStatement("select * from utilisateur where id_user = ?");
            st.setString(1, user_id);
            ResultSet rs = st.executeQuery();
            while (rs.next())
            {
                user.setIdUser(rs.getString("id_user"));
                user.setMdpUser(rs.getString("mdp_user"));
                user.setTelUser(rs.getString("tel_user"));
                user.setEmailUser(rs.getString("email_user"));
                user.setAdrUser(rs.getString("adr_user"));
                user.setImageUser(rs.getString("url_image_user"));
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

    public boolean updateRdv(int rdv_id, int etat)
    {

        String query="update rendezvous set etat_rdv = ? where id_rdv = ?";
        Connection connection = connecter();
        PreparedStatement prst = null;
        int i = -1;

        try
        {
            prst = connection.prepareStatement(query);
            prst.setInt(1, etat);
            prst.setInt(2, rdv_id);

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

    public ArrayList<RendezVous> getRdvsattente(String user_id)
    {

        Connection connection = connecter();
        ArrayList<RendezVous> rdvs = new ArrayList<RendezVous>();
        try
        {
            PreparedStatement st = connection.prepareStatement("select * from rendezvous join logement on rendezvous.lgt_rdv = logement.id_lgt where rendezvous.etat_rdv = 1 and logement.proprio_lgt = ?");
            st.setString(1, user_id);
            ResultSet rs = st.executeQuery();
            while (rs.next())
            {
                RendezVous rdv = new RendezVous();
                Logement logement = getLogement(rs.getLong("lgt_rdv"));
                Utilisateur visiteur = getUserLgt(rs.getString("client_rdv"));

                rdv.setIdRDV(rs.getInt("id_rdv"));
                rdv.setVisiteurRDV(visiteur);
                rdv.setLogementRDV(logement);
                rdv.setEtatRDV(rs.getInt("etat_rdv"));
                rdv.setDateRDV(rs.getString("date_rdv"));
                rdv.setHeureRDV(rs.getString("heure_rdv"));
                rdvs.add(rdv);
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
        return rdvs;
    }

    public boolean updateNote(long lgt_id, float note, int nb_note)
    {

        String query="update logement set note_actuelle_lgt = ?, nb_notes_lgt = ? where id_lgt = ?";
        Connection connection = connecter();
        PreparedStatement prst = null;
        int i = -1;

        try
        {
            prst = connection.prepareStatement(query);
            prst.setFloat(1, note);
            prst.setInt(2, nb_note);
            prst.setLong(3, lgt_id);

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

    public List<Utilisateur> getUser(String email, String mdp)
    {

        String query_lgt ="select * from utilisateur where email_user=? and mdp_user=?";
        List<Utilisateur> utilisateurList = new ArrayList<Utilisateur>();
        Connection connection = connecter();
        PreparedStatement prst = null;
        try
        {


            prst = connection.prepareStatement(query_lgt);

            prst.setString(1, email);
            prst.setString(2, mdp);

            ResultSet rs_lgt = prst.executeQuery();
            if(rs_lgt == null) return  null;

            while (rs_lgt.next())
            {
                Utilisateur utilisateur = new Utilisateur();

                utilisateur.setIdUser(rs_lgt.getString("id_user"));
                utilisateur.setEmailUser(rs_lgt.getString("email_user"));
                utilisateur.setMdpUser(rs_lgt.getString("mdp_user"));
                utilisateur.setImageUser(rs_lgt.getString("url_image_user"));
                utilisateur.setAdrUser(rs_lgt.getString("adr_user"));
                utilisateur.setTelUser(rs_lgt.getString("tel_user"));

                utilisateurList.add(utilisateur);
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
        return utilisateurList;
    }
}
