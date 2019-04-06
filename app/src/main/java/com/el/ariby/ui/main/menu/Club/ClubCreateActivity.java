package com.el.ariby.ui.main.menu.Club;

import android.content.ContentResolver;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.el.ariby.R;
import com.el.ariby.databinding.ActivityClubCreateBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;

public class ClubCreateActivity extends AppCompatActivity {
    ActivityClubCreateBinding mBinding;
    FirebaseDatabase database;
    DatabaseReference ref;

    Uri FilePathUri;
    StorageReference storageReference;
    String Storage_Path = "club/"; // 파이어베이스 스토리지 저장 폴더

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_create);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_club_create);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        storageReference = FirebaseStorage.getInstance().getReference();

        mBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.not_move_activity, R.anim.rightout_activity);
            }
        });

        mBinding.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select Image"), 1);
            }
        });

        mBinding.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadClubInfoToFirebase();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    FilePathUri = data.getData();
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    mBinding.imgProfile.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String GetFileExtension(Uri uri) { //
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void UploadClubInfoToFirebase() {
        if (FilePathUri != null) {
            final StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            final String name = mBinding.etName.getText().toString();
            String commend = mBinding.etIntro.getText().toString();
            String location = mBinding.etLocation.getText().toString();

            ref.child("club").child(name).setValue(new ClubModel(commend, location));
            ref.child("club").child(name).child("member").child("superVisor").setValue(mUser.getUid());
            storageReference2nd.putFile(FilePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();
                    ref.child("club").child(name).child("clubImageURL").setValue(downloadUrl.toString());

                    Toast.makeText(getApplicationContext(), "클럽이 생성되었습니다.", Toast.LENGTH_LONG).show();
                    finish();
                    overridePendingTransition(R.anim.not_move_activity, R.anim.rightout_activity);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), "업로드에 실패하였습니다.", Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "클럽 이미지 사진을 선택해주세요.", Toast.LENGTH_LONG).show();
        }
    }
}

class ClubModel {
    String commend;
    String location;

    public ClubModel(String commend, String location) {
        this.commend = commend;
        this.location = location;
    }
}
