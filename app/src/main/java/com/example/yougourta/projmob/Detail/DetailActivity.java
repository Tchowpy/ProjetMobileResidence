package com.example.yougourta.projmob.Detail;

import android.Manifest;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
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
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.yougourta.projmob.Classes.ConnexionManager;
import com.example.yougourta.projmob.Classes.Logement;
import com.example.yougourta.projmob.Classes.MesRdvListeSingleRow;
import com.example.yougourta.projmob.Classes.RendezVous;
import com.example.yougourta.projmob.Classes.Utilisateur;
import com.example.yougourta.projmob.LoginActivity;
import com.example.yougourta.projmob.MainActivity;
import com.example.yougourta.projmob.NavDrawer.ConfirmerRdvs;
import com.example.yougourta.projmob.R;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


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

    private RendezVous rdv;

    public static String aa = "";
    public static String mm = "";
    public static String jj = "";

    public static String hh = "";
    public static String mnt = "";

    public static DecimalFormat formatter;
    ArrayList markerPoints = new ArrayList();

    String url="http://192.168.43.76:8080/insertrdv";

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display d = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int screenWidth = (int)(d.getWidth());

        Log.d("-----------", String.valueOf(screenWidth));
        if(screenWidth > 1200)
        {
            Intent intent = new Intent(DetailActivity.this, MainActivity.class);
            startActivity(intent);
        }

        setContentView(R.layout.activity_detail);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_dyalna);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Détail");


        final Intent intent = getIntent();
        logement = (Logement) intent.getSerializableExtra("appartement");

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ImageAdapter adapter = new ImageAdapter(this, logement);
        viewPager.setAdapter(adapter);

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

        ratingBar.setRating(logement.getNoteLogement());
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
                ConnexionManager connexionManager = new ConnexionManager(DetailActivity.this);

                if(connexionManager.isUserConnected()==false){

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
                            logement.setnbnotesLogement(logement.getnbnotesLogement()+1);
                            logement.setNoteLogement((ratingBarInterne.getRating() + logement.getNoteLogement()) / (logement.getnbnotesLogement()));
                            ratingBar.setRating(logement.getNoteLogement());

                            String url="http://192.168.43.76:8080/updatenote";
                            RequestQueue queue = Volley.newRequestQueue(DetailActivity.this);
                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    Toast.makeText(DetailActivity.this, s, Toast.LENGTH_SHORT).show();

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Toast.makeText(DetailActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {

                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put("lgt", new Gson().toJson(logement));
                                    return map;
                                }
                            };

                            queue.add(request);

                            mDialog.cancel();
                        }
                    });
                }

            }
        });

        commentaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnexionManager connexionManager = new ConnexionManager(DetailActivity.this);

                if(connexionManager.isUserConnected()==false){

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(DetailActivity.this,R.style.datepicker);
                    builder1.setMessage("Vous devez vous connecter !");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Se connecter",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent1 = new Intent(DetailActivity.this, LoginActivity.class);
                                    intent1.putExtra("logement", logement);
                                    startActivity(intent1);
                                    dialog.cancel();

                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else{
                    Intent intent = new Intent(DetailActivity.this, CommentairesActivity.class);
                    intent.putExtra("logement", logement);
                    startActivity(intent);
                }
            }
        });


        appel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+logement.getProprietaireLogement().getTelUser()));
                if (ActivityCompat.checkSelfPermission(DetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
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
                ConnexionManager connexionManager = new ConnexionManager(DetailActivity.this);

                if(connexionManager.isUserConnected()==false)
                {
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

                    Utilisateur usr = new Utilisateur();
                    usr.setIdUser(connexionManager.getIdConnected());
                    usr.setEmailUser(connexionManager.getEmailConnected());
                    usr.setImageUser(connexionManager.getImageConnected());

                    rdv = new RendezVous();
                    rdv.setVisiteurRDV(usr);
                    rdv.setLogementRDV(logement);
                    rdv.setEtatRDV(0);

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
                            rdv.setDateRDV(jj+"-"+ String.format("%02d", month+1)+"-"+aa);

                            Calendar mcurrentTime2 = Calendar.getInstance();
                            int hour = mcurrentTime2.get(Calendar.HOUR_OF_DAY);
                            int minute = mcurrentTime2.get(Calendar.MINUTE);

                            TimePickerDialog mTimePicker;

                            mTimePicker = new TimePickerDialog(DetailActivity.this, R.style.datepicker, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                    hh = String.valueOf(selectedHour);
                                    mnt = String.valueOf(selectedMinute);
                                    rdv.setHeureRDV(String.format("%02d", selectedHour)+"h"+String.format("%02d", selectedMinute));

                                    Toast.makeText(DetailActivity.this, "Demande Envoyée", Toast.LENGTH_SHORT).show();

                                    RequestQueue queue = Volley.newRequestQueue(DetailActivity.this);
                                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String s) {
                                            Toast.makeText(DetailActivity.this, s, Toast.LENGTH_SHORT).show();
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {
                                            Toast.makeText(DetailActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }){
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {

                                            Map<String, String> map = new HashMap<String, String>();
                                            map.put("rdv", new Gson().toJson(rdv));
                                            return map;
                                        }
                                    };

                                    queue.add(request);
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
        mMap.addMarker(new MarkerOptions().position(sydney).title(logement.getTitreLogement()+" "+logement.getTypeLogement()+" "+logement.getAdrLogement()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(logement.getLatitude(), logement.getLongetude()))      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.setMyLocationEnabled(true);



        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else
        {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        String url = getMapsApiDirectionsUrl(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), new LatLng(logement.getLatitude(), logement.getLongetude()));
        ReadTask downloadTask = new ReadTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    private String  getMapsApiDirectionsUrl(LatLng origin,LatLng dest) {
        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;


        return url;

    }

    private class ReadTask extends AsyncTask<String, Void , String> {

        @Override
        protected String doInBackground(String... url) {
            // TODO Auto-generated method stub
            String data = "";
            try {
                MapHttpConnection http = new MapHttpConnection();
                data = http.readUr(url[0]);


            } catch (Exception e) {
                // TODO: handle exception
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }

    }

    public class MapHttpConnection {
        public String readUr(String mapsApiDirectionsUrl) throws IOException{
            String data = "";
            InputStream istream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(mapsApiDirectionsUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                istream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(istream));
                StringBuffer sb = new StringBuffer();
                String line ="";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                data = sb.toString();
                br.close();


            }
            catch (Exception e) {
                Log.d("Exception :", e.toString());
            } finally {
                istream.close();
                urlConnection.disconnect();
            }
            return data;

        }
    }

    public class PathJSONParser {

        public List<List<HashMap<String, String>>> parse(JSONObject jObject) {
            List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>();
            JSONArray jRoutes = null;
            JSONArray jLegs = null;
            JSONArray jSteps = null;
            try {
                jRoutes = jObject.getJSONArray("routes");
                for (int i=0 ; i < jRoutes.length() ; i ++) {
                    jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                    List<HashMap<String, String>> path = new ArrayList<HashMap<String,String>>();
                    for(int j = 0 ; j < jLegs.length() ; j++) {
                        jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");
                        for(int k = 0 ; k < jSteps.length() ; k ++) {
                            String polyline = "";
                            polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                            List<LatLng> list = decodePoly(polyline);
                            for(int l = 0 ; l < list.size() ; l ++){
                                HashMap<String, String> hm = new HashMap<String, String>();
                                hm.put("lat",
                                        Double.toString(((LatLng) list.get(l)).latitude));
                                hm.put("lng",
                                        Double.toString(((LatLng) list.get(l)).longitude));
                                path.add(hm);
                            }
                        }
                        routes.add(path);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;

        }

        private List<LatLng> decodePoly(String encoded) {
            List<LatLng> poly = new ArrayList<LatLng>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }
            return poly;
        }}

    private class ParserTask extends AsyncTask<String,Integer, List<List<HashMap<String , String >>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {
            // TODO Auto-generated method stub
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(4);
                polyLineOptions.color(Color.BLUE);
            }

            mMap.addPolyline(polyLineOptions);

        }
    }

}