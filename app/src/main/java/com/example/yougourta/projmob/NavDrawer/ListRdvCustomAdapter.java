package com.example.yougourta.projmob.NavDrawer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yougourta.projmob.Classes.Logement;
import com.example.yougourta.projmob.Classes.MesRdvListeSingleRow;
import com.example.yougourta.projmob.R;

import java.util.ArrayList;

/**
 * Created by Nadji AZRI on 16/04/2017.
 */


class ListRdvCustomAdapter extends BaseAdapter {

    ArrayList<MesRdvListeSingleRow> list = null;
    private Context context;

    ListRdvCustomAdapter(ArrayList<MesRdvListeSingleRow> listParam,Context context){

        list = listParam;
        this.context = context;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = parent.inflate(context,R.layout.liste_rdv_single_row,null);

        TextView nom = (TextView) convertView.findViewById(R.id.liste_rdv_nom_prenom);

        TextView date = (TextView)convertView.findViewById(R.id.liste_rdv_date);
        TextView heure = (TextView)convertView.findViewById(R.id.liste_rdv_heure);
        ImageButton confirmer = (ImageButton)convertView.findViewById(R.id.imageButton4);
        confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ImageButton decliner = (ImageButton)convertView.findViewById(R.id.imageButton);
        decliner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        nom.setText(list.get(position).getNom());
        date.setText(list.get(position).getDate());
        heure.setText(list.get(position).getHeure());

        return convertView;
    }
}
