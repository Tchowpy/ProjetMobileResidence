package com.example.yougourta.projmob.NavDrawer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.yougourta.projmob.Classes.MesRdvEnAttenteSingleRow;
import com.example.yougourta.projmob.Classes.MesRdvListeSingleRow;
import com.example.yougourta.projmob.R;

import java.util.ArrayList;

public class MesRdvEnAttenteActivity extends AppCompatActivity {

    private ListView listView=null;
    private ArrayList<MesRdvEnAttenteSingleRow> listRdvEnAttente=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_rdv_en_attente);

        Intent intent = getIntent();
        listRdvEnAttente = (ArrayList<MesRdvEnAttenteSingleRow>) intent.getSerializableExtra("list");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_dyalna);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("En attente");

        listView = (ListView) findViewById(R.id.mes_rdv_en_attente_liste_view);


        if(listRdvEnAttente != null) {
            ListRdvEnAttenteCustomAdapter customAdapter = new ListRdvEnAttenteCustomAdapter(listRdvEnAttente, this);
            listView.setAdapter(customAdapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        }
    }
}
