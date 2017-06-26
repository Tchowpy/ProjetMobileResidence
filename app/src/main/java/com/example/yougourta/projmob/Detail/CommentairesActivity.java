package com.example.yougourta.projmob.Detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yougourta.projmob.Classes.Commentaire;
import com.example.yougourta.projmob.Classes.Logement;
import com.example.yougourta.projmob.MainActivity;
import com.example.yougourta.projmob.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentairesActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Commentaire> commentaires;
    Logement logement;
    commentaireAdapter adapter;
    String url="http://192.168.43.76:8080/insertcomment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentaires);

        Intent intent = getIntent();
        logement = (Logement) intent.getSerializableExtra("logement");
        commentaires = logement.getCommentairesLogement();
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_dyalna);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Commentaires");

        listView = (ListView)findViewById(R.id.listView);
        adapter = new commentaireAdapter(commentaires);
        listView.setAdapter(adapter);

        TextView envoyer = (TextView)findViewById(R.id.envoyer);
        final EditText comment = (EditText)findViewById(R.id.commentaireEditText);

        envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Commentaire commentaire = new Commentaire(MainActivity.userConnected, comment.getText().toString(), logement.getIdLogement());
                commentaires.add(commentaire);
                RequestQueue queue = Volley.newRequestQueue(CommentairesActivity.this);
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Toast.makeText(CommentairesActivity.this, s, Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(CommentairesActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> map = new HashMap<String, String>();
                        map.put("commentaire", new Gson().toJson(commentaire));
                        return map;
                    }
                };

                queue.add(request);
                comment.setText("");
                adapter.notifyDataSetChanged();
                listView.smoothScrollToPosition(adapter.getCount());
                InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(
                        comment.getWindowToken(), 0);
            }
        });

    }
}
