package com.example.yougourta.projmob.NavDrawer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.yougourta.projmob.R;

/**
 * Created by Yougourta on 4/22/17.
 */

public class ConfirmerRdvsViewHolder extends RecyclerView.ViewHolder {

    public ConfirmerRdvsViewHolder(View itemView) {
        super(itemView);
    }

    public void initData(String nomProprio, String nomLogement, String date, String heure) {
        ((TextView)itemView.findViewById(R.id.liste_rdv_nom_prenom)).setText(nomProprio);
        ((TextView)itemView.findViewById(R.id.liste_rdv_logement)).setText(nomLogement);
        ((TextView)itemView.findViewById(R.id.liste_rdv_date)).setText(date);
        ((TextView)itemView.findViewById(R.id.liste_rdv_heure)).setText(heure);

    }
}
