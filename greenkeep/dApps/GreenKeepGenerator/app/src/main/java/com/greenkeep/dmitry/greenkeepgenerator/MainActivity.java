package com.greenkeep.dmitry.greenkeepgenerator;

import android.app.Activity;
import android.os.Handler;
import android.os.SystemClock;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request.Method;

import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.greenkeep.dmitry.greenkeepgenerator.json.SensorData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends Activity {

    private Button generateAndPushButton;
    private ListView generateListView;

    private ArrayList<String> listItems=new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private Integer count = 0;
    private RequestQueue queue;
    private ArrayList<SensorData> sensorData = new ArrayList<>();
    final Integer MAX_ENTRIES = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("dmitry", "ONCREATE");


        queue = Volley.newRequestQueue(this);
        generateAndPushButton = (Button) findViewById(R.id.generateAndPushButton);
        generateListView = (ListView) findViewById(R.id.generatedDataListView);


        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        generateListView.setAdapter(adapter);

        sensorData = generateSensorData();

        generateAndPushButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                String url ="http://192.168.137.1:8080/foo";

                for(SensorData data: sensorData) {
                    queue.add(createPostRequest(url, data));
                }
            }
        });

    }

    private ArrayList<SensorData> generateSensorData() {
        ArrayList<SensorData> sensorData = new ArrayList<>();

        for (int i = 0; i < MAX_ENTRIES; i++) {
            SensorData data = new SensorData();
            data.setDitance_traveled_per_day(genRandomNumberInRange(3, 5));
            data.setAge(genRandomNumberInRange(3, 5));
            data.setBatchdate(20180805);
            data.setLocation("Creek Homestead");
            data.setWeight(genRandomNumberInRange(720, 1100));
            sensorData.add(data);
            Log.d("dmitry", data.toString());

        }
        return sensorData;
    }

    private StringRequest createPostRequest(final String url, final SensorData data) {
        StringRequest postRequest = new StringRequest(Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        addItems(data);
                        generateListView.setSelection(adapter.getCount() - 1);
                        count++;

                        if (count == MAX_ENTRIES-1) {
                            Toast.makeText(getApplicationContext(), "Done",
                                    Toast.LENGTH_LONG).show();
                        }

                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("distance_traveled_per_day", data.getDitance_traveled_per_day().toString());
                params.put("age", data.getAge().toString());
                params.put("weight", data.getWeight().toString());
                params.put("batchdate", data.getBatchdate().toString());
                params.put("weight", data.getWeight().toString());
                params.put("location", data.getLocation());

                return params;
            }
        };
        return  postRequest;
    }


    public void addItems(SensorData data) {
        listItems.add(data.toString());
        adapter.notifyDataSetChanged();
    }

    private static int genRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("Max number must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
