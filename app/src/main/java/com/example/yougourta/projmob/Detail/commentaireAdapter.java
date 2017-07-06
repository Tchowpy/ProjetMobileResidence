package com.example.yougourta.projmob.Detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yougourta.projmob.Classes.Commentaire;
import com.example.yougourta.projmob.R;

import java.util.ArrayList;

/**
 * Created by Yougourta on 3/28/17.
 */

public class commentaireAdapter extends BaseAdapter {

    ArrayList<Commentaire>commentaireLogement;
    Context context;

    public commentaireAdapter(ArrayList<Commentaire> commentaireLogement, Context context) {
        this.commentaireLogement = commentaireLogement;
        this.context = context;
    }

    @Override
    public int getCount() {
        return commentaireLogement.size();
    }

    @Override
    public Object getItem(int position) {
        return commentaireLogement.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commentaire_ligne, parent, false);

        ImageView imageView = (ImageView)view.findViewById(R.id.imageUserCommentaire);
        TextView user = (TextView)view.findViewById(R.id.utilisateurCommentaire);
        TextView content = (TextView)view.findViewById(R.id.contenuCommentaire);

        /**imageView.setImageResource(commentaireLogement.get(position).getUtilisateur().getImageUser()); GLID GLID**/
        //Glide.with(context).load("http://192.168.43.76:8888/MAMP/images/ProjMob/users/"+commentaireLogement.get(position).getUtilisateur().getIdUser()+"/"+commentaireLogement.get(position).getUtilisateur().getImageUser()).skipMemoryCache(false).into(imageView);
        user.setText(commentaireLogement.get(position).getUtilisateur().getIdUser());
        content.setText(commentaireLogement.get(position).getContentu());
        return view;
    }
}
