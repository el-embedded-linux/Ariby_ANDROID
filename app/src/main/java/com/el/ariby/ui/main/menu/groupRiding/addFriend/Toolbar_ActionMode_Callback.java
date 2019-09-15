package com.el.ariby.ui.main.menu.groupRiding.addFriend;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.el.ariby.R;
import com.el.ariby.ui.main.menu.groupRiding.AddFriendActivity;

import java.util.ArrayList;

public class Toolbar_ActionMode_Callback implements ActionMode.Callback {
    public Toolbar_ActionMode_Callback(Context context, FriendListRecyclerAdapter recyclerAdapter, ArrayList<FriendListItem> items) {
        this.context = context;
        this.recyclerAdapter = recyclerAdapter;
        this.items = items;
    }

    private Context context;
    private FriendListRecyclerAdapter recyclerAdapter;
    private ArrayList<FriendListItem> items;


    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.group_menu_main, menu); //Inflate the menu over action mode
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        //Sometimes the menu will not be visible so for that we need to set their visibility manually in this method
        //So here show action menu according to SDK Levels
        if (Build.VERSION.SDK_INT < 11) {
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_invite), MenuItemCompat.SHOW_AS_ACTION_NEVER);
        } else {
            menu.findItem(R.id.action_invite).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_invite:
                //Check if current action mode if from
                SparseBooleanArray selected;
                selected = recyclerAdapter.getSelectedIds();
                int selectedMessageSize = selected.size();
                StringBuilder str = new StringBuilder();
                //loop
                for (int i = (selectedMessageSize - 1); i >= 0; i--) {
                    if (selected.valueAt(i)) {
                        //선택된 데이터 가져오기
                        FriendListItem model = items.get(selected.keyAt(i));
                        String nickname = model.getFriend_nick();
                        String fUid = model.getfUid();
                        String profile = model.getProfile();
                        Log.e("selected", nickname + ", " + fUid + ", " + profile);
                        str.append(nickname + "*" + fUid + "*" + profile + "*");
                    }
                }
                Log.e("string : ", String.valueOf(str));
                Toast.makeText(context, selectedMessageSize + "명의 친구를 초대했습니다.", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.context);
                //editor 얻기
                SharedPreferences.Editor editor = sharedPref.edit();
                //value 넣기
                editor.putInt("count", selectedMessageSize);
                editor.putString("members", str.toString());
                editor.commit();
                mode.finish();
                ((Activity) context).finish();
                break;
        }


        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        //When action mode destroyed remove selected selections and set action mode to null
        //First check current fragment action mode
        recyclerAdapter.removeSelection();
        Fragment recyclerFragment = new AddFriendActivity().getFragment(0);
        if (recyclerFragment != null) {
            ((RecyclerView_Fragment) recyclerFragment).setNullToActionMode();
        }

    }

}
