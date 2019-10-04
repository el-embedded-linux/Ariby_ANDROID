package com.el.ariby.ui.main.menu.recommend;

import android.content.Context;
import androidx.cardview.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.el.ariby.R;

public class CustomRecommend extends LinearLayout {

    CardView cardview;
    TextView title;
    TextView kmtext;
    TextView rating;
    ImageView image;

    public CustomRecommend(Context context) {
        super(context);
    }

    public CustomRecommend(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_recommend_item, this, true);
        cardview = findViewById(R.id.cardview);
        title = findViewById(R.id.title);
        kmtext = findViewById(R.id.kmText);
        rating = findViewById(R.id.rating);
        image = findViewById(R.id.image);
    }
}
