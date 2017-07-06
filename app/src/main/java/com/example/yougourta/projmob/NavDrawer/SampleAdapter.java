package com.example.yougourta.projmob.NavDrawer;

/**
 * Created by Yougourta on 6/27/17.
 */

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yougourta.projmob.Classes.RendezVous;
import com.example.yougourta.projmob.R;
import com.google.gson.Gson;
import com.tr4android.recyclerviewslideitem.SwipeAdapter;
import com.tr4android.recyclerviewslideitem.SwipeConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SampleAdapter extends SwipeAdapter {
    ArrayList<RendezVous> rdvs;
    private Context mContext;
    private RecyclerView mRecyclerView;
    RendezVous rdv1;

    public SampleAdapter(Context context, RecyclerView recyclerView, ArrayList<RendezVous> rdvs) {
        mContext = context;
        mRecyclerView = recyclerView;
        this.rdvs = rdvs;
    }

    public class SampleViewHolder extends RecyclerView.ViewHolder {
        TextView nomVisisteur;
        TextView nomLogement;
        TextView date;
        TextView heure;

        public SampleViewHolder(View view) {
            super(view);
            nomVisisteur = (TextView) view.findViewById(R.id.liste_rdv_nom_prenom);
            nomLogement = (TextView) view.findViewById(R.id.liste_rdv_logement);
            date = (TextView) view.findViewById(R.id.liste_rdv_date);
            heure = (TextView) view.findViewById(R.id.liste_rdv_heure);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateSwipeViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.liste_rdv_single_row, parent, true);
        return new SampleViewHolder(v);
    }

    @Override
    public void onBindSwipeViewHolder(RecyclerView.ViewHolder swipeViewHolder, int i) {
        SampleViewHolder sampleViewHolder = (SampleViewHolder) swipeViewHolder;
        sampleViewHolder.nomVisisteur.setText(rdvs.get(i).getVisiteurRDV().getIdUser());
        sampleViewHolder.nomLogement.setText(rdvs.get(i).getLogementRDV().getTitreLogement()+" "+rdvs.get(i).getLogementRDV().getTypeLogement()+", "+rdvs.get(i).getLogementRDV().getAdrLogement()+" à "+rdvs.get(i).getLogementRDV().getPrixLogement()+" DA");
        sampleViewHolder.date.setText(rdvs.get(i).getDateRDV());
        sampleViewHolder.heure.setText(rdvs.get(i).getHeureRDV());
    }

    @Override
    public SwipeConfiguration onCreateSwipeConfiguration(Context context, int position) {
        return new SwipeConfiguration.Builder(context)
                .setLeftBackgroundColorResource(R.color.color_delete)
                .setRightBackgroundColorResource(R.color.color_mark)
                .setDrawableResource(R.drawable.ic_delete_white_24dp)
                .setRightDrawableResource(R.drawable.ic_done_white_24dp)
                .setLeftUndoable(true)
                .setRightUndoable(true)
                .setLeftUndoButtonText("Annuler")
                .setDescriptionTextColorResource(android.R.color.white)
                .setLeftSwipeBehaviour(SwipeConfiguration.SwipeBehaviour.NORMAL_SWIPE)
                .setRightSwipeBehaviour(SwipeConfiguration.SwipeBehaviour.NORMAL_SWIPE)
                .build();
    }

    @Override
    public void onSwipe(final int position, int direction) {
        if (direction == SWIPE_LEFT) {
            rdv1 = rdvs.get(position);
            rdv1.setEtatRDV(2);
            Log.d("----------------------", String.valueOf(rdvs.size()));
            String url="http://192.168.43.76:8080/updaterdv";
            RequestQueue queue = Volley.newRequestQueue(mContext);
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(mContext, volleyError.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("rdv", new Gson().toJson(rdv1));
                    return map;
                }
            };

            queue.add(request);
            rdvs.remove(position);
            notifyItemRemoved(position);
        }
        else
        {
            rdv1 = rdvs.get(position);
            rdv1.setEtatRDV(1);
            Log.d("----------------------", String.valueOf(rdvs.size()));
            String url="http://192.168.43.76:8080/updaterdv";
            RequestQueue queue = Volley.newRequestQueue(mContext);
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(mContext, volleyError.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("rdv", new Gson().toJson(rdv1));
                    return map;
                }
            };

            queue.add(request);
            rdvs.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public int getItemCount() {
        return rdvs.size();
    }
}
