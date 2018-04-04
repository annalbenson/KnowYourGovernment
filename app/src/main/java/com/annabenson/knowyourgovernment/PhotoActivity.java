package com.annabenson.knowyourgovernment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Anna on 4/4/2018.
 */

public class PhotoActivity extends AppCompatActivity {

    /* 3/16 pdf
    office, name, background color (but not party)
     */

    public static final String TAG = "PhotoActivity";
    public static final String NO_DATA = "No Data Provided";
    public TextView officeView;
    public TextView nameView;

    public TextView locationView;

    public ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        setContentView(R.layout.activity_photo);

        locationView = findViewById(R.id.locationID);
        officeView = findViewById(R.id.officeID);
        nameView = findViewById(R.id.nameID);
        imageView = findViewById(R.id.imageID);
        //imageView.setImageResource(R.drawable.ic_hourglass_empty_white_24dp);

        Intent intent = this.getIntent();
        String header = intent.getStringExtra("header");
        Log.d(TAG, "onCreate: got header" + header);
        locationView.setText(header.toString());
        //if(header != null){
        //    locationView.setText(header);
        //}
        //locationView.setText(intent.getStringExtra("header"));
        officeView.setText(intent.getStringExtra("office"));
        nameView.setText(intent.getStringExtra("name"));
        String color = intent.getStringExtra("color");
        if (color.equals("red")) {
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.darkRed));
        }
        if (color.equals("blue")) {
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.darkBlue));
        }

        /*Image Loading*/
        Log.d(TAG, "onCreate: Image Loading");
        final String photoUrl = intent.getStringExtra("photoUrl");
        Log.d(TAG, "onCreate: photo url is " + photoUrl);
        Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                // Here we try https if the http image attempt failed
                final String changedUrl = photoUrl.replace("http:", "https:");
                Log.d(TAG, "onImageLoadFailed: AAA");
                picasso.load(changedUrl)
                        .error(R.drawable.brokenimage)
                        .placeholder(R.drawable.ic_hourglass_empty_white_24dp)
                        .into(imageView);

            }
        }).build();

        Log.d(TAG, "onCreate: BBB");
        picasso.load(photoUrl)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.ic_hourglass_empty_white_24dp)
                .into(imageView);

    }

}