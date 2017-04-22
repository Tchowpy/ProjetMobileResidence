package com.example.yougourta.projmob;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.yougourta.projmob.Classes.Commentaire;
import com.example.yougourta.projmob.Classes.Logement;
import com.example.yougourta.projmob.Classes.MesRdvListeSingleRow;
import com.example.yougourta.projmob.Classes.Utilisateur;
import com.example.yougourta.projmob.Detail.CommentairesActivity;
import com.example.yougourta.projmob.Detail.DetailActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.example.yougourta.projmob.Detail.DetailActivity.jj;

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {
    ArrayList<Commentaire> commentaires = null;


    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Intent itnt = getIntent();
        commentaires = (ArrayList<Commentaire>) itnt.getSerializableExtra("commentaires");
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {


        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Connexion...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.



        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 1000);

        /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically

                //this.finish();


            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        if(commentaires != null){
            Intent intent = new Intent(LoginActivity.this, CommentairesActivity.class);
            intent.putExtra("commentaires", commentaires);
            startActivity(intent);
            finish();
        }


        else {
            finish();
            /*vive mob*/
        }
    }

    public void onLoginFailed() {


        AlertDialog.Builder builder1 = new AlertDialog.Builder(this,R.style.datepicker);
        builder1.setMessage("Email ou mot de passe invalide ! ");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        if(MainActivity.user1.getEmailUser().equals(email) && MainActivity.user1.getMdpUser().equals(password)){

            MainActivity.estConnecte =true;
            MainActivity.userConnected = MainActivity.user1;
            valid = true;
        }
        else if(MainActivity.user2.getEmailUser().equals(email) && MainActivity.user2.getMdpUser().equals(password)){
            MainActivity.estConnecte =true;
            MainActivity.userConnected = MainActivity.user2;
            valid = true;
        }
        else if(MainActivity.user3.getEmailUser().equals(email) && MainActivity.user3.getMdpUser().equals(password)){
            MainActivity.estConnecte =true;
            MainActivity.userConnected = MainActivity.user3;
            valid = true;
        }
        else if(MainActivity.user4.getEmailUser().equals(email) && MainActivity.user4.getMdpUser().equals(password)){
            MainActivity.estConnecte =true;
            MainActivity.userConnected = MainActivity.user4;
            valid = true;
        }
        else {
            _passwordText.setError(null);
            valid = false;
        }

        return valid;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
