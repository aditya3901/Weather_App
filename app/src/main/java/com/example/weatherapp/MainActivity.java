package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();

        binding.loader.setVisibility(View.VISIBLE);
        loadWeather();
    }

    public void loadWeather(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q=barrackpore&units=metric&appid=06c921750b9a82d8f5d1294e1586276f";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String location = response.getString("name");
                            binding.address.setText(location);

                            long timeInMili = Long.parseLong(response.getString("dt"));
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            String t1 = sdf.format(new java.util.Date(timeInMili*1000));
                            binding.updatedAt.setText(t1);

                            JSONArray array = response.getJSONArray("weather");
                            JSONObject obj = array.getJSONObject(0);
                            String status = obj.getString("main");
                            binding.status.setText(status);
                            JSONObject main = response.getJSONObject("main");
                            String temp = main.getString("temp");
                            binding.temp.setText(temp + "\u2103");
                            String min = main.getString("temp_min");
                            String max = main.getString("temp_max");
                            binding.tempMin.setText("Min Temp : " + min + "\u2103");
                            binding.tempMax.setText("Max Temp : " + max + "\u2103");
                            String pressure = main.getString("pressure");
                            binding.pressure.setText(pressure);
                            String humidity = main.getString("humidity");
                            binding.humidity.setText(humidity);
                            JSONObject sys = response.getJSONObject("sys");
                            String sunrise = sys.getString("sunrise");
                            String sunset = sys.getString("sunset");

                            long SR = Long.parseLong(sunrise);
                            String t2 = convert(SR);
                            binding.sunrise.setText(t2);

                            long SS = Long.parseLong(sunset);
                            String t3 = convert(SS);
                            binding.sunset.setText(t3);
                            binding.mainContainer.setVisibility(View.VISIBLE);
                            binding.loader.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        queue.add(jsonObjectRequest);
    }

    public String convert(long dateTime){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String convertedDate = sdf.format(new java.util.Date(dateTime*1000));
        return convertedDate;
    }
}