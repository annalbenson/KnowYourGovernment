package com.annabenson.knowyourgovernment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Anna on 3/22/2018.
 */

public class OfficialActivity extends AppCompatActivity {

    //SET PARENT ACTIVITY IN THE MANIFEST

    public static final String TAG  = "OfficialActivity";
    public static final String NO_DATA = "No Data Provided";
    public static final String UNKNOWN = "Unknown";
    public static final String DEM = "Democratic";
    public static final String GOP = "Republican";

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

    public TextView addressLabel;
    public TextView phoneLabel;
    public TextView emailLabel;
    public TextView websiteLabel;

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

        this.addressLabel = findViewById(R.id.addressLabel);
        this.phoneLabel = findViewById(R.id.phoneLabel);
        this.emailLabel = findViewById(R.id.emailLabel);
        this.websiteLabel = findViewById(R.id.websiteLabel);

        // Get passed Official object
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        Official official = (Official) bundle.getSerializable("official");

        // Populate those variables
        if( official.getOffice().equals(NO_DATA)){ hideView(officeView);}
        else{officeView.setText(official.getOffice());}
        if( official.getName().equals(NO_DATA)){ hideView(nameView);}
        else{nameView.setText(official.getName());}
        if( official.getParty().equals(UNKNOWN)){ hideView(partyView); /* set background to black */}
        else{
            partyView.setText("(" + official.getParty() + ")");
            if(official.getParty().equals(DEM)){
                // set background to blue
            }
            if(official.getParty().equals(GOP)){
                // set background to red
            }
        }

        imageView.setImageResource(R.drawable.ic_hourglass_empty_white_24dp);

        if ( official.getPhotoUrl().equals(NO_DATA)) { /*imageView.setImage to generic*/}
        else{

        }


        /* loading image
        1) not async, in example done in MainActivity



        */



        //loadImage();

        //imageView.setText();

        if( official.getAddress().equals(NO_DATA)){hideView(addressView); hideView(addressLabel);}
        else{addressView.setText(official.getAddress());}

        if( official.getPhone().equals(NO_DATA)){hideView(phoneView); hideView(phoneLabel);}
        else{phoneView.setText(official.getPhone());}

        if( official.getEmail().equals(NO_DATA)){ hideView(emailView); hideView(emailLabel);}
        else{ emailView.setText(official.getEmail());}

        if( official.getUrl().equals(NO_DATA)){hideView(websiteView); hideView(websiteLabel);}
        else{ websiteView.setText(official.getUrl());}



        // how do?
        //locator = new Locator();


    }

    private static void hideView(View v){
        v.setVisibility(View.GONE);
    }

    private static void loadImage(View v, String Url){

    }

}