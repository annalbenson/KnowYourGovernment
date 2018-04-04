package com.annabenson.knowyourgovernment;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Anna on 3/27/2018.
 */

public class AsyncOfficialLoader extends AsyncTask<String, Void, String> {

    private MainActivity mainActivity;

    private static final String TAG = "AsyncOfficialLoader";
    private static final String KEY = "AIzaSyCM_InZvyfdcq9ehC6TFysILPNdFvIR6CE";
    private final String dataURLStem = "https://www.googleapis.com/civicinfo/v2/representatives?key="+ KEY +"&address=";

    public static final String NO_DATA = "No Data Provided";
    public static final String UNKNOWN = "Unknown";

    // normalized input fields
    private String city;
    private String state;
    private String zip;

    //private boolean fileFound = true;


    public AsyncOfficialLoader(MainActivity ma){ mainActivity = ma;}

    @Override
    protected void onPreExecute(){
        //Toast.makeText(mainActivity, "Loading Official Data", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String s){
        Log.d(TAG, "onPostExecute: In post execute");

        if(s == null){
            Toast.makeText(mainActivity,"Civic Info service is unavailable",Toast.LENGTH_SHORT).show();
            mainActivity.setOfficialList(null);
            return;
        }
        if(s.isEmpty()){
            Toast.makeText(mainActivity,"No data is available for the specified location",Toast.LENGTH_SHORT).show();;
            mainActivity.setOfficialList(null);
            return;
        }

        ArrayList<Official> officialList = parseJSON(s);
        Object [] results = new Object[2];
        results[0] = city + ", " + state + " " + zip;
        results[1] = officialList;
        mainActivity.setOfficialList(results);
        return;

    }

    @Override
    protected String doInBackground(String... params){

        //1) Create URL for Google Civic Info REST service using your API key and the zip code

        String dataURL = dataURLStem + params[0];
        //Log.d(TAG, "doInBackground: URL is " + dataURL);
        Uri dataUri = Uri.parse(dataURL);
        String urlToUse = dataUri.toString();
        //Log.d(TAG, "doInBackground: " + urlToUse);

        //2) Connect to REST service
        //- Response code 400 -> return empty string ???
        //+ Exception -> Log an error message
        //-

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            //Log.d(TAG, "doInBackground: A");
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            //Log.d(TAG, "doInBackground: " + sb.toString());

        }
        catch (FileNotFoundException e){
            //fileFound = false;
            return null;
        }
        catch (Exception e) {
            //Exception -> Log an error message
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        //Log.d(TAG, "doInBackground: " + sb.toString());
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
            city = normalizedInput.getString("city");
            state = normalizedInput.getString("state");
            zip = normalizedInput.getString("zip");

            //Log.d(TAG, "parseJSON: city, state, zip -> " + city + ", " + state + ", " + zip);
            // will be sent to MainActivity by postExecute

            // parse offices
            
            for(int i = 0;i < offices.length(); i++){
                JSONObject obj = offices.getJSONObject(i);
                String officeName = obj.getString("name");
                String officialIndices = obj.getString("officialIndices");

                //Log.d(TAG, "parseJSON: officialIndices as String: " + officialIndices);

                //Log.d(TAG, "parseJSON: Office Name: " + officeName);
                //Log.d(TAG, "parseJSON: Official Indices: " + officialIndices);

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

                    //Log.d(TAG, "parseJSON: indices[j] -> " + indices[j]);
                    //Log.d(TAG, "parseJSON: person's name -> " + name);


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
                    //Log.d(TAG, "parseJSON: address? " + address);


                    String party = (innerObj.has("party") ? innerObj.getString("party") : UNKNOWN );
                    //Log.d(TAG, "parseJSON: party: " + party);

                    String phones = ( innerObj.has("phones") ? innerObj.getJSONArray("phones").getString(0) : NO_DATA );
                    //Log.d(TAG, "parseJSON: phone number: " + phones);

                    String urls = ( innerObj.has("urls") ? innerObj.getJSONArray("urls").getString(0) : NO_DATA );
                    //Log.d(TAG, "parseJSON: urls: " + urls);

                    String emails = (innerObj.has("emails") ? innerObj.getJSONArray("emails").getString(0) : NO_DATA );
                    //Log.d(TAG, "parseJSON: emails: " + emails);

                    String photoURL = (innerObj.has("photoUrl") ? innerObj.getString("photoUrl") : NO_DATA);
                    //Log.d(TAG, "parseJSON: photoUrl: " + photoURL);

                    //String googleplus = (innerObj.getJSONArray("channels").getJSONObject(0) ? : );

                    JSONArray channels = ( innerObj.has("channels") ? innerObj.getJSONArray("channels") : null );
                    String googleplus = ""; String facebook = ""; String twitter = ""; String youtube = "";

                    if(channels != null){
                        for(int k = 0; k < channels.length(); k++ ){
                            String type = channels.getJSONObject(k).getString("type");
                            //Log.d(TAG, "parseJSON: type at index " + k + " is " + type );
                            switch (type){
                                case "GooglePlus":
                                    googleplus = channels.getJSONObject(k).getString("id");
                                    break;
                                case "Facebook":
                                    facebook = channels.getJSONObject(k).getString("id");
                                    break;
                                case "Twitter":
                                    twitter = channels.getJSONObject(k).getString("id");
                                    break;
                                case "YouTube":
                                    youtube = channels.getJSONObject(k).getString("id");
                                    break;
                                default:
                                    //Log.d(TAG, "parseJSON: non recognized social media");
                                    break;
                            }
                        }
                    }
                    else{ // is null
                        googleplus = NO_DATA; facebook = NO_DATA;
                        twitter = NO_DATA; youtube = NO_DATA;
                    }

                    //Log.d(TAG, "parseJSON: GooglePlus? " + googleplus);
                    //Log.d(TAG, "parseJSON: Facebook? " + facebook);
                    //Log.d(TAG, "parseJSON: Twitter? " + twitter);
                    //Log.d(TAG, "parseJSON: YouTube? " + youtube);


                    /*DONE PARSING*/

                    // add official
                    Official o = new Official(name, officeName, party,
                            address, phones, urls, emails, photoURL,
                            googleplus, facebook, twitter, youtube);
                    officialList.add(o);
                } // end of j for loop
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