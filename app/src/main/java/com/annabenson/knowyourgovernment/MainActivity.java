package com.annabenson.knowyourgovernment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.graphics.Color.WHITE;
import static android.text.InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "MainActivity";
    private MainActivity mainActivity = this;
    private RecyclerView recyclerView;
    private List<Official> officialList = new ArrayList<>();
    private OfficialAdapter officialAdapter;

    private TextView locationText;
    private Locator locator;


    // Normalized Input Fields
    private String displayCity = "";
    private String displayState = "";
    private String displayZip = "";

    // initial comment 3/19
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setBackgroundColor( getResources().getColor( R.color.purple));

        recyclerView = findViewById(R.id.recycler);
        officialAdapter = new OfficialAdapter(officialList, this);
        recyclerView.setAdapter(officialAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        locationText = findViewById(R.id.locationDisplay);
        locationText.setTextColor(getResources().getColor( R.color.white));
        locator = new Locator(this);
        //locationText.setText("OK");


        /*
        for( int i = 0; i < 5; i++){
            Log.d(TAG, "onCreate: Creating dummy official " + i);
            //Official o = new Official("Name" + i, "Office" + i, "Party" + i);
            //officialList.add(o);
        }
        */


        //Any text data not supplied use "No Data Provided"
        // Except, party, then the default is "Unknown"
        /*
        Official xy = new Official("John Doe", "County Clerk", "Democratic", "1234 Main St", "123-555-4567", "www.nope.com", "jdoe.gmail.com", "photo.com", "gplus", "facebook", "tweet", "yt" );
        Official xx = new Official("Jane Doe", "Comptroller", "Republican", "No Data Provided", "123-555-4567", "www.nope.com", "jdoe.gmail.com", "photo.com", "gplus", "facebook", "tweet", "yt" );
        Official unknowns = new Official(
                "Anna Benson",
                "Senator",
                "Unknown",
                "3015 S Union",
                "No Data Provided",
                "www.anna.com",
                "alb.gmail.com",
                "photo.com",
                "gplus.anna",
                "fbook.anna",
                "tweet.alb",
                "No Data Provided"
        );
        officialList.add(xy);
        officialList.add(xx);
        officialList.add(unknowns);

        officialAdapter.notifyDataSetChanged();
        */


        /// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        //new AsyncOfficialLoader(mainActivity).execute("60616");
        /// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    }

    @Override
    protected void onResume(){

        Log.d(TAG, "onResume: display vars: " + displayCity + " " + displayState + " " + displayZip);
        setLocatonDisplay(displayCity,displayState,displayZip);
        new AsyncOfficialLoader(mainActivity).execute(displayZip);

        /*
        if(locator != null){
            locator.shutdown();
        }
        */

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        //locator.shutdown();
        super.onDestroy();
    }


    /* END OF ON_ METHODS */


    /* START OF RECYCLERVIEW METHODS */

    public void addOfficials(ArrayList<Official> offList){

        officialList.clear();
        if(offList != null){

            for (int i = 0; i < offList.size(); i++){
                officialList.add(offList.get(i));
            }
            officialAdapter.notifyDataSetChanged();
        }
        else{
            Log.d(TAG, "addOfficials: null offList");
        }

    }

    /* END OF RECYCLERVIEW METHODS*/

    /* START OF LOCATION METHODS */


    public void setData(double lat, double lon){
        // gets the results of Locator
        doAddress(lat,lon);
        //no return
        //locationText.setText(address);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Log.d(TAG, "onRequestPermissionsResult: CALL: " + permissions.length);
        //Log.d(TAG, "onRequestPermissionsResult: PERM RESULT RECEIVED");

        if (requestCode == 5) {
            Log.d(TAG, "onRequestPermissionsResult: permissions.length: " + permissions.length);
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        //Log.d(TAG, "onRequestPermissionsResult: HAS PERM");
                        locator.setUpLocationManager();
                        locator.determineLocation();
                    } else {
                        Toast.makeText(this, "Location permission was denied - cannot determine address", Toast.LENGTH_LONG).show();
                        //Log.d(TAG, "onRequestPermissionsResult: NO PERM");
                    }
                }
            }
        }
        Log.d(TAG, "onRequestPermissionsResult: Exiting onRequestPermissionsResult");
    }

    public void setLocatonDisplay(String city, String state, String zip){
        Log.d(TAG, "setLocatonDisplay: arg: " + city + " " + state + " " + zip);
        displayCity = city;
        displayState = state;
        displayZip = zip;
        String s = String.format("%s, %s %s", displayCity, displayState, displayZip);
        locationText.setText(s);
    }

    private void doAddress(double latitude, double longitude) {

        //Log.d(TAG, "doAddress: Lat: " + latitude + ", Lon: " + longitude);

        List<Address> addresses = null;
        for (int times = 0; times < 3; times++) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                //Log.d(TAG, "doAddress: Getting address now");

                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                //Log.d(TAG, "doAddress: Num addresses: " + addresses.size());

                //StringBuilder sb = new StringBuilder();

                for (Address ad : addresses) {
                    //Log.d(TAG, "X: doLocation: " + ad);

                    //Log.d(TAG, "doAddress: ZZZ" + ad.getAddressLine(0));
                    String addressLine = ad.getAddressLine(0);
                    String [] addressArray = addressLine.split(",");

                    /*for( String s : addressArray ){Log.d(TAG, "doAddress: s " + s);}*/
                    String city = addressArray[1].trim();
                    String [] sZip = addressArray[2].trim().split(" ");
                    String state = sZip[0];
                    String zip = sZip[1];
                    //String stateZip = (addressArray[2]).trim();
                    //int idx = stateZip.indexOf(" "); // space in between State and ZIP
                    //currentZip = stateZip.substring(idx,stateZip.length()).trim();
                    //Log.d(TAG, "doAddress: currentZip: " + currentZip);

                    Log.d(TAG, "doAddress: setting location display " + city + ", " + state + " " + zip);
                    setLocatonDisplay(city,state,zip);

                    //sb.append(ad.getAddressLine(0));
                    //sb.append("\nAddress\n\n");
                    /*
                    for (int i = 0; i < ad.getMaxAddressLineIndex(); i++) {
                        //Log.d(TAG, "doAddress: AAA");
                        //Log.d(TAG, "doAddress: QQQ" + ad.getAddressLine(i));
                        sb.append(ad.getAddressLine(i));
                    }
                    */
                    //sb.append("\t" + ad.getCountryName() + " (" + ad.getCountryCode() + ")\n");
                }

                //return sb.toString();
            } catch (IOException e) {
                Log.d(TAG, "doAddress: " + e.getMessage());

            }
            //Toast.makeText(this, "GeoCoder service is slow - please wait", Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(this, "GeoCoder service timed out - please try again", Toast.LENGTH_LONG).show();
        //return null;
    }



    public void noLocationAvailable() {
        //Toast.makeText(this, "No location providers were available", Toast.LENGTH_LONG).show();
    }


    /* END OF LOCATION METHODS */

    /* START OF CLICK METHODS */

    @Override
    public void onClick(View v){
        //Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();

        // xyz

        Intent intent = new Intent(MainActivity.this, OfficialActivity.class);
        // get official
        int pos = recyclerView.getChildLayoutPosition(v);
        Official o = officialList.get(pos);
        Bundle bundle = new Bundle();
        bundle.putSerializable("official", o);
        intent.putExtras(bundle); // Extra"s" because passing a bundle
        intent.putExtra("city", displayCity);
        intent.putExtra("state", displayState);
        intent.putExtra("zip", displayZip);

        startActivity(intent);
    }
    @Override
    public boolean onLongClick(View v){
        //Toast.makeText(this, "Long Clicked", Toast.LENGTH_SHORT).show();
        int pos = recyclerView.getChildLayoutPosition(v);

        return false;
    }

    /* END OF CLICK METHODS */

    /* START OF MENU METHODS */

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.location:
                Log.d(TAG, "onOptionsItemSelected: search clicked");
                /*
                if(locator.getLocationListener() != null ){
                    Log.d(TAG, "onOptionsItemSelected: loc listener not null, shutting down");
                    locator.shutdown();    
                }
                else{
                    Log.d(TAG, "onOptionsItemSelected: null, loc listener already shut down");
                }
                */
                searchDialog();
                return true;
            case R.id.about:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* END OF MENU METHODS */

    /* START OF DIALOGS */

    public void searchDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Log.d(TAG, "searchDialog: started");
        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT );
        et.setGravity(Gravity.CENTER_HORIZONTAL);

        builder.setView(et);
        //builder.setIcon(R.drawable.ic_launcher_background)

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String input = et.getText().toString();
                new AsyncOfficialLoader(mainActivity).execute(input);
                //TextView tv = findViewById(R.id.locationDisplay);
                //tv.setText(currentZip); // also need state and city
                //Toast.makeText(mainActivity,"OK clicked", Toast.LENGTH_SHORT);
                // do
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "onClick: Cancel clicked, do nothing");
                //Toast.makeText(mainActivity,"Cancel clicked", Toast.LENGTH_SHORT);
                //TextView tv = findViewById(R.id.locationDisplay);
                //tv.setText("CANCEL");

            }
        });

        builder.setMessage("Enter a City, State, or Zip Code:");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /* END OF DIALOGS */


}
