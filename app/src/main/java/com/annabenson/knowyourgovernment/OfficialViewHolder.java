package com.annabenson.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Anna on 3/19/2018.
 */

public class OfficialViewHolder extends RecyclerView.ViewHolder {

    public TextView name;

    public OfficialViewHolder(View view){
        super(view);
        name = view.findViewById(R.id.nameID);
    }

    // will need more fields but is start 3/19
}
