package com.annabenson.knowyourgovernment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Anna on 3/20/2018.
 */

public class AboutActivity extends AppCompatActivity {

    //SET PARENT ACTIVITY IN THE MANIFEST

    private static final String TAG = "About_Activity";

    private TextView titleView;
    private TextView copyrightView;
    private TextView versionView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        titleView = findViewById(R.id.titleID);
        copyrightView = findViewById(R.id.copyrightID);
        versionView = findViewById(R.id.versionID);

        titleView.setText("Know Your Government");
        copyrightView.setText("© 2018, Anna L. Benson");
        versionView.setText("Version 1.0");
    }
}
