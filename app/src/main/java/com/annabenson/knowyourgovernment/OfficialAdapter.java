package com.annabenson.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Anna on 3/19/2018.
 */

public class OfficialAdapter extends RecyclerView.Adapter<MyViewHolder> {

    public static final String TAG = "OfficialAdapter";
    private List<Official> officialList;
    private MainActivity mainActivity;

    public OfficialAdapter(List<Official> offList, MainActivity ma){
        this.officialList = offList;
        mainActivity = ma;
    }

    @Override
    public OfficialAdapter onCreateViewHolder(final ViewGroup parent, int viewType){
        Log.d(TAG, "onCreateViewHolder: Making New");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.official_list_row, parent, false);
        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);

        return new OfficialViewHolder(itemView);

    }

    @Override
    public void onBindOfficialHolder(OfficialViewHolder holder, int position){
        Official official = officialList.get(position);
        official.name.setText(official.getName());

    }
}
