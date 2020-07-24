package com.example.trainenquiry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class info extends AppCompatActivity {

    TextView textView ;
    ListView listView;

    public void back(View view){
      finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

//        textView = findViewById(R.id.textView);

        Intent intent = getIntent();
        String msg =intent.getStringExtra("msg");


        ArrayList<String> numbersList = (ArrayList<String>) getIntent().getSerializableExtra("array");
//        textView.setText(String.valueOf(numbersList));

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, numbersList);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(itemsAdapter);


    }
}