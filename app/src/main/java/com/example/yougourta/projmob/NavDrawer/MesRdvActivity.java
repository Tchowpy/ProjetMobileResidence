package com.example.yougourta.projmob.NavDrawer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yougourta.projmob.Classes.MesRdvListeSingleRow;
import com.example.yougourta.projmob.MainActivity;
import com.example.yougourta.projmob.R;

import java.util.ArrayList;

public class MesRdvActivity extends AppCompatActivity {
    private ListView listView=null;
    private ArrayList<MesRdvListeSingleRow> listRdv=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_rdv);

        Intent intent = getIntent();
        listRdv = (ArrayList<MesRdvListeSingleRow>) intent.getSerializableExtra("list");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_dyalna);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.mes_rdv_liste_view);

        if(listRdv != null) {
            ListRdvCustomAdapter customAdapter = new ListRdvCustomAdapter(listRdv, this);
            listView.setAdapter(customAdapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        }


    }
}


