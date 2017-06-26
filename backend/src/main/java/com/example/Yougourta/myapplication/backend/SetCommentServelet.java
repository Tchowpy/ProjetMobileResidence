package com.example.Yougourta.myapplication.backend;

import com.google.gson.Gson;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Yougourta on 6/26/17.
 */

public class SetCommentServelet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String commentJson = req.getParameter("commentaire");
        Commentaire commentaire = new Gson().fromJson(commentJson, Commentaire.class);
        Boolean inserted = new DataBaseService().insertComment(commentaire);
        resp.getWriter().print(inserted);
    }
}
