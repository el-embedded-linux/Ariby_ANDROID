package com.el.ariby.ui.main.menu.groupRiding.addFriend;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.el.ariby.R;
import com.el.ariby.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecyclerView_Fragment extends Fragment {
    private static View view;
    private static RecyclerView recyclerView;
    private static ArrayList<FriendListItem> item_models;
    private static FriendListRecyclerAdapter adapter;
    private ActionMode actionMode;

    FirebaseDatabase database;
    DatabaseReference userRef;
    FirebaseUser user;
    String uid, followUid;
    DatabaseReference ref;

    int i=0;

    public RecyclerView_Fragment(){

    }

    public static Fragment fa;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recycler_view_fragment, container, false);
        populateRecyclerView();
        implementRecyclerViewClickListeners();
        fa = this.getTargetFragment();
        return view;
    }

    //Populate ListView with dummy data
    private void populateRecyclerView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        item_models = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        ref = database.getReference("FRIEND").child("following").child(uid);
        userRef = database.getReference("USER");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            int i=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot : dataSnapshot.getChildren()){

                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(final DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                                String user = snapshot1.getKey();
                                followUid = snapshot.getKey();
                                if(followUid.equals(user)){
                                    String url = (String)snapshot1.child("userImageURL").getValue();
                                    String nickname = dataSnapshot.child(user).child("nickname").getValue().toString();
                                    String fUid = dataSnapshot.child(user).getKey();
                                    Log.d("url",url+"  nick : "+nickname);
                                    if(url == null)
                                    {
                                        url = "https://firebasestorage.googleapis.com/v0/b/elandroid.appspot.com/o/profile%2Fprofile.png?alt=media&token=b65b2e7b-e58b-4ff5-a38d-99ce048ec97a";
                                    }
                                    item_models.add(new FriendListItem(nickname,url, fUid));
                                    Log.e("str : ", item_models.get(i).toString());
                                    i = i+1;
                                }
                            }
                            adapter = new FriendListRecyclerAdapter(getActivity(), item_models);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    private void implementRecyclerViewClickListeners() {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerClick_Listener() {
            @Override
            public void onClick(View view, int position) {
                //If ActionMode not null select item
                if (actionMode != null)
                    onListItemSelect(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                //Select item on long click
                onListItemSelect(position);
            }
        }));
    }

    private void onListItemSelect(int position) {
        adapter.toggleSelection(position);//Toggle the selection

        boolean hasCheckedItems = adapter.getSelectedCount() > 0;//Check if any items are already selected or not


        if (hasCheckedItems && actionMode == null)
            // there are some selected items, start the actionMode
            actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new Toolbar_ActionMode_Callback(getActivity(), adapter, item_models));
        else if (!hasCheckedItems && actionMode != null)
            // there no selected items, finish the actionMode
            actionMode.finish();

        if (actionMode != null)
            //set action mode title on item selection
            actionMode.setTitle(String.valueOf(adapter
                    .getSelectedCount()) + "명의 친구 추가");
    }

    //Set action mode null after use
    public void setNullToActionMode() {
        if (actionMode != null)
            actionMode = null;
    }



}
