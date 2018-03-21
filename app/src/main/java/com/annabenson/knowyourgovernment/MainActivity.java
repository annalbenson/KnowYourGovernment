package com.annabenson.knowyourgovernment;

import android.content.DialogInterface;
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

import java.util.ArrayList;
import java.util.List;

import static android.text.InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;

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
        officialAdapter = new OfficialAdapter(officalList, this);
        recyclerView.setAdapter(officialAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        for( int i = 0; i < 5; i++){
            Log.d(TAG, "onCreate: Creating dummy official " + i);
            Official o = new Official("Name" + i, "Office" + i, "Party" + i);
            officalList.add(o);
        }

        officialAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v){
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onLongClick(View v){
        Toast.makeText(this, "Long Clicked", Toast.LENGTH_SHORT).show();
        int pos = recyclerView.getChildLayoutPosition(v);

        return false;
    }

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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
}
