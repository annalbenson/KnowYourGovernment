package com.annabenson.knowyourgovernment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Anna on 3/22/2018.
 */

public class OfficialActivity extends AppCompatActivity {

    //SET PARENT ACTIVITY IN THE MANIFEST

    public static final String TAG  = "OfficialActivity";
    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        // Connect all layout views to variables
        textView = findViewById(R.id.textView2);

        // Get passed Official object
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        Official official = (Official) bundle.getSerializable("official");

        // Populate those variables
        String name = official.getName();
        textView.setText(name);





    }

}