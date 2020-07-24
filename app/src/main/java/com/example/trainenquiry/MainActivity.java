package com.example.trainenquiry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText scode ;
    EditText dcode ;
    Intent intent;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       scode = findViewById(R.id.scode);
       dcode = findViewById(R.id.dcode2);


    }

    public void submit(View view){

        DownloadTask task = new DownloadTask();
        task.execute("https://indianrailapi.com/api/v2/TrainBetweenStation/apikey/41f4fad0aa782ee1a43d7f0c91b4e456/From/"+scode.getText().toString()+"/To/"  +dcode.getText().toString());

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(dcode.getWindowToken(), 0);

        intent = new Intent(getApplicationContext(), info.class);
//        startActivity(intent);


    }

    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (Exception e) {

                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "NO TRAINS FOUND :(", Toast.LENGTH_LONG).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);

                String trainInfo = jsonObject.getString("Trains");

                Log.i("train content", trainInfo);

                JSONArray arr = new JSONArray(trainInfo);

                ArrayList<String> trains = new ArrayList<String>();

                String message = "";

                for (int i=0; i < arr.length(); i++) {
                    JSONObject jsonPart = arr.getJSONObject(i);
                    String trainno = jsonPart.getString("TrainNo");
                    String trainname = jsonPart.getString("TrainName");
                    String arrivaltime = jsonPart.getString("ArrivalTime");
                    String departuretime = jsonPart.getString("DepartureTime");
                    String traveltime = jsonPart.getString("TravelTime");



                    Log.i("train no",jsonPart.getString("TrainNo"));
                    Log.i("train name",jsonPart.getString("TrainName"));
                    Log.i("train name",jsonPart.getString("ArrivalTime"));
                    Log.i("train name",jsonPart.getString("DepartureTime"));
                    Log.i("train name",jsonPart.getString("TravelTime"));


                    if(!trainno.equals("") && !trainname.equals("")){
                       message += trainno + "\n" +trainname + "\n";
                       String message1 = "Train No : " + trainno + "\n" + "Train Name : "  +trainname + "\n" + "Arrival Time : "  +arrivaltime + "\n" + "Departure Time : " +departuretime + "\n" +"Travel Time : " + traveltime + "\n";
                       trains.add(message1);
                    }else{
                        trains.add("Something went wrong !");
                    }


                }

                if(!message.equals("")){
                    intent.putExtra("msg",message);
                    intent.putExtra("array", trains);
                    startActivity(intent);

                    for (int i = 0; i < trains.size(); i++) {
                        Log.i("description array", trains.get(i));
                    }

                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "NO TRAINS FOUND :(", Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }

        }
    }

}