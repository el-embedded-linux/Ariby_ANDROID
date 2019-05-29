package com.el.ariby.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.el.ariby.R;
import com.el.ariby.ui.main.menu.follow.FindFollowActivity;
import com.el.ariby.ui.main.menu.follow.FollowListActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import javax.security.auth.callback.Callback;

public class InfoFragment extends Fragment {
    DatabaseReference ref;
    FirebaseDatabase database;
    TextView displayName, following_num, followers_num;
    ImageView photo;
    Button profile_more;
    Button add_friend;
    View v;
    FirebaseUser user;
    Uri uri;
    String following, follower;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_info, container, false);
        displayName = v.findViewById(R.id.fragment_info_profile_displayName);
        photo = v.findViewById(R.id.fragment_info_profile_photo);
        profile_more = v.findViewById(R.id.fragment_info_profile_more);
        add_friend = v.findViewById(R.id.fragment_info_profile_friend);
        following_num = v.findViewById(R.id.following_num);
        followers_num = v.findViewById(R.id.followers_num);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("USER");


        user = FirebaseAuth.getInstance().getCurrentUser();

        doWork(new Callback() {
            @Override
            public void callback() {
                if (user != null) {
                    String name = user.getDisplayName();
                    uri = user.getPhotoUrl();
                    Glide.with(getActivity()).load(uri).apply(RequestOptions.circleCropTransform()).into(photo); //이미지를 둥글게 처리
                    displayName.setText(name);
                    photo.setImageURI(uri);
                    following_num.setText(following);
                    followers_num.setText(follower);
                }
            }
        });



        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 이미지 파이어베이스에 업로드
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        profile_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 더보기 이벤트
                Toast.makeText(getContext(), "클릭했습니다.", Toast.LENGTH_LONG).show();
            }
        });

        add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 친구찾기 이벤트
                Intent intent = new Intent(getActivity(), FindFollowActivity.class);
                startActivity(intent);
            }
        });

        followers_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FollowListActivity.class);
                startActivity(intent);
            }
        });
        following_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FollowListActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference storageRef = storage.getReference();

            Uri file = data.getData();

            final StorageReference riversRef = storageRef.child("profile/" + user.getUid());
            UploadTask uploadTask = riversRef.putFile(file);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // 실패
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageRef.child("profile/" + user.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(uri)
                                    .build();
                            //프로필 사진을 바꾼 해당 유저의 uid를 받아와서 userImageURL에 uri를 설정
                            user = FirebaseAuth.getInstance().getCurrentUser();

                            String uid = user.getUid();
                            Log.d("URI", String.valueOf(uri) + "\nref" + ref + "\n" + uid);
                            ref.child(uid).child("userImageURL").setValue(String.valueOf(uri));


                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                }
            });
        }
    }


    public interface Callback {
        void callback();             // Callback 인터페이스 내의 속이 없는 껍데기 함수
    }


        public void doWork(final Callback mCallback) {
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String userUid = user.getUid();
                    following = dataSnapshot.child(userUid).child("following").getValue().toString();
                    follower = dataSnapshot.child(userUid).child("follower").getValue().toString();
                    Log.d("asd11", String.valueOf(following) + "asd11" + follower);
                    mCallback.callback();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
}

