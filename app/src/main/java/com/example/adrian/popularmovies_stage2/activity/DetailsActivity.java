package com.example.adrian.popularmovies_stage2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.adrian.popularmovies_stage2.R;
import com.example.adrian.popularmovies_stage2.data.model.Trailer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {


    List<Trailer> trailers;
    String movieTitle;
    @BindView(R.id.toolbar_details) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_details);
        setSupportActionBar(toolbar);

        movieTitle = getIntent().getStringExtra("title");
        setTitle(movieTitle);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_share:
                sendTextIntent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setTrailers(List<Trailer> trailers){
        this.trailers = trailers;
    }

    public void sendTextIntent(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        String message = String.format(getString(R.string.share_statement), movieTitle);
        if(trailers != null && trailers.size() > 0){
            message += String.format(getString(R.string.share_trailer), trailers.get(0).getTrailerUrl());
        }
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setType("text/plain");
        startActivity(intent);
    }
}
