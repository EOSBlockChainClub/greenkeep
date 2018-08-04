package com.greenkeep.dmitry.greenkeepstats;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.greenkeep.dmitry.greenkeepstats.json.ResultWrapper;
import com.greenkeep.dmitry.greenkeepstats.json.SensorData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //ArrayList<SensorData> sensorData;
    private Gson gson;
    private TextView batchSizeTextBox;
    private TextView batchDateTextBox;
    private TextView batchLocationTextBox;
    private TextView distanceTextBox;
    private TextView weightTextBox;
    private TextView ageTextBox;
    private ArrayList<SensorData> sensorData;
    private Button scanButton;
    private RequestQueue queue;
    private String url ="http://192.168.137.1:8080/foo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanButton = (Button) findViewById(R.id.scanButton);

        batchSizeTextBox = (TextView) findViewById(R.id.batchSize);
        batchSizeTextBox.setTextColor(Color.parseColor("#ee1289"));

        batchDateTextBox = (TextView) findViewById(R.id.batchDate);
        batchDateTextBox.setTextColor(Color.parseColor("#ee1289"));

        batchLocationTextBox = (TextView) findViewById(R.id.batchLocation);
        batchLocationTextBox.setTextColor(Color.parseColor("#ee1289"));

        distanceTextBox = (TextView) findViewById(R.id.distance);
        distanceTextBox.setTextColor(Color.parseColor("#ee1289"));

        weightTextBox = (TextView) findViewById(R.id.weight);
        weightTextBox.setTextColor(Color.parseColor("#ee1289"));

        ageTextBox = (TextView) findViewById(R.id.age);
        ageTextBox.setTextColor(Color.parseColor("#ee1289"));

        sensorData = new ArrayList<>();

        gson = new Gson();
        //sensorData = new ArrayList<>();

        queue = Volley.newRequestQueue(this);
        String url = "http://192.168.137.1:8080/foo";

        scanButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                createGetRequest();
            }
        });

// Request a string response from the provided URL.

    }

    private void createGetRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dmitry", response);
                        // Display the first 500 characters of the response string.
                        ResultWrapper result = gson.fromJson(response, ResultWrapper.class);

                        JSONObject jo = null;
                        try {
                            jo = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONObject results = jo.optJSONObject("result");
                        JSONArray jarray = results.optJSONArray("rows");

                        Type listType = new TypeToken<List<SensorData>>() {
                        }.getType();

                        sensorData = gson.fromJson(jarray.toString(), listType);

                        calculateAverageStats();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dmitry", error.toString());
            }
        });

        queue.add(stringRequest);
    }


    private void calculateAverageStats() {
        Integer batchSize = sensorData.size();

        Double averageAge = 0d;
        Double averageWeight = 0d;
        Double averageDistance = 0d;

        for (SensorData data : sensorData) {
            averageAge += data.getAge();
            averageWeight += data.getWeight();
            averageDistance += data.getDistance_traveled_per_day();
        }

        averageAge = averageAge / batchSize;
        averageWeight = averageWeight / batchSize;
        averageDistance = averageDistance / batchSize;


        displayStats(sensorData.get(0).getBatchdate(), sensorData.get(0).getLocation(), batchSize, averageAge, averageWeight, averageDistance);
    }

    private void displayStats(Integer batchdate, String location, Integer batchSize, Double averageAge, Double averageWeight, Double averageDistance) {


        batchDateTextBox.setText(buildDateString(batchdate.toString()));
        batchLocationTextBox.setText(location);
        batchSizeTextBox.setText(batchSize.toString());
        ageTextBox.setText(String.format("%.2f", averageAge) + " years");
        weightTextBox.setText(String.format("%.2f", averageWeight) + " kg");
        distanceTextBox.setText(String.format("%.2f", averageDistance) + " km");
    }

    private String buildDateString(String s) {
        return s.substring(0, 4) + "/" + s.substring(4, 6) + "/" + s.substring(6, 8);
    }
}
