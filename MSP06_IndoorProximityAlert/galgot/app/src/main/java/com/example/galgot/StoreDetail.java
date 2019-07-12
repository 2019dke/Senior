package com.example.galgot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StoreDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
    }

    public void onClick(View view){

        if(view.getId() == R.id.btn_back){
            finish();
        }
        else if(view.getId() == R.id.btn_sel){
            //id, 가게정보를 Eval 액티비티로 보내주어 디테일 화면 표시하기.
            Intent intent = new Intent(getApplicationContext(), storeEval.class);
            startActivity(intent);
        }

    }
}
