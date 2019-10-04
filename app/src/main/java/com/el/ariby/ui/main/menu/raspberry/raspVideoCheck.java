package com.el.ariby.ui.main.menu.raspberry;

import android.content.ContentValues;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.el.ariby.R;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class raspVideoCheck extends AppCompatActivity {
    ListView rasp_video_list;
    ArrayList<String> videos = new ArrayList<String>();
    private String URL = "http://192.168.100.1:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rasp_video_check);
        rasp_video_list = findViewById(R.id.rasp_video_list);

        ContentValues values = new ContentValues();
        values.put("token", "test");
        NetworkTask task = new NetworkTask(URL+"/videoList",values,"GET");
        task.addNetworkTaskResult(new NetworkTask.NetworkTaskResult() {
            @Override
            public void done(String result) {
                try {
                    final JSONArray jsonViedos = new JSONArray(result);
                    for (int i = 0; i < jsonViedos.length(); i++) {
                        videos.add(jsonViedos.get(i).toString());
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,videos);
                    rasp_video_list.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String result) {

            }
        });
        task.execute();

        rasp_video_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),videos.get(position),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplication(),videoPlayer.class);
                intent.putExtra("URL",URL+"/"+videos.get(position));
                startActivity(intent);
            }
        });
    }
}
