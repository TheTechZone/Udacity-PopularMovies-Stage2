package com.example.adrian.popularmovies_stage2.fragment;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.adrian.popularmovies_stage2.R;
import com.example.adrian.popularmovies_stage2.data.model.Trailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adrian on 22.02.2018.
 */

public class TrailersDialogFragment extends DialogFragment {

    private List<Trailer> trailers;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        Bundle arguments = getArguments();
        String[] titleArray = new String[0];
        String[] urlArray = new String[0];
        ArrayList<String> titles = arguments.getStringArrayList("titles");
        ArrayList<String> urls = arguments.getStringArrayList("urls");

        if (titles != null) {
            titleArray = titles.toArray(new String[titles.size()]);
        }

        if (urls != null) {
            urlArray = urls.toArray(new String[titles.size()]);
        }
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setTitle("Pick a trailer to watch")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        if(titleArray!=null && titleArray.length > 0) {
            final String[] finalUrlArray = urlArray;
            builder.setItems(titleArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    watchTrailer(getContext(), finalUrlArray[i]);
                }
            });
        }else {
            builder.setMessage("No trailer available :(");
        }
        // 3. Create the AlertDialog object and return it
        return builder.create();
    }

    public static void watchTrailer(Context context, String url){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        Intent chooser = Intent.createChooser(intent , context.getString(R.string.open_with));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            try {
                context.startActivity(chooser);
            } catch (ActivityNotFoundException e) {

            }
        }
    }
}
