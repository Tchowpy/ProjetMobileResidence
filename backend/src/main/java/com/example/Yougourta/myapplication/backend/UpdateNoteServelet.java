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

public class UpdateNoteServelet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String lgtJson = req.getParameter("lgt");
        Logement lgt = new Gson().fromJson(lgtJson, Logement.class);
        Boolean updated = new DataBaseService().updateNote(lgt.getIdLogement(), lgt.getNoteLogement(), lgt.getnbnotesLogement());
        resp.getWriter().print(updated);
    }
}
