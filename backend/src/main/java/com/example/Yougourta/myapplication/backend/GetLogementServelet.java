package com.example.Yougourta.myapplication.backend;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Yougourta on 6/25/17.
 */

public class GetLogementServelet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Logement> logementList = new DataBaseService().getLogementList();
        Gson gson = new Gson();
        resp.getWriter().print(gson.toJson(logementList));
    }
}
