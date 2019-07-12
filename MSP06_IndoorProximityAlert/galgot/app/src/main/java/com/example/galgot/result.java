package com.example.galgot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class result extends AppCompatActivity {
    ListView lv;
    Intent intent;
    private Socket socket;  //소켓생성
    BufferedReader in;      //서버로부터 온 데이터를 읽는다.
    PrintWriter out;        //서버에 데이터를 전송한다.
    String data;
    TextView output;
    Thread worker;
    EditText et_search;

    final itemAdapter adapter = new itemAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        intent =getIntent();
        et_search = (EditText)findViewById(R.id.et_search);

        lv = (ListView)findViewById(R.id.lv_item);
        data = intent.getStringExtra("value");
        output = (TextView)findViewById(R.id.textView);

        //worker = new mythread();
        //worker.start();
        //adapter.addItem(new StoreItem("중국집"));


        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent(getApplicationContext(),StoreDetail.class);
                startActivity(intent);
            }
        });
    }

    class itemAdapter extends BaseAdapter{
        ArrayList<StoreItem> items = new ArrayList<StoreItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }
        public void addItem(StoreItem item){
            items.add(item);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            StoreItemView view = new StoreItemView(getApplicationContext());

            StoreItem item = items.get(position);
            view.setStore(item.getName());

            return view;
        }

    }


    public void onClick(View view){

        if(view.getId() == R.id.btn_back){
            data = "END";
            worker = new mythread();
            worker.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finish();
        }
        else if(view.getId() == R.id.btn_refresh){
            data = et_search.getText().toString();
            worker = new mythread();
            worker.start();
        }

    }
    //쓰레드를 계속 켜두자 - 나정기
    class mythread extends Thread{
        public void run() { //스레드 실행구문
            try {
//소켓을 생성하고 입출력 스트립을 소켓에 연결한다.
                socket = new Socket("218.150.182.75", 4995); //소켓생성
                out = new PrintWriter(socket.getOutputStream(), true); //데이터를 전송시 stream 형태로 변환하여                                                                                                                       //전송한다.
                if (data != null) { //만약 데이타가 아무것도 입력된 것이 아니라면
                    out.println(data); //data를   stream 형태로 변형하여 전송.  변환내용은 쓰레드에 담겨 있다.
                }
                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream(),"euc-kr")); //데이터 수신시 stream을 받아들인다.

            } catch (IOException e) {
                e.printStackTrace();
            }

//소켓에서 데이터를 읽어서 화면에 표시한다.
            try {

                data = in.readLine(); // in으로 받은 데이타를 String 형태로 읽어 data 에 저장

                output.post(new Runnable() {
                    public void run() {
                adapter.addItem(new StoreItem(data));
                adapter.notifyDataSetChanged();
                output.setText(data); //글자출력칸에 서버가 보낸 메시지를 받는다.
                Log.d("???",data);
                    }
                });
            } catch (Exception e) {
            }
        }
    }


}
