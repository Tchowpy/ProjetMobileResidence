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
import com.example.yougourta.projmob.Classes.ConnexionManager;
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
    ArrayList<Logement> logementList;
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

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Gson gson = new Gson();
                logementList = new ArrayList<>(Arrays.asList(gson.fromJson(jsonArray.toString(), Logement[].class)));
                logementsNew.addAll(logementList);
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

        ratingBar.setRating(logementList.get(position).getNoteLogement());
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
                ConnexionManager connexionManager = new ConnexionManager(getContext());
                if(connexionManager.isUserConnected() == false)
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
                            logementList.get(pos).setNoteLogement((ratingBarInterne.getRating() + logementList.get(pos).getNoteLogement()) / 2);
                            ratingBar.setRating(logementList.get(pos).getNoteLogement());
                            mDialog.cancel();
                        }
                    });
                }

            }
        });

        commentaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnexionManager connexionManager = new ConnexionManager(getContext());

                if(connexionManager.isUserConnected()==false){

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



        /*rendezvous.setOnClickListener(new View.OnClickListener() {
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
        });*/
    }
}