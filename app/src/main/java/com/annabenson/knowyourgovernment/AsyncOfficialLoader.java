package com.annabenson.knowyourgovernment;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Anna on 3/27/2018.
 */

public class AsyncOfficialLoader extends AsyncTask<String, Integer, String> {

    private MainActivity mainActivity;

    private static final String TAG = "AsyncOfficialLoader";
    private static final String KEY = "AIzaSyCM_InZvyfdcq9ehC6TFysILPNdFvIR6CE";
    private final String dataURLStem = "https://www.googleapis.com/civicinfo/v2/representatives?key="+ KEY +"&address=";

    public static final String NO_DATA = "No Data Provided";
    public static final String UNKNOWN = "Unknown";


    public AsyncOfficialLoader(MainActivity ma){ mainActivity = ma;}

    @Override
    protected void onPreExecute(){
        Toast.makeText(mainActivity, "Loading Official Data", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String s){
        ArrayList<Official> officialList = parseJSON(s);
        Log.d(TAG, "onPostExecute: In post execute");
        // call main activity methods as appropriate
    }

    @Override
    protected String doInBackground(String... params){

        String dataURL = dataURLStem + params[0];
        Log.d(TAG, "doInBackground: URL is " + dataURL);
        Uri dataUri = Uri.parse(dataURL);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            Log.d(TAG, "doInBackground: A");
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        Log.d(TAG, "doInBackground: " + sb.toString());
        Log.d(TAG, "doInBackground: returning");
        return sb.toString();

    }

    private ArrayList<Official> parseJSON(String s){
        Log.d(TAG, "parseJSON: started JSON");

        ArrayList<Official> officialList = new ArrayList<>();
        try{
            JSONObject wholeThing = new JSONObject(s);
            JSONObject normalizedInput = wholeThing.getJSONObject("normalizedInput");
            JSONArray offices = wholeThing.getJSONArray("offices");
            JSONArray officials = wholeThing.getJSONArray("officials");


            // parse normalizedInput, this is for location display in our activities
            String city = normalizedInput.getString("city");
            String state = normalizedInput.getString("state");
            String zip = normalizedInput.getString("zip");

            Log.d(TAG, "parseJSON: city, state, zip -> " + city + ", " + state + ", " + zip);
            // need somewhere to put this information
            // parse offices
            
            for(int i = 0;i < offices.length(); i++){
                JSONObject obj = offices.getJSONObject(i);
                String officeName = obj.getString("name");
                String officialIndices = obj.getString("officialIndices");

                //Log.d(TAG, "parseJSON: officialIndices as String: " + officialIndices);

                Log.d(TAG, "parseJSON: Office Name: " + officeName);
                Log.d(TAG, "parseJSON: Official Indices: " + officialIndices);

                // turn officialndices into int array

                //1) slice so no []
                String temp = officialIndices.substring(1,officialIndices.length()-1);
                String [] temp2 = temp.split(",");
                int [] indices = new int [temp2.length];
                for(int j = 0; j < temp2.length; j++){
                    indices[j] = Integer.parseInt(temp2[j]);
                }

                // now have indices, an int array of index data
                // need to extract lots of data from officials array


                for(int j = 0; j < indices.length; j++ ){
                    JSONObject innerObj = officials.getJSONObject(indices[j]);
                    String name = innerObj.getString("name");

                    Log.d(TAG, "parseJSON: indices[j] -> " + indices[j]);
                    Log.d(TAG, "parseJSON: name -> " + name);


                    String address = "";
                    if(! innerObj.has("address")){
                        address = NO_DATA;
                    }
                    else {
                        JSONArray addressArray = innerObj.getJSONArray("address");
                        JSONObject addressObject = addressArray.getJSONObject(0);
                        // works ^^^^^^

                        if (addressObject.has("line1")) {
                            address += addressObject.getString("line1") + "\n";
                            //Log.d(TAG, "parseJSON: address currently is " + address);
                        }
                        if (addressObject.has("line2")) {
                            address += addressObject.getString("line2") + "\n";
                            //Log.d(TAG, "parseJSON: address currently is " + address);
                        }
                        if (addressObject.has("city")) {
                            address += addressObject.getString("city") + " ";
                            //Log.d(TAG, "parseJSON: address currently is " + address);
                        }
                        if (addressObject.has("state")) {
                            address += addressObject.getString("state") + ", ";
                            //Log.d(TAG, "parseJSON: address currently is " + address);
                        }
                        if (addressObject.has("zip")) {
                            address += addressObject.getString("zip");
                            //Log.d(TAG, "parseJSON: address currently is " + address);
                        }

                        // Carolyn J. Gallagher has no value for address
                    }
                    Log.d(TAG, "parseJSON: address? " + address);


                    String party = (innerObj.has("party") ? innerObj.getString("party") : UNKNOWN );
                    Log.d(TAG, "parseJSON: party: " + party);

                    String phones = ( innerObj.has("phones") ? innerObj.getJSONArray("phones").getString(0) : NO_DATA );
                    Log.d(TAG, "parseJSON: phone number: " + phones);

                    String urls = ( innerObj.has("urls") ? innerObj.getJSONArray("urls").getString(0): NO_DATA );
                    Log.d(TAG, "parseJSON: urls: " + urls);

                    // need to check for address line fields
                    // throws if no mapping exists
                    //JSONArray addressArray = innerObj.getJSONArray("address");
                    //JSONObject addressObject = addressArray.getJSONObject(0);

                    //JSONObject addressObject = innerObj.getJSONObject("address");

                    //String x = innerObj.getString("address");
                    //Log.d(TAG, "parseJSON: address x -> " + x);

/*
                    String address = "";
                    if(addressObject.has("line1")){
                        address += innerObj.getString("line1") + "\n";
                    }
                    if(addressObject.has("line2")){
                        address += innerObj.getString("line2") + "\n";
                    }
                    if (addressObject.has("city") && obj.has("state") && obj.has("zip")) {
                        address += innerObj.get("city") + obj.getString("state") + obj.getString("zip");

                    }

                    Log.d(TAG, "parseJSON: Address is: " + address);
*/
                    /*
                    String party;
                    if(obj.has("party")){
                        party = obj.getString("party");
                    }
                    else{
                        party = UNKNOWN;
                    }

                    if(obj.has("phones")){

                        // want only first entry, parse array?
                        //String temp4 = obj.getString("phones");
                    }

                    */

                    //officialList.add(new Official(       ));

                } // end of j for loop


                //JSONObject obj2 = officials.getJSONObject()
            } // end of i for loop

            return officialList;
            // end of try block
        }catch(Exception e){
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}