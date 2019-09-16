package com.el.ariby.ui.main.menu.ridingRecord;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.el.ariby.R;

public class CustomRidingrecord extends LinearLayout {

    CardView cardView;
    TextView date;
    TextView ridingtime;
    TextView ridingkm;
    TextView ridingkcal;

    public CustomRidingrecord(Context context) {
        super(context);
    }

    public CustomRidingrecord(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_riding_record_item, this, true);
        cardView = findViewById(R.id.cardview);
        date = findViewById(R.id.date);
        ridingtime = findViewById(R.id.riding_time);
        ridingkm = findViewById(R.id.riding_km);
        ridingkcal = findViewById(R.id.riding_kcal);
    }
}
