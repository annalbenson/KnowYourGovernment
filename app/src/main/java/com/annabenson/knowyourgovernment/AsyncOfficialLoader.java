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

    public AsyncOfficialLoader(MainActivity ma){ mainActivity = ma;}

    @Override
    protected void onPreExecute(){
        Toast.makeText(mainActivity, "Loading Official Data", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String s){
        ArrayList<Official> officialList = parseJSON(s);
        // call main activity methods as appropriate
    }

    @Override
    protected String doInBackground(String... params){

        params[0] = "60616"; // for testing
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


            // parse normalizedInput

            // parse offices

            // parse officials
            return officialList;
        }catch(Exception e){
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }




}
