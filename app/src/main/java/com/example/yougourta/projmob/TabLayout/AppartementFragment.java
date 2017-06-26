package com.example.yougourta.projmob.TabLayout;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.yougourta.projmob.Classes.Commentaire;
import com.example.yougourta.projmob.Classes.Disponibilite;
import com.example.yougourta.projmob.Classes.Logement;
import com.example.yougourta.projmob.Classes.MesRdvListeSingleRow;
import com.example.yougourta.projmob.Detail.CommentairesActivity;
import com.example.yougourta.projmob.Detail.DetailActivity;
import com.example.yougourta.projmob.ListeLogements.LogementsAdapter;
import com.example.yougourta.projmob.ListeLogements.RecyclerItemClickListener;
import com.example.yougourta.projmob.LoginActivity;
import com.example.yougourta.projmob.MainActivity;
import com.example.yougourta.projmob.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Nadji AZRI on 24/03/2017.
 */

public class AppartementFragment extends Fragment implements OnMapReadyCallback{

    int resultat = 0;

    //ArrayList<Logement> logements = new ArrayList<Logement>();
    ArrayList<Logement> logementsNew = new ArrayList<Logement>();
    List<Logement> logementList;
    View masterView;

    RatingBar ratingBar;
    ImageButton right;
    ImageButton left;
    int cpt = 0;
    private GoogleMap mMap;
    private SimpleDateFormat mFormatter = new SimpleDateFormat("MMMM dd yyyy hh:mm aa");
    private Button mButton;

    public static MesRdvListeSingleRow rdv;

    public static String aa = "";
    public static String mm = "";
    public static String jj = "";

    public static String hh = "";
    public static String mnt = "";

    public static DecimalFormat formatter;

    int pos=0;

    ImageSwitcher imageSwitcher;

    TextView prix;
    TextView titre;
    TextView adresse;
    TextView nb_chambres;
    TextView surface;
    TextView detail;
    TextView horaires;
    TextView carre;

    ImageButton noter;
    ImageButton commentaire;

    ImageButton appel;
    ImageButton email;
    ImageButton rendezvous;

    String url="http://192.168.43.76:8080/getget";

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.appartementfragment,container,false);

        appartement(view);

        if (isTwoPane(view))
        {
            SupportMapFragment mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.fragment2, mapFragment).commit();
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    public void appartement(View v)
    {
        view = v;
        if (isTwoPane(view))
        {
            imageSwitcher = (ImageSwitcher) view.findViewById(R.id.imageSwitcher);

            imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
                @Override
                public View makeView() {
                    ImageView myView = new ImageView(getContext());
                    myView.setScaleX(2.0f);
                    myView.setScaleY(2.0f);
                    return myView;
                }
            });
        }
        /** Commentaires **/
        //ArrayList de commentaires
        /*
        ArrayList<Commentaire> commentaire1 = new ArrayList<Commentaire>();
        ArrayList<Commentaire> commentaire2 = new ArrayList<Commentaire>();
        ArrayList<Commentaire> commentaire3 = new ArrayList<Commentaire>();
        ArrayList<Commentaire> commentaire4 = new ArrayList<Commentaire>();
        ArrayList<Commentaire> commentaire5 = new ArrayList<Commentaire>();
        ArrayList<Commentaire> commentaire6 = new ArrayList<Commentaire>();
        */

        //Remplissage des commentaires
        /*
        commentaire1.add(new Commentaire(MainActivity.user3, "Appartement nul, vraiment déguelasse... mais il est bien situé si vous songez à le réaménager !"));
        commentaire1.add(new Commentaire(MainActivity.user4, "Dommage que ça soit un F3"));
        commentaire1.add(new Commentaire(MainActivity.user3, "Exactement En plus c'est un F3, franchment .."));

        commentaire2.add(new Commentaire(MainActivity.user3, "Appartement nul, vraiment déguelasse..."));
        commentaire2.add(new Commentaire(MainActivity.user4, "Plutôt bien situé."));
        commentaire2.add(new Commentaire(MainActivity.user2, "Dommage que ça soit un F3"));
        commentaire2.add(new Commentaire(MainActivity.user3, "Super spacieux"));
        commentaire2.add(new Commentaire(MainActivity.user4, "Juste magnifique"));

        commentaire3.add(new Commentaire(MainActivity.user1, "Appartement nul, vraiment déguelasse..."));
        commentaire3.add(new Commentaire(MainActivity.user3, "Plutôt bien situé."));
        commentaire3.add(new Commentaire(MainActivity.user2, "Dommage que ça soit un F3"));
        commentaire3.add(new Commentaire(MainActivity.user4, "Super spacieux"));
        commentaire3.add(new Commentaire(MainActivity.user3, "Juste magnifique"));

        commentaire4.add(new Commentaire(MainActivity.user1, "Appartement nul, vraiment déguelasse..."));
        commentaire4.add(new Commentaire(MainActivity.user4, "Plutôt bien situé."));
        commentaire4.add(new Commentaire(MainActivity.user4, "Dommage que ça soit un F3"));
        commentaire4.add(new Commentaire(MainActivity.user2, "Super spacieux"));
        commentaire4.add(new Commentaire(MainActivity.user3, "Juste magnifique"));

        commentaire5.add(new Commentaire(MainActivity.user3, "Appartement nul, vraiment déguelasse..."));
        commentaire5.add(new Commentaire(MainActivity.user2, "Plutôt bien situé."));
        commentaire5.add(new Commentaire(MainActivity.user1, "Dommage que ça soit un F3"));
        commentaire5.add(new Commentaire(MainActivity.user1, "Super spacieux"));
        commentaire5.add(new Commentaire(MainActivity.user2, "Juste magnifique"));

        commentaire6.add(new Commentaire(MainActivity.user4, "Appartement nul, vraiment déguelasse..."));
        commentaire6.add(new Commentaire(MainActivity.user4, "Plutôt bien situé."));
        commentaire6.add(new Commentaire(MainActivity.user4, "Dommage que ça soit un F3"));
        commentaire6.add(new Commentaire(MainActivity.user3, "Super spacieux"));
        commentaire6.add(new Commentaire(MainActivity.user2, "Juste magnifique"));
        */

        /** IMAGES **/

        //ArrayList d'Images pour chaque appartement
        /*
        ArrayList<Integer> images1 = new ArrayList<Integer>();
        images1.add(R.drawable.ic_a);
        images1.add(R.drawable.ic_b);
        images1.add(R.drawable.ic_c);
        images1.add(R.drawable.ic_d);

        ArrayList<Integer> images2 = new ArrayList<Integer>();
        images2.add(R.drawable.ic_b);

        ArrayList<Integer> images3 = new ArrayList<Integer>();
        images3.add(R.drawable.ic_c);

        ArrayList<Integer> images4 = new ArrayList<Integer>();
        images4.add(R.drawable.ic_d);

        ArrayList<Integer> images5 = new ArrayList<Integer>();
        images5.add(R.drawable.ic_e);

        ArrayList<Integer> images6 = new ArrayList<Integer>();
        images6.add(R.drawable.ic_f);
        */

        /** DISPONIBILITE **/

        /**Creéation d'un ensemble de jourDispo**/
        /*Disponibilite disponibilite1 = new Disponibilite("Mardi", "15h", "16h");
        Disponibilite disponibilite2 = new Disponibilite("Samedi", "9h", "12h");
        Disponibilite disponibilite3 = new Disponibilite("Jeudi", "18h", "19h");
        Disponibilite disponibilite4 = new Disponibilite("Vendredi", "16h", "18h");

        //ArrayList de Disponibilites
        ArrayList<Disponibilite> disponibilites = new ArrayList<Disponibilite>();
        disponibilites.add(disponibilite1);
        disponibilites.add(disponibilite3);
        disponibilites.add(disponibilite4);*/

        /** LOGEMENTS **/

        /**Création d'un ensemble de logements**/
        /*
        Logement logement1 = new Logement("Appartement","80, 000", "F3", "98", "2", "Bejaia", "     Appartement pour location, bonne localisation, citée calme avec un voisinage superbe." +'\n'+'\n'+"Appartement pour location, bonne localisation, citée calme avec un voisinage superbe. Il vous apportera lux et confort et plei nde bla bla bla, oui j'écris ça juste pour remplire.", 36.735160, 5.0469151, disponibilites, images1, MainActivity.user4, "3.4", commentaire1, "Libre", "13");
        Logement logement2 = new Logement("Appartement","43, 000", "F4", "221", "3", "Bejaia", "Appartement pour location, bonne localisation", 36.761015, 5.056305, disponibilites, images2, MainActivity.user3, "3.4", commentaire2, "Loué", "43");
        Logement logement3 = new Logement("Appartement","21, 000", "F2", "438", "1", "Bejaia", "Appartement pour location, bonne localisation", 36.751141, 5.0557437, disponibilites, images3, MainActivity.user3, "3.4", commentaire3, "Libre", "103");
        Logement logement4 = new Logement("Appartement","33, 000", "F4", "354", "3", "Bejaia", "Appartement pour location, bonne localisation", 36.753199, 5.034329, disponibilites, images4, MainActivity.user4, "3.4", commentaire4, "Loué", "213");
        Logement logement5 = new Logement("Appartement","54, 000", "F3", "230", "2", "Bejaia", "Appartement pour location, bonne localisation", 36.739379, 5.062149, disponibilites, images5, MainActivity.user2, "3.4", commentaire5, "Loué", "113");
        Logement logement6 = new Logement("Appartement","28, 000", "F3", "196", "2", "Ben Aknoun", "Appartement pour location, bonne localisation", 36.741457, 5.045203, disponibilites, images6, MainActivity.user1, "3.4", commentaire6, "Libre", "413");
        */
        /**ArrayList de logements**/
        /*
        logements.add(logement1);
        logements.add(logement2);
        logements.add(logement3);
        logements.add(logement4);
        logements.add(logement5);
        logements.add(logement6);

        logementsNew.addAll(logements);
        */

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Gson gson = new Gson();
                logementList = Arrays.asList(gson.fromJson(jsonArray.toString(), Logement[].class));
                recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewAppartement);

                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                recyclerView.setHasFixedSize(true);

                // use a linear layout manager
                layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);

                // specify an adapter (see also next example)
                adapter = new LogementsAdapter(logementList, getActivity().getApplicationContext());
                recyclerView.setAdapter(adapter);

                masterView = view;

                /*if (isTwoPane(view))
                {
                    prix = (TextView) view.findViewById(R.id.prix);
                    titre = (TextView) view.findViewById(R.id.titre);
                    adresse = (TextView) view.findViewById(R.id.adresse);
                    nb_chambres = (TextView) masterView.findViewById(R.id.nb_chambres);
                    surface = (TextView) masterView.findViewById(R.id.surface);
                    detail = (TextView) masterView.findViewById(R.id.detail);
                    horaires = (TextView) masterView.findViewById(R.id.horaires);
                    carre = (TextView)masterView.findViewById(R.id.carre);

                    noter = (ImageButton) masterView.findViewById(R.id.note);
                    commentaire = (ImageButton) masterView.findViewById(R.id.commentaires);

                    appel = (ImageButton) masterView.findViewById(R.id.appel);
                    email = (ImageButton) masterView.findViewById(R.id.email);
                    rendezvous = (ImageButton) masterView.findViewById(R.id.rendezvous);


                    ratingBar = (RatingBar) masterView.findViewById(R.id.ratingBar);

                    insertView(0);
                }*/

                recyclerView.addOnItemTouchListener(
                        new RecyclerItemClickListener(getActivity(), new   RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if(isTwoPane(masterView))
                                {
                                    pos = position;

                                    LatLng sydney = new LatLng(logementList.get(pos).getLatitude(), logementList.get(pos).getLongetude());
                                    mMap.addMarker(new MarkerOptions().position(sydney).title(logementList.get(pos).getTitreLogement()));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                    mMap.animateCamera(CameraUpdateFactory.zoomIn());

                                    CameraPosition cameraPosition = new CameraPosition.Builder()
                                            .target(new LatLng(logementList.get(pos).getLatitude(), logementList.get(pos).getLongetude()))      // Sets the center of the map to Mountain View
                                            .zoom(17)                   // Sets the zoom
                                            .bearing(90)                // Sets the orientation of the camera to east
                                            .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                            .build();                   // Creates a CameraPosition from the builder
                                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                    insertView(position);
                                }
                                else
                                {
                                    createIntent(position);
                                }
                            }
                        })
                );

                MainActivity.actv.clearFocus();

                MainActivity.actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int i = 0;
                        while (i < logementList.size())
                        {
                            if (!logementList.get(i).getAdrLogement().equals(MainActivity.actv.getText().toString()))
                            {
                                logementList.remove(i);
                                adapter.notifyItemRemoved(i);
                            }
                            else
                            {
                                i++;
                            }
                        }

                    }
                });


                MainActivity.actv.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (String.valueOf(s).isEmpty())
                        {
                            logementList.clear();
                            logementList.addAll(logementsNew);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity().getApplicationContext(), "Une erreur s'est produite", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }

    public void createIntent(int position)
    {
        Intent intent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
        intent.putExtra("appartement", logementList.get(position));
        startActivity(intent);
    }

    public boolean isTwoPane(View v)
    {
        View view = v.findViewById(R.id.fragment1);
        return (view != null);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(logementList.get(pos).getLatitude(), logementList.get(pos).getLongetude());
        mMap.addMarker(new MarkerOptions().position(sydney).title(logementList.get(pos).getTitreLogement()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(logementList.get(pos).getLatitude(), logementList.get(pos).getLongetude()))      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }



    public void insertView(int position)
    {
        carre.setText(Html.fromHtml("m<sup>2</sup>"));

        prix.setText(logementList.get(position).getPrixLogement());

        imageSwitcher.setImageResource(logementList.get(pos).getImages().get(0));


        final Animation in = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_out_right);
        imageSwitcher.setInAnimation(in);
        imageSwitcher.setOutAnimation(out);

        right = (ImageButton) masterView.findViewById(R.id.imageButtonRight);
        left = (ImageButton) masterView.findViewById(R.id.imageButtonLeft);

        left.setVisibility(View.INVISIBLE);

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cpt++;
                if (cpt >= logementList.get(pos).getImages().size()) {
                    right.setVisibility(View.INVISIBLE);
                } else {
                    imageSwitcher.setImageResource(logementList.get(pos).getImages().get(cpt));
                }

                if (cpt > -1) {
                    left.setVisibility(View.VISIBLE);
                }
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cpt--;
                if (cpt <= -1) {
                    left.setVisibility(View.INVISIBLE);
                    right.setVisibility(View.VISIBLE);
                } else {
                    imageSwitcher.setImageResource(logementList.get(pos).getImages().get(cpt));
                }

                if (cpt < logementList.get(pos).getImages().size()) {
                    right.setVisibility(View.VISIBLE);
                }
            }
        });


        float noteFinale;

        ratingBar.setRating(Float.parseFloat(logementList.get(position).getNoteLogement()));
        titre.setText(logementList.get(position).getTitreLogement() + " " + logementList.get(position).getTypeLogement() + " à louer.");
        adresse.setText(logementList.get(position).getAdrLogement());
        nb_chambres.setText(logementList.get(position).getNb_chambreLogement());
        surface.setText(logementList.get(position).getSurfaceLogement());
        detail.setText(logementList.get(position).getDetailLogement());

        String str = logementList.get(position).getJoursVisiteLogement().get(0).getJourDispo() + " : " + logementList.get(position).getJoursVisiteLogement().get(0).getHeureDebutDispo() + " - " + logementList.get(position).getJoursVisiteLogement().get(0).getHeureFinDispo();
        for (int i = 1; i < logementList.get(position).getJoursVisiteLogement().size(); i++) {
            str = str + '\n' + logementList.get(position).getJoursVisiteLogement().get(i).getJourDispo() + " : " + logementList.get(position).getJoursVisiteLogement().get(i).getHeureDebutDispo() + " - " + logementList.get(position).getJoursVisiteLogement().get(i).getHeureFinDispo();
        }
        horaires.setText(str);

        noter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MainActivity.estConnecte == false)
                {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext(),R.style.datepicker);
                    builder1.setMessage("Vous devez vous connecter !");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Se connecter",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent1 = new Intent(getContext(), LoginActivity.class);
                                    startActivity(intent1);
                                    dialog.cancel();

                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
                else{
                    final Dialog mDialog = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth);

                    mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    mDialog.setContentView(R.layout.activity_rating);
                    mDialog.show();

                    final RatingBar ratingBarInterne = (RatingBar) mDialog.findViewById(R.id.ratingBar2);
                    Button submit = (Button) mDialog.findViewById(R.id.submit);

                    ratingBarInterne.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float rating,
                                                    boolean fromUser) {
                            if (rating < 1.0f)
                                ratingBar.setRating(1.0f);
                        }
                    });

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            logementList.get(pos).setNoteLogement(String.valueOf((ratingBarInterne.getRating() + Float.parseFloat(logementList.get(pos).getNoteLogement())) / 2));
                            ratingBar.setRating(Float.parseFloat(logementList.get(pos).getNoteLogement()));
                            mDialog.cancel();
                        }
                    });
                }

            }
        });

        commentaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.estConnecte==false){

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext(),R.style.datepicker);
                    builder1.setMessage("Vous devez vous connecter !");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Se connecter",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent1 = new Intent(getContext(), LoginActivity.class);
                                    intent1.putExtra("commentaires", logementList.get(pos).getCommentairesLogement());
                                    startActivity(intent1);
                                    dialog.cancel();

                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else{
                    Intent intent = new Intent(getContext(), CommentairesActivity.class);
                    intent.putExtra("commentaires", logementList.get(pos).getCommentairesLogement());
                    startActivity(intent);
                }
            }
        });


        appel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+logementList.get(pos).getProprietaireLogement().getTelUser()));
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);

            }
        });


        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{logementList.get(pos).getProprietaireLogement().getEmailUser()});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, logementList.get(pos).getTitreLogement()+" "+logementList.get(pos).getTypeLogement());

                try
                {
                    startActivity(Intent.createChooser(emailIntent, "Envoyer un mail..."));
                    //finish();
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getContext(), "Aucune application Mail installée.", Toast.LENGTH_SHORT).show();
                }
            }
        });



        rendezvous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MainActivity.estConnecte == false){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext(),R.style.datepicker);
                    builder1.setMessage("Vous devez vous connecter !");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Se connecter",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intntnn = new Intent(getContext(),LoginActivity.class);
                                    startActivity(intntnn);
                                    dialog.cancel();

                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else
                {
                    formatter = new DecimalFormat("00");

                    rdv = new MesRdvListeSingleRow();
                    rdv.setNom(MainActivity.userConnected.getIdUser());
                    rdv.setLogement(logementList.get(pos).getTitreLogement());

                    Calendar mcurrentTime = Calendar.getInstance();
                    int mYear = mcurrentTime.get(Calendar.YEAR); // current year
                    int mMonth = mcurrentTime.get(Calendar.MONTH); // current month
                    int mDay = mcurrentTime.get(Calendar.DAY_OF_MONTH); // current day
                    DatePickerDialog mDatePicker;
                    mDatePicker = new DatePickerDialog(getContext(), R.style.datepicker,new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            aa = String.valueOf(year);
                            mm = String.valueOf(month);
                            jj = String.valueOf(dayOfMonth);
                            rdv.setDate(jj+"-"+ String.format("%02d", month+1)+"-"+aa);

                            Calendar mcurrentTime2 = Calendar.getInstance();
                            int hour = mcurrentTime2.get(Calendar.HOUR_OF_DAY);
                            int minute = mcurrentTime2.get(Calendar.MINUTE);

                            TimePickerDialog mTimePicker;

                            mTimePicker = new TimePickerDialog(getContext(), R.style.datepicker, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                    hh = String.valueOf(selectedHour);
                                    mnt = String.valueOf(selectedMinute);
                                    rdv.setHeure(String.format("%02d", selectedHour)+"h"+String.format("%02d", selectedMinute));

                                    Toast.makeText(getContext(), "Demande Envoyée", Toast.LENGTH_SHORT).show();
                                }
                            }, hour, minute, true);//Yes 24 hour time
                            mTimePicker.show();
                        }
                    }, mYear, mMonth, mDay);
                    mDatePicker.show();
                }
            }
        });
    }
}