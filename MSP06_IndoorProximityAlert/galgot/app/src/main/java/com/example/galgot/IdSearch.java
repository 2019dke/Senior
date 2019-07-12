package com.example.galgot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class IdSearch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_search);
    }
    public void onClick(View view){

        if(view.getId() == R.id.btn_back){
            finish();
        }
        else if(view.getId() == R.id.btn_IdSearch){
            //ID 정보 등등 넘김
            Intent intent = new Intent(getApplicationContext(), result.class);
            startActivity(intent);
        }

    }
}
