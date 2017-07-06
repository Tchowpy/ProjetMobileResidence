package com.example.Yougourta.myapplication.backend;

import com.google.gson.Gson;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Yougourta on 6/27/17.
 */

public class SetRendezvousServelet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String rdvJson = req.getParameter("rdv");
        RendezVous rdv = new Gson().fromJson(rdvJson, RendezVous.class);
        Boolean inserted = new DataBaseService().insertRdv(rdv);
        resp.getWriter().print(inserted);
    }
}
