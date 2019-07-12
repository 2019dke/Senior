package com.example.galgot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Main_Search extends AppCompatActivity {
    EditText etSearch;
    String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__search);
        etSearch=(EditText) findViewById(R.id.etSearch);
    }
    public void onClick(View view){
        data = "Food";

        if(view.getId() == R.id.btnLogout){
            finish();
        }
        else if(view.getId() == R.id.btnRes){
            data = data + etSearch.getText().toString();

            Intent intent = new Intent(getApplicationContext(), result.class);
            intent.putExtra("value",data);
            startActivity(intent);
        }
        else if(view.getId() == R.id.btnCafe){
            //ID, 식당,카페,놀이 정보, 보내줌
            Intent intent = new Intent(getApplicationContext(), result.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.btnPlay){
            //ID, 식당,카페,놀이 정보, 보내줌
            Intent intent = new Intent(getApplicationContext(), result.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.btnIdSearch){
            Intent intent = new Intent(getApplicationContext(), IdSearch.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.btnAgeSearch){
            Intent intent = new Intent(getApplicationContext(), AgeSearch.class);
            startActivity(intent);
        }


    }
}
