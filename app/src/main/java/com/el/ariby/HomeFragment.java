package com.el.ariby;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class HomeFragment extends Fragment {
    TextView te;
    ImageView ima;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container,false);
        te = v.findViewById(R.id.test);
        ima = v.findViewById(R.id.imagetest);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                String name = profile.getDisplayName();
                Uri uri=profile.getPhotoUrl();
                Glide.with(this).load(uri).into(ima);
                te.setText(name);
                ima.setImageURI(uri);
            }
        }
        return v;
    }
}
