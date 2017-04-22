package com.example.yougourta.projmob.NavDrawer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.yougourta.projmob.Classes.MesRdvListeSingleRow;
import com.example.yougourta.projmob.R;

import java.util.ArrayList;

import io.huannguyen.swipeablerv.SWItemRemovalListener;
import io.huannguyen.swipeablerv.view.SWRecyclerView;

public class ConfirmerRdvs extends AppCompatActivity {

    public static SWRecyclerView mRecyclerView;
    com.example.yougourta.projmob.NavDrawer.ConfirmerRdvsAdapter mAdapter;

    ArrayList<MesRdvListeSingleRow> listRdv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmer_rdvs);

        Intent intent = getIntent();
        listRdv = (ArrayList<MesRdvListeSingleRow>) intent.getSerializableExtra("list");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_dyalna);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (SWRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.getSwipeMessageBuilder()
                .withSwipeDirection(SWRecyclerView.SwipeMessageBuilder.BOTH)
                .build();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new com.example.yougourta.projmob.NavDrawer.ConfirmerRdvsAdapter(listRdv);
        // allow swiping with both directions (left-to-right and right-to-left)
        mRecyclerView.setAdapter(mAdapter);


        Bitmap annuler = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_delete_white_24dp);
        Bitmap confirmer = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_done_white_24dp);

        mRecyclerView.setRTLSwipeIcon(confirmer);
        mRecyclerView.setLTRSwipeIcon(annuler);

        mRecyclerView.setLTRUndoActionText("Annuler");
        mRecyclerView.setRTLUndoActionText("Annuler");

        mRecyclerView.setupSwipeToDismiss(mAdapter, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT);

        mAdapter.setItemRemovalListener(new SWItemRemovalListener<MesRdvListeSingleRow>() {
            @Override
            public void onItemTemporarilyRemoved(MesRdvListeSingleRow item, int position) {

            }

            @Override
            public void onItemPermanentlyRemoved(MesRdvListeSingleRow item) {

            }

            @Override
            public void onItemAddedBack(MesRdvListeSingleRow item, int position) {

            }
        });



    }
}
