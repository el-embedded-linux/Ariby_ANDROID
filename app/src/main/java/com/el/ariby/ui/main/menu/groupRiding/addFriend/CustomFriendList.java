package com.el.ariby.ui.main.menu.groupRiding.addFriend;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.el.ariby.R;

public class CustomFriendList extends LinearLayout {

    LinearLayout custom_friend;
    TextView nickname;
    ImageView profile;

    public CustomFriendList(Context context) {
        super(context);
    }

    public CustomFriendList(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

   private void init(Context context){
       LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       inflater.inflate(R.layout.custom_add_friend_to_group, this, true);
       custom_friend = findViewById(R.id.lin_list);
       nickname = findViewById(R.id.txt_friend_nick);
       profile = findViewById(R.id.img_Prof);
   }

}
