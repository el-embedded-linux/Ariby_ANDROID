package com.el.ariby;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.el.ariby.ui.main.menu.navigation.MapData;
import com.el.ariby.ui.main.menu.navigation.MapSearchAdapter;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapTapi;

import java.util.ArrayList;

public class MapInputActivity extends AppCompatActivity {
    EditText etMapName;
    RecyclerView mRecycle;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayout;
    ArrayList<MapData> mArrayList;
    ArrayList<MapData> mArrayListTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_input);

        etMapName=findViewById(R.id.et_map_name);
        mRecycle=findViewById(R.id.my_recycler_view);
        mLayout = new LinearLayoutManager(this);
        mRecycle.setLayoutManager(mLayout);
        mArrayList=new ArrayList<>();
        mArrayListTest=new ArrayList<>();
        mAdapter=new MapSearchAdapter(mArrayList);
        mRecycle.setAdapter(mAdapter);

        TMapTapi tmaptapi = new TMapTapi(this);
        tmaptapi.setSKTMapAuthentication("d7673b71-bc89-416a-9ac6-019e5d8f327a");
        final TMapData tmapdata = new TMapData();

        etMapName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() { // POI검색은 쓰레드를 사용해야 나옴.
                        String str = etMapName.getText().toString();


                        tmapdata.findAllPOI(str, new TMapData.FindAllPOIListenerCallback() {
                            @Override
                            public void onFindAllPOI(ArrayList<TMapPOIItem> poiItem) {
                                mArrayListTest.clear();
                                /*
                                 java.lang.IndexOutOfBoundsException: Inconsistency detected
                                너무 빠른 요청은 에러를 발생시키기 때문에 새로운 ArrayList를 만들어서
                                목록을 받고 기존 ArrayList에 추가시키는 방식으로 하면 해결된다.
                                 */
                                for (int i = 0; i < poiItem.size(); i++) {
                                    TMapPOIItem item = (TMapPOIItem) poiItem.get(i);
                                    //if(!(item.getPOIAddress().length()<0))
                                        mArrayListTest.add(new MapData(item.getPOIName(),item.getPOIPoint().toString()));
                                }

                            }
                        });
                    }
                });
                mArrayList.clear();
                mArrayList.addAll(mArrayListTest);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
