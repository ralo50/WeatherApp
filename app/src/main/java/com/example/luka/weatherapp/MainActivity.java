package com.example.luka.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {


    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {

            String result = "";

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = connection.getInputStream();

                int data = inputStream.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = inputStream.read();
                }
                    return result;
                } catch(Exception e){

                }
                return "error";
            }
        }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void getWeather(View view){
        EditText editText = (EditText) findViewById(R.id.editText);
        TextView textView = (TextView) findViewById(R.id.textView);
        DownloadTask task = new DownloadTask();
        String result = null;

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        try {
            String encodedText = URLEncoder.encode(editText.getText().toString(), "UTF-8");
            String city = "http://api.openweathermap.org/data/2.5/weather?q=" + encodedText +"&appid=bf33da7435b314f1566d17131147d1b7";
            result = task.execute(city).get();
            System.out.println(result);
            if (result.equals("error")){
                Toast.makeText(this, "Invalid city", Toast.LENGTH_SHORT).show();
                textView.setText("");
            }
        } catch (Exception e) {
            Toast.makeText(this, "Invalid city", Toast.LENGTH_SHORT).show();
        }

        try {
            JSONObject jsonObject = new JSONObject(result);
            String weatherInfo = jsonObject.getString("weather");
            System.out.println(weatherInfo);
            JSONArray jsonArray = new JSONArray(weatherInfo);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject part = jsonArray.getJSONObject(i);

                String weatherr = part.getString("description");
                if(weatherr != "") {
                    textView.setText((part.getString("description")));
                }
                else{
                    Toast.makeText(this, "Invalid city", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
