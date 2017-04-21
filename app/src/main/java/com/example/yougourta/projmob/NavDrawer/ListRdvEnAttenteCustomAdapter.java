package com.example.yougourta.projmob.NavDrawer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yougourta.projmob.Classes.MesRdvEnAttenteSingleRow;
import com.example.yougourta.projmob.Classes.MesRdvListeSingleRow;
import com.example.yougourta.projmob.R;

import java.util.ArrayList;

/**
 * Created by Nadji AZRI on 21/04/2017.
 */

public class ListRdvEnAttenteCustomAdapter extends BaseAdapter {

    ArrayList<MesRdvEnAttenteSingleRow> list = null;
    private Context context;

    public ListRdvEnAttenteCustomAdapter(ArrayList<MesRdvEnAttenteSingleRow> list, Context context) {
        this.list = list;
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

        convertView = parent.inflate(context,R.layout.list_rdv_en_attente_single_row,null);

        TextView nom = (TextView) convertView.findViewById(R.id.liste_rdv_en_attente_nom_prenom);
        TextView logement = (TextView) convertView.findViewById(R.id.liste_rdv_en_attente_logement);
        TextView date = (TextView)convertView.findViewById(R.id.liste_rdv_en_attente_date);
        TextView heure = (TextView)convertView.findViewById(R.id.liste_rdv_en_attente_heure);

        nom.setText(list.get(position).getNom());
        logement.setText(list.get(position).getLogement());
        date.setText(list.get(position).getDate());
        heure.setText(list.get(position).getHeure());

        return convertView;
    }
}
