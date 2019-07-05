package com.el.ariby.ui.main.menu.groupRiding;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.el.ariby.R;

import org.w3c.dom.Text;


public class CustomGroupRide extends LinearLayout {
    CardView custom_groupRide;
    TextView groupName;
    TextView start;
    TextView end;
    TextView location;

    public CustomGroupRide(Context context) {
        super(context);
    }

    public CustomGroupRide(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_group_ride, this, true);
        custom_groupRide = findViewById(R.id.custom_group_ride);
        groupName = findViewById(R.id.txt_loc);
        start = findViewById(R.id.txt_departure);
        end = findViewById(R.id.txt_destination);
        location = findViewById(R.id.txt_loc);
    }

}
