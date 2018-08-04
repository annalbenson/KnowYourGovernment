package com.annabenson.knowyourgovernment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.graphics.Color.WHITE;
import static android.text.InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "MainActivity";
    //private static final int CLICK_REQUEST_CODE = 10;
    private MainActivity mainActivity = this;
    private RecyclerView recyclerView;
    private List<Official> officialList = new ArrayList<>();
    private OfficialAdapter officialAdapter;

    private TextView locationView;
    private Locator locator;

    //private List<Official> savedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setBackgroundColor( getResources().getColor( R.color.purple));

        // set up RV and Adapter (as usual)
        recyclerView = findViewById(R.id.recycler);
        officialAdapter = new OfficialAdapter(officialList, this);
        recyclerView.setAdapter(officialAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        locationView = findViewById(R.id.locationID);
        locationView.setTextColor(getResources().getColor(R.color.white));

        if(connected()) {
            locator = new Locator(this); // calls doLocationWork in this Activity
            locator.shutdown();
        } else{
            locationView.setText("No Data For Location");
            noNetDialog();
        }

    }


    @Override
    protected void onResume(){

        // load data from file
        //Object[] saved = loadData();
        //setOfficialList(saved);

        super.onResume();

    }

    @Override
    protected void onPause() {
        //nD.setNote( noteBody.getText().toString());
        // CALL SUPER
        super.onPause();
    }

    @Override
    protected void onStop(){
        //saveNote();
        // CALL SUPER
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //locator.shutdown();
        super.onDestroy();
    }

    private void saveData(){
        Log.d(TAG, "saveData: ");

        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent(("  "));

            // save normalized input header
            writer.beginObject();
            writer.name("norminput").value(locationView.getText().toString());
            writer.endObject();

            // save list items
            writer.beginArray(); // array of officials
            for(int i = 0; i < officialList.size(); i++){
                writer.beginObject();
                writer.endObject();
            }

            writer.endArray();
            
            writer.close();
            Log.d(TAG, "saveData: saved");

        } catch (Exception e){
            e.getStackTrace();
        }
    }

/*
    private Object[] loadData(){
        Log.d(TAG, "loadFile: Reloading list data upon rotate");

        try{

        }


    }
*/

    /* END OF ON_ METHODS */

    /* START OF RECYCLERVIEW METHODS */

    public void setOfficialList(Object[] results){

        if(results == null){
            locationView.setText("No Data For Location");
            officialList.clear();
        }
        else{
            locationView.setText(results[0].toString());
            officialList.clear();
            ArrayList<Official> offList = (ArrayList<Official>) results[1];
            for(int i = 0; i < offList.size(); i++){
                officialList.add( offList.get(i));
            }
        }
        officialAdapter.notifyDataSetChanged();

    }

    /* END OF RECYCLERVIEW METHODS*/

    /* START OF LOCATION METHODS */


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);

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
                        //Flow chart slide #2
                        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
                    } else {
                        Toast.makeText(this, "Location permission was denied - cannot determine address", Toast.LENGTH_LONG).show();
                        //Log.d(TAG, "onRequestPermissionsResult: NO PERM");
                    }
                }
            }
        }
        Log.d(TAG, "onRequestPermissionsResult: Exiting onRequestPermissionsResult");
    }

    public void doLocationWork(double latitude, double longitude) {

        Log.d(TAG, "doAddress: Lat: " + latitude + ", Lon: " + longitude);

        List<Address> addresses = null;
        //for (int times = 0; times < 3; times++) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                Log.d(TAG, "doAddress: Getting address now");

                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                //Log.d(TAG, "doAddress: Num addresses: " + addresses.size());
                // Get the zip code from the first returned address
                Address ad = addresses.get(0);
                //StringBuilder sb = new StringBuilder();
                String zip = ad.getPostalCode();
                // Use that zip code to create & execute the Civic Info Downloader
                new AsyncOfficialLoader(mainActivity).execute(zip);

            } catch (IOException e) {
                Log.d(TAG, "doAddress: " + e.getMessage());
                Toast.makeText(mainActivity,"Address cannot be acquired from provided latitude/longitude", Toast.LENGTH_SHORT).show();

            }
            //Toast.makeText(this, "GeoCoder service is slow - please wait", Toast.LENGTH_SHORT).show();
        //}
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
        // Add extra w/ heading
        intent.putExtra("header", locationView.getText().toString() );
        // Add extra w/ Official object
        Bundle bundle = new Bundle();
        bundle.putSerializable("official", o);
        intent.putExtras(bundle); // Extra"s" because passing a bundle

        // start the activity
        startActivity(intent);

    }
    @Override
    public boolean onLongClick(View v){
        //Toast.makeText(this, "Long Clicked", Toast.LENGTH_SHORT).show();
        int pos = recyclerView.getChildLayoutPosition(v);
        onClick(v);

        return false;
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CLICK_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                locationView.setText(data.getStringExtra("header"));
            }
        }
    }
    */

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
                if(connected()){
                    searchDialog();
                }
                else{
                    noNetDialog();
                }
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

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "onClick: Cancel clicked, do nothing");


            }
        });

        builder.setMessage("Enter a City, State, or Zip Code:");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void noNetDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Data cannot be accessed/loaded without an internet connection.");
        builder.setTitle("No Network Connection");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /* END OF DIALOGS */

    private boolean connected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}