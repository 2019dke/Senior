package com.example.galgot;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StoreItemView extends LinearLayout {
    TextView tv;
    public StoreItemView(Context context) {
        super(context);
        init(context);
    }

    public StoreItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.store_item,this,true);
        tv = findViewById(R.id.tv_store);

    }
    public void setStore(String name){
        tv.setText(name);
    }


}
