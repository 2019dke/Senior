package com.example.galgot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class storeEval extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_eval);
        final TextView tv = (TextView)findViewById(R.id.tv_rating);
        RatingBar rb = (RatingBar)findViewById(R.id.rb);
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                tv.setText("Rating : "+ rating);
            }
        });

        Button bt = findViewById(R.id.btn_eval);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ID 정보와 별점 정보, 가게 정보를 DB에 저장한다.
                finish();
            }
        });
    }
}
