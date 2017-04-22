package com.example.yougourta.projmob.Detail;

import android.Manifest;
import android.support.v7.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

import com.example.yougourta.projmob.Classes.Logement;
import com.example.yougourta.projmob.Classes.MesRdvListeSingleRow;
import com.example.yougourta.projmob.Classes.RendezVous;
import com.example.yougourta.projmob.LoginActivity;
import com.example.yougourta.projmob.MainActivity;
import com.example.yougourta.projmob.NavDrawer.ConfirmerRdvs;
import com.example.yougourta.projmob.R;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    public static Logement logement;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_dyalna);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        final Intent intent = getIntent();
        logement = (Logement) intent.getSerializableExtra("appartement");


        final ImageSwitcher imageSwitcher;
        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                myView.setScaleX(2.0f);
                myView.setScaleY(2.0f);
                return myView;
            }
        });

        imageSwitcher.setImageResource(logement.getImages().get(0));


        final Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        imageSwitcher.setInAnimation(in);
        imageSwitcher.setOutAnimation(out);

        right = (ImageButton) findViewById(R.id.imageButtonRight);
        left = (ImageButton) findViewById(R.id.imageButtonLeft);

        left.setVisibility(View.INVISIBLE);

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cpt++;
                if (cpt >= logement.getImages().size()) {
                    right.setVisibility(View.INVISIBLE);
                } else {
                    imageSwitcher.setImageResource(logement.getImages().get(cpt));
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
                    imageSwitcher.setImageResource(logement.getImages().get(cpt));
                }

                if (cpt < logement.getImages().size()) {
                    right.setVisibility(View.VISIBLE);
                }
            }
        });


        TextView prix = (TextView) findViewById(R.id.prix);
        TextView titre = (TextView) findViewById(R.id.titre);
        TextView adresse = (TextView) findViewById(R.id.adresse);
        TextView nb_chambres = (TextView) findViewById(R.id.nb_chambres);
        TextView surface = (TextView) findViewById(R.id.surface);
        TextView detail = (TextView) findViewById(R.id.detail);
        TextView horaires = (TextView) findViewById(R.id.horaires);
        TextView carre = (TextView)findViewById(R.id.carre);

        carre.setText(Html.fromHtml("m<sup>2</sup>"));

        ImageButton noter = (ImageButton) findViewById(R.id.note);
        ImageButton commentaire = (ImageButton) findViewById(R.id.commentaires);

        ImageButton appel = (ImageButton) findViewById(R.id.appel);
        ImageButton email = (ImageButton) findViewById(R.id.email);
        ImageButton rendezvous = (ImageButton) findViewById(R.id.rendezvous);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        prix.setText(logement.getPrixLogement());

        float noteFinale;

        ratingBar.setRating(Float.parseFloat(logement.getNoteLogement()));
        titre.setText(logement.getTitreLogement() + " " + logement.getTypeLogement() + " à louer.");
        adresse.setText(logement.getAdrLogement());
        nb_chambres.setText(logement.getNb_chambreLogement());
        surface.setText(logement.getSurfaceLogement());
        detail.setText(logement.getDetailLogement());

        String str = logement.getJoursVisiteLogement().get(0).getJourDispo() + " : " + logement.getJoursVisiteLogement().get(0).getHeureDebutDispo() + " - " + logement.getJoursVisiteLogement().get(0).getHeureFinDispo();
        for (int i = 1; i < logement.getJoursVisiteLogement().size(); i++) {
            str = str + '\n' + logement.getJoursVisiteLogement().get(i).getJourDispo() + " : " + logement.getJoursVisiteLogement().get(i).getHeureDebutDispo() + " - " + logement.getJoursVisiteLogement().get(i).getHeureFinDispo();
        }
        horaires.setText(str);

        noter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MainActivity.estConnecte == false){

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(DetailActivity.this,R.style.datepicker);
                    builder1.setMessage("Vous devez vous connecter !");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Se connecter",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent1 = new Intent(DetailActivity.this, LoginActivity.class);
                                    startActivity(intent1);
                                    dialog.cancel();

                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
                else{
                    final Dialog mDialog = new Dialog(DetailActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth);

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
                            logement.setNoteLogement(String.valueOf((ratingBarInterne.getRating() + Float.parseFloat(logement.getNoteLogement())) / 2));
                            ratingBar.setRating(Float.parseFloat(logement.getNoteLogement()));
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

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(DetailActivity.this,R.style.datepicker);
                    builder1.setMessage("Vous devez vous connecter !");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Se connecter",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent1 = new Intent(DetailActivity.this, LoginActivity.class);
                                    intent1.putExtra("commentaires", logement.getCommentairesLogement());
                                    startActivity(intent1);
                                    dialog.cancel();

                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else{
                    Intent intent = new Intent(DetailActivity.this, CommentairesActivity.class);
                    intent.putExtra("commentaires", logement.getCommentairesLogement());
                    startActivity(intent);
                }
            }
        });


        appel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+logement.getProprietaireLogement().getTelUser()));
                if (ActivityCompat.checkSelfPermission(DetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{logement.getProprietaireLogement().getEmailUser()});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, logement.getTitreLogement()+" "+logement.getTypeLogement());

                try {
                    startActivity(Intent.createChooser(emailIntent, "Envoyer un mail..."));
                    finish();
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(DetailActivity.this, "Aucune application Mail installée.", Toast.LENGTH_SHORT).show();
                }
            }
        });



        rendezvous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MainActivity.estConnecte == false){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(DetailActivity.this,R.style.datepicker);
                    builder1.setMessage("Vous devez vous connecter !");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Se connecter",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intntnn = new Intent(DetailActivity.this,LoginActivity.class);
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
                    rdv.setLogement(logement.getTitreLogement());

                    Calendar mcurrentTime = Calendar.getInstance();
                    int mYear = mcurrentTime.get(Calendar.YEAR); // current year
                    int mMonth = mcurrentTime.get(Calendar.MONTH); // current month
                    int mDay = mcurrentTime.get(Calendar.DAY_OF_MONTH); // current day
                    DatePickerDialog mDatePicker;
                    mDatePicker = new DatePickerDialog(DetailActivity.this, R.style.datepicker,new DatePickerDialog.OnDateSetListener() {
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

                            mTimePicker = new TimePickerDialog(DetailActivity.this, R.style.datepicker, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                    hh = String.valueOf(selectedHour);
                                    mnt = String.valueOf(selectedMinute);
                                    rdv.setHeure(String.format("%02d", selectedHour)+"h"+String.format("%02d", selectedMinute));

                                    Toast.makeText(DetailActivity.this, "Demande Envoyée", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(logement.getLatitude(), logement.getLongetude());
        mMap.addMarker(new MarkerOptions().position(sydney).title(logement.getTitreLogement()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(logement.getLatitude(), logement.getLongetude()))      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
