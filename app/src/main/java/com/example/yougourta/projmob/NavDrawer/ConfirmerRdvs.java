package com.example.yougourta.projmob.NavDrawer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.yougourta.projmob.Classes.ConnexionManager;
import com.example.yougourta.projmob.Classes.MesRdvListeSingleRow;
import com.example.yougourta.projmob.Classes.RendezVous;
import com.example.yougourta.projmob.MainActivity;
import com.example.yougourta.projmob.R;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.huannguyen.swipeablerv.SWItemRemovalListener;
import io.huannguyen.swipeablerv.view.SWRecyclerView;

public class ConfirmerRdvs extends AppCompatActivity {

    public static SWRecyclerView mRecyclerView;
    com.example.yougourta.projmob.NavDrawer.ConfirmerRdvsAdapter mAdapter;
    //ArrayList<MesRdvListeSingleRow> listRdv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmer_rdvs);

        ConnexionManager connexionManager = new ConnexionManager(this);
        String url="http://192.168.43.76:8080/getrdvs?usr="+connexionManager.getIdConnected();

        //Intent intent = getIntent();
        //listRdv = (ArrayList<MesRdvListeSingleRow>) intent.getSerializableExtra("list");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_dyalna);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Rendez-Vous");

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Gson gson = new Gson();
                ArrayList<RendezVous> rdvs = new ArrayList(Arrays.asList(gson.fromJson(jsonArray.toString(), RendezVous[].class)));

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                //recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                recyclerView.setAdapter(new SampleAdapter(getApplicationContext(), recyclerView, rdvs));


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ConfirmerRdvs.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }
}
