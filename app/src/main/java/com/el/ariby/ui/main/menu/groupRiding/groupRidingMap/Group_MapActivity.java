package com.el.ariby.ui.main.menu.groupRiding.groupRidingMap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.el.ariby.R;
import com.el.ariby.ui.api.MapFindApi;
import com.el.ariby.ui.api.SelfCall;
import com.el.ariby.ui.api.response.MapFindRepoResponse;
import com.el.ariby.ui.main.menu.groupRiding.GroupRideActivity;
import com.el.ariby.ui.main.menu.navigation.PointDouble;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Group_MapActivity extends AppCompatActivity
        implements MapView.CurrentLocationEventListener, NavigationView.OnNavigationItemSelectedListener {
    MapView mapView;
    FloatingActionButton fab;
    ArrayList<PointDouble> memberPoints = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference ref;
    DatabaseReference userRef;
    DatabaseReference myGroupRef;
    String groupName;
    int count = 100;
    private CountDownTimer countDownTimer;

    //좌표
    ArrayList<Double> coordinates;
    Intent intent;
    String startX, startY, endX, endY;
    int memberTag =1;
    MapPOIItem[] items;
    List<MapPOIItem> marker;
    ArrayList<String> memberList = new ArrayList<>();
    String myNick;
    String myUid;

    //TODO. test
    Double latitude = 37.66739;
    Double longitude = 127.03892;
    ListView listView = null;

    //drawer
    ArrayList<MemberListItem> memberListItems = new ArrayList<>();
    MemberListAdapter memberListAdapter;

    int myPosition;
    int memberCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_riding_main);
        Toolbar toolbar = findViewById(R.id.group_riding_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        DrawerLayout drawerLayout = findViewById(R.id.group_drawer_layout2);
        NavigationView navigationView = findViewById(R.id.group_nav_view);
        final TextView txtGroupName = findViewById(R.id.group_navi_groupname);
        final TextView txtMemberCount = findViewById(R.id.group_navi_members);
        final TextView btnOut = findViewById(R.id.btnOut);
        final TextView btnStop = findViewById(R.id.btnStop);
        listView = findViewById(R.id.group_nav_member_list);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        mapView = new MapView(this);
        memberListAdapter = new MemberListAdapter();
        listView.setAdapter(memberListAdapter);

        final ViewGroup mapViewContainer = findViewById(R.id.group_map_view);
        mapViewContainer.addView(mapView);
        //GPSListener gpsListener = new GPSListener();

        intent = getIntent();
        groupName = intent.getStringExtra("groupName");
        startX = intent.getStringExtra("startY");
        startY = intent.getStringExtra("startX");
        endX = intent.getStringExtra("endY");
        endY = intent.getStringExtra("endX");
        Log.e("intents : ", startX+", "+startY);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("GROUP_RIDING");
        myGroupRef = database.getReference("GROUP_RIDING_MEMBERS");

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myUid = firebaseUser.getUid();
        userRef = database.getReference("USER").child(firebaseUser.getUid());
        marker = new ArrayList<MapPOIItem>();


        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myNick = dataSnapshot.child("nickname").getValue().toString();
                myUid = firebaseUser.getUid();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nameCom = null;
                memberTag =0;
                int a = 0;
                // URL url =  new URL("https://firebasestorage.googleapis.com/v0/b/elandroid.appspot.com/o/profile%2Fprofile.png?alt=media&token=b65b2e7b-e58b-4ff5-a38d-99ce048ec97a");;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    nameCom = snapshot.getKey();
                    if(groupName.equals(nameCom)){
                        memberCount = (int) snapshot.child("members").getChildrenCount();
                        Log.d("memberCount1", String.valueOf(memberCount));
                        for(a = 0; a<memberCount; a++) { //멤버들의 위치, 프로필, 닉네임 가져오기 (처음 로딩되었을 때 마커 뿌리기)

                            final String getProf = snapshot.child("members").child(String.valueOf(a)).child("profile").getValue().toString();
                            String getNick = snapshot.child("members").child(String .valueOf(a)).child("nickname").getValue().toString();
                            String getState = snapshot.child("members").child(String.valueOf(a)).child("state").getValue().toString();
                            memberList.add(getNick);
                            memberListAdapter.addItem(new MemberListItem(getProf, getNick));
                            coordinates = startLocationService();

                            if(memberList.get(a).equals(myNick)){
                                Log.d("내 닉네임 : ", myNick);
                                ref.child(groupName).child("members").child(String.valueOf(a)).child("lat").setValue(coordinates.get(0));
                                ref.child(groupName).child("members").child(String.valueOf(a)).child("lon").setValue(coordinates.get(1));
                                myPosition = a;
                            }

                            String getLat = snapshot.child("members").child(String.valueOf(a)).child("lat").getValue().toString();
                            String getLon = snapshot.child("members").child(String.valueOf(a)).child("lon").getValue().toString();
                            MapPoint markPoint3 = MapPoint.mapPointWithGeoCoord(Double.parseDouble(getLat), Double.parseDouble(getLon));
                            MapPOIItem marker2 = new MapPOIItem();


                            Log.e("getLat + getLon : ", getLat + ",   " + getLon+",   "+getProf);
                            marker2.setItemName(String.valueOf(memberTag));
                            marker2.setTag(memberTag);
                            marker2.setMapPoint(markPoint3);
                            /*marker2.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                            marker2.setCustomImageResourceId(R.drawable.default_image);
                            marker2.setCustomImageAutoscale(true);
                            marker2.setCustomImageAnchor(0.5f, 1.0f);
*/
                            //marker2.setCustomImageResourceId(drawableFromUrl(getProf));
                            marker2.setMarkerType(MapPOIItem.MarkerType.YellowPin);
                            marker2.setSelectedMarkerType(MapPOIItem.MarkerType.YellowPin);
                            marker.add(marker2);
                           mapView.addPOIItem(marker2);
                            memberTag++;
                        }
                        items = mapView.getPOIItems();
                        Log.e("item_list length ", String.valueOf(items.length));
                        for(int z = 0; z <items.length ;z++){
                            MapPOIItem mapPOIItem = mapView.findPOIItemByTag(items[z].getTag());
                            mapView.selectPOIItem(mapPOIItem, true);
                            Log.d("item_list : ", items[z].getTag()+" item_list poiItem : "+ mapPOIItem);
                            Log.e("item_list N : ", String.valueOf(z));
                        }
                        break;
                    }
                }
                txtGroupName.setText(groupName);
                txtMemberCount.setText("인원 : "+memberCount);
                memberListAdapter.notifyDataSetChanged();
            } 

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getMapFind(startX, startY, endX, endY);
        //getMapFind(startY, startY, endY, endX);
        MapPoint markerPointStart = MapPoint.mapPointWithGeoCoord(
                Double.parseDouble(startY), Double.parseDouble(startX));
        Log.e("MapPoint : ", "Y: "+startY);

        MapPoint markerPointEnd = MapPoint.mapPointWithGeoCoord(
                Double.parseDouble(endY), Double.parseDouble(endX));

        mapView.setMapCenterPoint(markerPointStart, true);
        MapPOIItem startMarker = new MapPOIItem(); // 마커 생성
        startMarker.setItemName("start");
        startMarker.setTag(100); //start pin tag
        startMarker.setMapPoint(markerPointStart);
        startMarker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        startMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

        MapPOIItem endMarker = new MapPOIItem(); // 마커 생성
        endMarker.setItemName("end");
        endMarker.setTag(101); //end pin tag
        endMarker.setMapPoint(markerPointEnd);
        endMarker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        endMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

        mapView.addPOIItem(startMarker);
        mapView.addPOIItem(endMarker);

        mapView.setHDMapTileEnabled(true); // HD 타일 사용여부
        mapView.setMapTilePersistentCacheEnabled(true);//다운한 지도 데이터를 단말의 영구 캐쉬 영역에 저장하는 기능
        mapView.setCurrentLocationEventListener(this);

        countDownTimer();
        countDownTimer.start();

        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Group_MapActivity.this);
                alertDialog.setTitle("나가기");
                alertDialog.setMessage("그룹에서 나가시겠습니까?");
                alertDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Group_MapActivity.this, "그룹을 나갑니다.", Toast.LENGTH_SHORT).show();
                        myGroupRef.child(myUid).child(groupName).removeValue();
                        dialog.cancel();
                        onBackPressed();
                    }
                });

                alertDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });
    }

    public static BitmapDrawable drawableFromUrl (String url) throws IOException{
        Bitmap bitmap;
        HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();
        bitmap = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(Resources.getSystem(), bitmap);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void countDownTimer(){
        countDownTimer = new CountDownTimer(11*1000, 6000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.e("countdownTimer : ", String.valueOf(count));
                count--;
                startLocationService();
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //5초 간격 값 불러오기 (위치 업데이트)
                        String nameCom = null;
                        //memberTag =1;
                        int a = 1;

                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            nameCom = snapshot.getKey();
                            if(groupName.equals(nameCom)){
                                memberCount = (int) snapshot.child("members").getChildrenCount();
                                Log.d("memberCount", String.valueOf(memberCount));
                                items = mapView.getPOIItems();
                                for(a = 0; a<memberCount; a++) {
                                    String getLat = snapshot.child("members").child(String.valueOf(a)).child("lat").getValue().toString();
                                    String getLon = snapshot.child("members").child(String.valueOf(a)).child("lon").getValue().toString();
                                    String getNick = snapshot.child("members").child(String.valueOf(a)).child("nickname").getValue().toString();
                                    MapPoint markPoint3 = MapPoint.mapPointWithGeoCoord(Double.parseDouble(getLat), Double.parseDouble(getLon));
                                    MapPOIItem poiItemByTag = mapView.findPOIItemByTag(a);
                                    poiItemByTag.setMapPoint(markPoint3);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onFinish() {
                Log.d("finished", "finished");
                //countDownTimer.start();
            }
        };
    }

    public void onBackPressed(){
        countDownTimer.cancel();
        DrawerLayout drawerLayout = findViewById(R.id.group_drawer_layout2);
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
            Intent intent1 = new Intent(this, GroupRideActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent1);
            return;

    }

    private void getMapFind(final String startX, final String startY, final String endX, final String endY) { //레트로핏 경로 가져오기
        Retrofit retrofit = SelfCall.createRetrofit(MapFindApi.BASEURL);
        MapFindApi apiService = retrofit.create(MapFindApi.class);
        Call<MapFindRepoResponse> call =
                apiService.getMapFind("d7673b71-bc89-416a-9ac6-019e5d8f327a", "1",
                        startX, startY, endX, endY, "123", "234");

        call.enqueue(new Callback<MapFindRepoResponse>() {
            @Override
            public void onResponse(Call<MapFindRepoResponse> call,
                                   Response<MapFindRepoResponse> response) {

                MapFindRepoResponse repo = response.body();
                int featuresSize = repo.getFeatures().size();

                Log.d("getMapFind start : ", "start");
                MapPolyline polyline = new MapPolyline();
                polyline.setTag(1000);
                polyline.setLineColor(Color.argb(128, 255, 51, 0)); // Polyline 컬러 지정.

                polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(startY), Double.parseDouble(startX)));
                for (int i = 0; i < featuresSize; i++) {
                    String type = repo.getFeatures().get(i).getGeometry().getType();
                    PointDouble points;
                    if (type.equals("Point")) {
                        points = new PointDouble(
                                (Double) repo.getFeatures().get(i).getGeometry().getCoordinates().get(0),
                                (Double) repo.getFeatures().get(i).getGeometry().getCoordinates().get(1));
                        MapPoint marketPoint3 = MapPoint.mapPointWithGeoCoord(points.getY(), points.getX());
                        memberPoints.add(points);
                        MapPOIItem marker3 = new MapPOIItem(); // 마커 생성
                        marker3.setItemName(repo.getFeatures().get(i).getProperties().getDescription());
                        marker3.setTag(103);
                        marker3.setMapPoint(marketPoint3);
                        marker3.setMarkerType(MapPOIItem.MarkerType.RedPin);

                        mapView.addPOIItem(marker3);

                        polyline.addPoint(MapPoint.mapPointWithGeoCoord(points.getY(), points.getX()));
                    } else if (type.equals("LineString")) {
                        List<Object> list = repo.getFeatures().get(i).getGeometry().getCoordinates();
                        for (int k = 0; k < list.size(); k++) { // k
                            String[] lit = String.valueOf(list.get(k)).split(" ");
                            Double lineX = Double.parseDouble(lit[0].substring(1, lit[0].length() - 1));
                            Double lineY = Double.parseDouble(lit[1].substring(0, lit[1].length() - 1));
                            polyline.addPoint(MapPoint.mapPointWithGeoCoord(lineY, lineX));
                        }
                    }
                }
                polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(endY), Double.parseDouble(endX)));
                mapView.addPolyline(polyline);
                MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                int padding = 150; // px
                mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

            }

            @Override
            public void onFailure(Call<MapFindRepoResponse> call, Throwable t) {
                Log.d("TEST : ", t.getMessage());
            }
        });
    }


    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        // 킬로미터(Kilo Meter) 단위
        double distanceKiloMeter =
                distance(mapPointGeo.latitude, mapPointGeo.longitude,
                        37.47356391969749, 126.89078163446104, "meter");

        Log.d("currentDistance", String.valueOf(distanceKiloMeter));
        Log.d("currentLocation",
                String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)",
                        mapPointGeo.latitude, mapPointGeo.longitude, v));
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) +
                Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "kilometer") {
            dist = dist * 1.609344;
        } else if (unit == "meter") {
            dist = dist * 1609.344;
        }

        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    private ArrayList<Double> startLocationService() {

        LocationManager manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        }

        Location location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        //Double latitude = location.getLatitude();
        //Double longitude = location.getLongitude();

        ArrayList<Double> list = new ArrayList<>();
        list.add(latitude);
        list.add(longitude);

        String msg = "Group_Last Known Location -> Latitude : " +
                location.getLatitude() +
                "\nLongitude : " + location.getLongitude();
        Log.i("Group_SampleLocation ", msg);
        Log.d("myPosition check", String.valueOf(myPosition));

        ref.child(groupName).child("members").child(String.valueOf(myPosition)).child("lat").setValue(latitude);
        ref.child(groupName).child("members").child(String.valueOf(myPosition)).child("lon").setValue(longitude);
        String msg1 = "Latitude : " + latitude + "\nLongitude : " + longitude;
        Toast.makeText(Group_MapActivity.this, msg1, Toast.LENGTH_SHORT).show();
        latitude = latitude +0.001;
        longitude = longitude + 0.001;
        return list;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        return false;
    }

    public class MemberListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return memberListItems.size();
        }

        @Override
        public Object getItem(int position) {
            return memberListItems.get(position);
        }

        @Override
        public long getItemId(int position) {
           return position;
        }

        public void addItem(MemberListItem item){ memberListItems.add(item);}

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Context context = parent.getContext();
            CustomMemberList view = new CustomMemberList(context);
            MemberListItem item = memberListItems.get(position);
            view.setNickname(item.getNickname());
            view.setProfile(item.getProfile());
            return view;
        }
        public void clearItem(){memberListItems.clear();}
    }
}
