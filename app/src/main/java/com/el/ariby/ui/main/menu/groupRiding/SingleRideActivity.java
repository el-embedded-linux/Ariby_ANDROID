package com.el.ariby.ui.main.menu.groupRiding;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;

import com.el.ariby.R;
import com.squareup.picasso.Picasso;

public class SingleRideActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        Picasso.with(this)
                .load("http://square.github.io/picasso/static/sample.png")
                .into((ImageView)findViewById(R.id.img_picasso));



    }

}
