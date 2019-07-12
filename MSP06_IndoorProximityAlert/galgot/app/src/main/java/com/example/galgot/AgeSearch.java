package com.example.galgot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AgeSearch extends AppCompatActivity {

    String[] items = {"1~5","6~10","11~15","16~20","21~25","26~30","31~40","41~50","60 이상"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_search);
        Spinner spinner = (Spinner) findViewById(R.id.sp_age);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }
    public void onClick(View view){

        if(view.getId() == R.id.btn_back){
            finish();
        }
        else if(view.getId() == R.id.btn_ageSearch){
            //ID 정보 등등 넘김
            Intent intent = new Intent(getApplicationContext(), result.class);
            startActivity(intent);
        }

    }
}
