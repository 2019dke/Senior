package com.example.galgot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin;
    EditText etId,etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etId = findViewById(R.id.etId);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String strId = etId.getText().toString();
                String strPassword = etPassword.getText().toString();
                if(strId.equals("a") && strPassword.equals("a")){

                    Intent intent = new Intent(getApplicationContext(), Main_Search.class);
                    startActivity(intent);
                }
            }
        });



    }
}
