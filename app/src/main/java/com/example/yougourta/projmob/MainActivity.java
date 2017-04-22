package com.example.yougourta.projmob;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yougourta.projmob.Classes.MesRdvEnAttenteSingleRow;
import com.example.yougourta.projmob.Classes.MesRdvListeSingleRow;
import com.example.yougourta.projmob.Classes.Utilisateur;

import com.example.yougourta.projmob.Detail.DetailActivity;
import com.example.yougourta.projmob.NavDrawer.ConfirmerRdvs;
import com.example.yougourta.projmob.NavDrawer.MesRdvEnAttenteActivity;
import com.example.yougourta.projmob.TabLayout.AppartementFragment;

import java.security.acl.Group;
import java.util.ArrayList;

import com.example.yougourta.projmob.Classes.Utilisateur;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static ArrayList<MesRdvListeSingleRow> list;
    public static ArrayList<MesRdvEnAttenteSingleRow> list1;

    public static Utilisateur user1;
    public static Utilisateur user2;
    public static Utilisateur user3;
    public static Utilisateur user4;
    /*viive mobb*/

    public static boolean estConnecte = false;
    public static Utilisateur userConnected = null;

    TabLayout tabLayout;
    private DrawerLayout myDrawerLayout;
    private ActionBarDrawerToggle myToggle;
    NavigationView navigationView;

    String[] fruits = {"Bejaia", "Oued Smar", "El Kseur", "Oued Ghir", "Said Hamdine", "Ben Aknoun", "Bab Ezzouar"};

    public static AutoCompleteTextView actv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<MesRdvListeSingleRow>();
        list1 = new ArrayList<MesRdvEnAttenteSingleRow>();

        user1 = new Utilisateur("ArezkiBourihane06", "kiki_kiki", "+213780668840", "da_bourihane@esi.dz", "Sidi Ali Labhar", 0, false, null);
        user2 = new Utilisateur("B-Rekellah", "bily_kiki", "+213780668840", "db_rezkellah@esi.dz", "Tizi", 0, false, null);
        user3 = new Utilisateur("NadjiMob", "nadji_mob", "+213780668840", "dn_azri@esi.dz", "Stade", R.drawable.ic_picture2, true, null);
        user4 = new Utilisateur("YougortaTchowh", "juju_kiki", "+213780668840", "dy_ait_saada@esi.dz", "Polyvalent", R.drawable.ic_picture1, true, null);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /** RECHERCHE **/
        //Creating the instance of ArrayAdapter containing list of fruit names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, fruits);
        //Getting the instance of AutoCompleteTextView
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.clearFocus();

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new MyFragmentPageAdapter(getSupportFragmentManager(), this));
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);



    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        View hview = navigationView.getHeaderView(0);

        if (estConnecte == false) {
            navigationView.getMenu().setGroupVisible(R.id.nav_grp1_connecte, false);
            navigationView.getMenu().setGroupVisible(R.id.nav_grp2_connecte, false);
            navigationView.getMenu().setGroupVisible(R.id.nav_grp_non_connecte, true);

            ImageView imageView = (ImageView) hview.findViewById(R.id.nav_header_image);
            imageView.setImageResource(R.drawable.ic_account_circle_black_24dp);

            TextView textView1 = (TextView) hview.findViewById(R.id.header_textview1);
            TextView textView2 = (TextView) hview.findViewById(R.id.header_textview2);
            textView1.setText("Vous êtes hors connexion");
            textView2.setText(" ");
        } else {
            navigationView.getMenu().setGroupVisible(R.id.nav_grp1_connecte, true);
            navigationView.getMenu().setGroupVisible(R.id.nav_grp2_connecte, true);
            navigationView.getMenu().setGroupVisible(R.id.nav_grp_non_connecte, false);

            TextView textView1 = (TextView) hview.findViewById(R.id.header_textview1);
            TextView textView2 = (TextView) hview.findViewById(R.id.header_textview2);
            ImageView imageView = (ImageView) hview.findViewById(R.id.nav_header_image);
            imageView.setImageResource(userConnected.getImageUser());
            textView1.setText("Vous êtes connectés en tant que");
            textView2.setText(this.userConnected.getEmailUser());
        }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_accueil) {
            // Handle the camera action
        } else if (id == R.id.nav_mes_annonces) {


        } else if (id == R.id.nav_mes_rdv) {

            list1.add(new MesRdvEnAttenteSingleRow("AZRI Nadji", "APPARTEMENT f03", "17-01-2019", "15h"));
            list1.add(new MesRdvEnAttenteSingleRow("AIT SAADA ", "Villa 15", "17-01-2019", "15h"));
            list1.add(new MesRdvEnAttenteSingleRow("BOURIANE ", "Bungalow 15", "17-01-2019", "15h"));
            list1.add(new MesRdvEnAttenteSingleRow("AZRI Nadji", "APPARTEMENT f03", "17-01-2019", "15h"));
            list1.add(new MesRdvEnAttenteSingleRow("AZRI Nadji", "APPARTEMENT f03", "17-01-2019", "15h"));
            Intent intent = new Intent(MainActivity.this, MesRdvEnAttenteActivity.class);
            intent.putExtra("list", list1);
            startActivity(intent);

        } else if (id == R.id.nav_mes_demandes_rdv) {

            if((DetailActivity.rdv != null) && (!String.valueOf(DetailActivity.rdv.getHeure()).equals("null")))
            {
                list.add(DetailActivity.rdv);
            }

            Intent intent = new Intent(MainActivity.this, ConfirmerRdvs.class);
            intent.putExtra("list", list);
            startActivity(intent);

        } else if (id == R.id.nav_deconnexion) {
            estConnecte = false;
            userConnected = null;

            final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(false);
            progressDialog.setMessage("Déconnexion...");
            progressDialog.show();

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            recreate();
                            // onLoginFailed();
                            progressDialog.dismiss();
                        }
                    }, 1000);


        } else if (id == R.id.nav_se_connecter) {

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}

