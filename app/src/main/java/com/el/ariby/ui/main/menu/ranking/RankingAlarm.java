package com.el.ariby.ui.main.menu.ranking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RankingAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseReference ref;
        Log.d("알람","done");
        Toast.makeText(context, "알람울림", Toast.LENGTH_SHORT).show();
    }

    public int returnSit(){
        return 1;
    }

}
