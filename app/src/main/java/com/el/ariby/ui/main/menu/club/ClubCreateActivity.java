package com.el.ariby.ui.main.menu.club;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.el.ariby.Module;
import com.el.ariby.R;
import com.el.ariby.databinding.ActivityClubCreateBinding;
import com.el.ariby.ui.api.GeoApi;
import com.el.ariby.ui.api.SelfCall;
import com.el.ariby.ui.api.response.GeoRepoResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ClubCreateActivity extends AppCompatActivity {
    ActivityClubCreateBinding mBinding;
    FirebaseDatabase database;
    DatabaseReference ref;
    DatabaseReference userRef;
    DatabaseReference clubRef;

    Uri FilePathUri;
    StorageReference storageReference;
    String Storage_Path = "club/"; // 파이어베이스 스토리지 저장 폴더
    Boolean nameCheck = true;

    private Module module=new Module();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_club_create);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        clubRef=database.getReference("CLUB");
        storageReference = FirebaseStorage.getInstance().getReference();

        mBinding.etName.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        mBinding.txtNameCheck.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        clubRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String name = mBinding.etName.getText().toString();
                                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Log.e("테스트", name);
                                    Log.e("테스트:key", snapshot.getKey());

                                    if (name.equals(snapshot.getKey()) ||
                                            !(module.isNameCheck(name))) {
                                        mBinding.txtNameCheck.setText("이미 존재하거나 특수문자는 입력할 수 없습니다.");
                                        mBinding.txtNameCheck.setTextColor(Color.RED);
                                        mBinding.btnCreate.setEnabled(false);
                                        mBinding.btnCreate.setBackgroundColor(Color.parseColor("#FF979797"));
                                        break;
                                    } else {
                                        mBinding.txtNameCheck.setText("생성 가능합니다.");
                                        mBinding.txtNameCheck.setTextColor(Color.BLUE);
                                        mBinding.btnCreate.setEnabled(true);
                                        mBinding.btnCreate.setBackgroundColor(Color.parseColor("#1E90FF"));
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

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
        startLocationService();
        mBinding.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameCheck) {
                    showProgressBar();
                    UploadClubInfoToFirebase();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "같은 이름의 클럽이 이미 있습니다.", Toast.LENGTH_SHORT).show();
                }
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
            final StorageReference storageReference2nd = storageReference.child(Storage_Path +
                    System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            userRef = database.getReference("USER").child(mUser.getUid());

            final String name = mBinding.etName.getText().toString();
            String commend = mBinding.etIntro.getText().toString();
            String location = mBinding.etLocation.getText().toString();
            ClubModel model = new ClubModel(commend, location);
            ref.child("CLUB").child(name).setValue(model);
            ref.child("CLUB").child(name).child("member").child("superVisor").setValue(mUser.getUid());

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ref.child("CLUB").child(name).child("leaderNick").setValue(dataSnapshot.child("nickname").getValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            storageReference2nd.putFile(FilePathUri).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri downloadUrl = urlTask.getResult();
                            ref.child("CLUB").child(name).child("clubImageURL").setValue(downloadUrl.toString());
                            hideProgressBar();
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

    private void getGeo(String x, String y, String cord) {
        Retrofit retrofit = SelfCall.createRetrofit(GeoApi.BASEURL);

        GeoApi apiService = retrofit.create(GeoApi.class);
        Call<GeoRepoResponse> call = apiService.getGeo("KakaoAK e880d656790ed7e10098f0742679154e", x, y, cord);
        Log.d("call : ", call.toString());

        call.enqueue(new Callback<GeoRepoResponse>() {
            @Override
            public void onResponse(Call<GeoRepoResponse> call,
                                   Response<GeoRepoResponse> response) {
                if (response.isSuccessful()) {
                    GeoRepoResponse repo = response.body();
                    ArrayList<String> Region = new ArrayList<>();
                    Region.add(repo.getDocuments().get(0).getAddress().getRegion_1depth_name() + " ");
                    Region.add(repo.getDocuments().get(0).getAddress().getRegion_2depth_name() + " ");
                    Region.add(repo.getDocuments().get(0).getAddress().getRegion_3depth_name());

                    for (String name : Region) {
                        mBinding.etLocation.append(name);
                    }
                }
            }

            @Override
            public void onFailure(Call<GeoRepoResponse> call, Throwable t) {
                Log.d("getGeo Error:", t.toString());
            }
        });
    }

    private void startLocationService() {
        LocationManager manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        Location location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();

        getGeo(longitude.toString(), latitude.toString(), "WGS84");
    }

    private void showProgressBar() {
        mBinding.pbClubCreate.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mBinding.pbClubCreate.setVisibility(View.INVISIBLE);
    }
}

class ClubModel {
    public String commend;
    public String location;

    public ClubModel(String commend, String location) {
        this.commend = commend;
        this.location = location;
    }
}
