package com.example.Yougourta.myapplication.backend;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Nadji AZRI on 27/06/2017.
 */

public class GetUtilisateurServelet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Utilisateur> utilisateurList = new DataBaseService().getUser(req.getParameter("email"),req.getParameter("mdp"));
        Gson gson = new Gson();
        resp.getWriter().print(gson.toJson(utilisateurList));
    }
}
