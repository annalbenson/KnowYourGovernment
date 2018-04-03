package com.annabenson.knowyourgovernment;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Anna on 3/19/2018.
 */

public class OfficialViewHolder extends RecyclerView.ViewHolder {

    public TextView office;
    public TextView name; // party also displayed
    //private CardView cv;

    public OfficialViewHolder(View view){
        super(view);
        //cv = view.findViewById(R.id.cardView);
        office = view.findViewById(R.id.officeID);
        name = view.findViewById(R.id.nameID);

        //cv.setPreventCornerOverlap(false);
        //cv.setRadius(1f);
    }

}
