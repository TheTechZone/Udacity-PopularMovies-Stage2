package com.example.adrian.popularmovies_stage2.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.adrian.popularmovies_stage2.R;
import com.example.adrian.popularmovies_stage2.model.Trailer;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {


    List<Trailer> trailers;
    String movieTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        String message = movieTitle + " looks like a great movie.\n";
        if(trailers != null && trailers.size() > 0){
            message += "Check out this trailer: " + trailers.get(0).getTrailerUrl();
        }
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setType("text/plain");
        startActivity(intent);
    }
}
