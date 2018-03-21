package com.annabenson.knowyourgovernment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Anna on 3/20/2018.
 */

public class AboutActivity extends AppCompatActivity {

    //SET PARENT ACTIVITY IN THE MANIFEST

    private static final String TAG = "About_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


    }
}
