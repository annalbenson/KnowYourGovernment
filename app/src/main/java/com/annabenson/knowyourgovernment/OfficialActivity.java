package com.annabenson.knowyourgovernment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Anna on 3/22/2018.
 */

public class OfficialActivity extends AppCompatActivity {

    //SET PARENT ACTIVITY IN THE MANIFEST

    public static final String TAG  = "OfficialActivity";
    public TextView locationView;
    private Locator locator;

    public TextView officeView;
    public TextView nameView;
    public TextView partyView;
    public ImageView imageView;
    public TextView addressView;
    public TextView phoneView;
    public TextView emailView;
    public TextView websiteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        // Connect all layout views to variables
        locationView = findViewById(R.id.locationID);
        officeView = findViewById(R.id.officeID);
        nameView = findViewById(R.id.nameID);
        partyView = findViewById(R.id.partyID);
        imageView = findViewById(R.id.imageID);
        addressView = findViewById(R.id.addressID);
        phoneView = findViewById(R.id.phoneID);
        emailView = findViewById(R.id.emailID);
        websiteView = findViewById(R.id.websiteID);

        // Get passed Official object
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        Official official = (Official) bundle.getSerializable("official");

        // Populate those variables
        String name = official.getName();
        nameView.setText(name);


        // how do?
        //locator = new Locator();


    }

}