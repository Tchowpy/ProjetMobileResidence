package com.example.yougourta.projmob.Classes;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Nadji AZRI on 27/06/2017.
 */

public class ConnexionManager {
    private Context context;

    public ConnexionManager(Context context) {
        this.context = context;
    }

    public boolean saveConnectedUsser(Utilisateur utilisateur){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("connected",true);
        editor.putString("email",utilisateur.getEmailUser());
        editor.putString("image",utilisateur.getImageUser());
        editor.putString("usr_id", utilisateur.getIdUser());
        return editor.commit();
    }

    public boolean isUserConnected(){

        SharedPreferences sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("connected",false);
    }

    public String getEmailConnected(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        return sharedPreferences.getString("email",null);
    }

    public String getImageConnected(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        return sharedPreferences.getString("image",null);
    }

    public String getIdConnected(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        return sharedPreferences.getString("usr_id",null);
    }

    public  boolean deconnexion(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("connected",false);

        return editor.commit();
    }
}
