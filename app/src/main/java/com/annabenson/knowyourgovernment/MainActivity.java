package com.annabenson.knowyourgovernment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "MainActivity";
    private MainActivity mainActivity = this;
    private RecyclerView recyclerView;
    private List<Official> officalList = new ArrayList<>();
    private OfficialAdapter officialAdapter;

    // initial comment 3/19
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setAdapter(officialAdapter);

    }

    @Override
    public void onClick(View v){

    }
    @Override
    public boolean onLongClick(View v){
        int pos = recyclerView.getChildLayoutPosition(v);

        return false;
    }
}
