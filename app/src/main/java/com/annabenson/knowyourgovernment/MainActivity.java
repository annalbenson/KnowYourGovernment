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
        new AsyncOfficialLoader(mainActivity).execute("60616");
        /// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    }

    @Override
    protected void onDestroy() {
        locator.shutdown();
        super.onDestroy();
    }


    /* END OF ON METHODS */


    /* START OF RECYCLERVIEW METHODS */

    public void addOfficials(ArrayList<Official> offList){

        if(offList.size() == 0){
            Log.d(TAG, "addOfficials: empty list, no officials to add");
        }
        else {
            for (int i = 0; i < offList.size(); i++){
                officialList.add(offList.get(i));
            }
        }
        officialAdapter.notifyDataSetChanged();
    }

    /* END OF RECYCLERVIEW METHODS*/

    /* START OF LOCATION METHODS */


    public void setData(double lat, double lon){
        // gets the results of Locator

        String address = doAddress(lat,lon);
        locationText.setText(address);
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

    private String doAddress(double latitude, double longitude) {

        //Log.d(TAG, "doAddress: Lat: " + latitude + ", Lon: " + longitude);



        List<Address> addresses = null;
        for (int times = 0; times < 3; times++) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                //Log.d(TAG, "doAddress: Getting address now");


                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                //Log.d(TAG, "doAddress: Num addresses: " + addresses.size());

                StringBuilder sb = new StringBuilder();

                for (Address ad : addresses) {
                  //  Log.d(TAG, "doLocation: " + ad);

                    //sb.append("\nAddress\n\n");
                    /*
                    Log.d(TAG, "doAddress: " + ad.getMaxAddressLineIndex());
                    for (int i = 0; i <= ad.getMaxAddressLineIndex(); i++)
                        sb.append("\t" + ad.getAddressLine(i) + "\t");

                    */
                    //sb.append("\t" + ad.getCountryName() + " (" + ad.getCountryCode() + ")\n");

                    String s = ad.getAddressLine(0);
                    int idx = s.indexOf(',');
                    s = s.substring(idx + 2, s.length());
                    //int idx2 = s.length() - s.indexOf(',');
                    //s = s.substring(0,s.length() - idx2);
                    sb.append(s);
                }

                return sb.toString();
            } catch (IOException e) {
                Log.d(TAG, "doAddress: " + e.getMessage());

            }
            //Toast.makeText(this, "GeoCoder service is slow - please wait", Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(this, "GeoCoder service timed out - please try again", Toast.LENGTH_LONG).show();
        return null;
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
                String s = et.getText().toString();
                TextView tv = findViewById(R.id.locationDisplay);
                tv.setText("OK");
                //Toast.makeText(mainActivity,"OK clicked", Toast.LENGTH_SHORT);
                // do
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(mainActivity,"Cancel clicked", Toast.LENGTH_SHORT);
                TextView tv = findViewById(R.id.locationDisplay);
                tv.setText("CANCEL");

            }
        });

        builder.setMessage("Enter a City, State, or Zip Code:");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /* END OF DIALOGS */


}
