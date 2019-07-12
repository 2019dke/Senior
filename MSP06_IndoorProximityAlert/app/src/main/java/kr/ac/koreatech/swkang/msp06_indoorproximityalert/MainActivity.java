package kr.ac.koreatech.swkang.msp06_indoorproximityalert;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    boolean isPermitted = false;
    final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    TextView ap1;
    TextView ap2;
    TextView ap3;
    Button btn1;
    Button btn2;
    Button btn3;
    Vibrator vib;
    EditText placeName;

    WifiManager wifiManager;
    boolean isWifiSearch=false;
    boolean isWifis=false;

    List<ScanResult> scanResultList;

    int cnt = 1;
    ArrayList<String> wifiSearch = new ArrayList<>();
    int prech = 0;
    AlertService ms;
    boolean isService = false; // 서비스 중인 확인용

    //서비스와 데이터를 주고 받을 수 있도록 연결해주는 객체
    //(정태윤 학생이 서비스커넥션에 대한 내용과 블로그 알려줌)
    ServiceConnection conn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
// 서비스와 연결되었을 때 호출되는 메서드
// 서비스 객체를 전역변수로 저장
            AlertService.MyBinder mb = (AlertService.MyBinder) service;
            ms = mb.getService(); // 서비스가 제공하는 메소드 호출하여
// 서비스쪽 객체를 전달받을수 있슴
            isService = true;
        }
        public void onServiceDisconnected(ComponentName name) {
// 서비스와 연결이 끊겼을 때 호출되는 메서드
            isService = false;
        }
    };

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(isWifis) {
                isWifis=false;
                if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
                    getWifiInfo();
            }
            //슬립을 넣어주어야 하는지.
            if(isService){
                //AlertService에서 어느 장소에 가까이 있는지, 없는지에 대한 변수를 받아옴
                int a = ms.getJi();
                //각 장소에 들어온 경우, 장소에 맞는 버튼을 파랗게 해줌.
                if(a == 1) {
                    btn1.setBackgroundColor(Color.BLUE);
                    btn2.setBackgroundColor(Color.GRAY);
                    btn3.setBackgroundColor(Color.GRAY);
                }
                else if (a==2){
                    btn1.setBackgroundColor(Color.GRAY);
                    btn2.setBackgroundColor(Color.BLUE);
                    btn3.setBackgroundColor(Color.GRAY);

                }
                else if (a==3){
                    btn1.setBackgroundColor(Color.GRAY);
                    btn2.setBackgroundColor(Color.GRAY);
                    btn3.setBackgroundColor(Color.BLUE);

                }
                //없다면 모두 회색
                else if (a==0){
                    btn1.setBackgroundColor(Color.GRAY);
                    btn2.setBackgroundColor(Color.GRAY);
                    btn3.setBackgroundColor(Color.GRAY);

                }
                //어떤 장소에 들어갔다면 진동을 준다. 그리고 반복하지 않도록 변수를 이용한다.
                if(prech != a && a != 0){
                    prech=a;
                    vib.vibrate(1000);
                }
                //어떤 장소에도 포함되지 않는다면 다른 진동을 준다. 반복하지 않도록 변수를 이용한다.
                else if(prech !=a ){
                    prech = a;
                    long[] pattern = {0,200,100,200};
                    vib.vibrate(pattern,-1);
                }
            }
        }
    };

    // RSSI 값이 가장 큰 AP 정보를 얻기 위한 메소드
    private void getWifiInfo() {
        if (placeName.getText().toString().equals("") ) {
            Toast.makeText(this, "Input your place name", Toast.LENGTH_LONG).show();
        }else {
            if (cnt <= 3) {
                //장소를 등록하는것으로 ssid,bssid, 몇번째 장소인지 까지를 String array에 담는다.
                scanResultList = wifiManager.getScanResults();
                String tmp;
                for (int i = 0; i < scanResultList.size(); i++) {
                    ScanResult res = scanResultList.get(i);
                    tmp = res.SSID + res.BSSID + cnt;
                    wifiSearch.add(tmp);
                }
                tmp = placeName.getText().toString();
                //텍스트에 입력된 장소를 넣어 준다.
                if (cnt == 1) {ap1.setText(tmp);btn1.setText(tmp);}
                if (cnt == 2) {ap2.setText(tmp);btn2.setText(tmp);}
                if (cnt == 3) {ap3.setText(tmp);btn3.setText(tmp);}

                Toast.makeText(this, cnt+"번째 장소 등록완료", Toast.LENGTH_LONG).show();
                cnt += 1;

            } else {
                Toast.makeText(this, "이미 3가지 장소를 다 등록했습니다!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestRuntimePermission();

        ap1 = (TextView)findViewById(R.id.ap1);
        ap2 = (TextView)findViewById(R.id.ap2);
        ap3 = (TextView)findViewById(R.id.ap3);
        btn1= (Button)findViewById(R.id.btn1);
        btn2= (Button)findViewById(R.id.btn2);
        btn3= (Button)findViewById(R.id.btn3);
        vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        placeName = (EditText)findViewById(R.id.placeName);

        btn1.setBackgroundColor(Color.GRAY);
        btn2.setBackgroundColor(Color.GRAY);
        btn3.setBackgroundColor(Color.GRAY);
        wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        if(wifiManager.isWifiEnabled() == false)
            wifiManager.setWifiEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // wifi scan 결과 수신을 위한 BroadcastReceiver 등록
        IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // wifi scan 결과 수신용 BroadcastReceiver 등록 해제
        unregisterReceiver(mReceiver);
    }

    public void onClick(View view) {
        if(view.getId() == R.id.start) {
            // Start wifi scan 버튼이 눌렸을 때

            if(isPermitted) {
                // wifi 스캔 시작
                isWifiSearch=true;
                isWifis = true;
                wifiManager.startScan();

            } else {
                Toast.makeText(getApplicationContext(),
                        "Location access 권한이 없습니다..", Toast.LENGTH_LONG).show();
            }
        }
        else if(view.getId() == R.id.startAlert) {
            // Start proximity alert 버튼이 눌렸을 때
            Intent intent =new Intent(MainActivity.this,AlertService.class);
            bindService(intent,conn,Context.BIND_AUTO_CREATE);

            // placeName이라는 변수로 참조하는 EditText에 쓰여진 장소 이름으로 proximity alert을 등록한다

            // proximity alert을 주는 것은 Service로 구현
            // Service를 AlertService라는 이름의 클래스로 구현하고 startService 메소드를 호출하여 이 Service를 시작
            if(isWifiSearch) {

                    Intent in = new Intent(this, AlertService.class);
                    in.putStringArrayListExtra("place",wifiSearch);
                    // 위에서 key 값으로 쓰인 String 값은 여러 곳에서 반복해서 사용된다면
                    // String 상수로 정의해 놓고 사용하는 것이 좋음
                    // 이 예제에서는 AlertService에서 쓰임

                    startService(in);
                    placeName.setText("");
            }
        }
        else if(view.getId() == R.id.stopAlert) {
            // Stop proximity alert 버튼이 눌렸을 때
            // AlertService 동작을 중단
            stopService(new Intent(this, AlertService.class));
        }
    }

    private void requestRuntimePermission() {
        //*******************************************************************
        // Runtime permission check
        //*******************************************************************
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        } else {
            // ACCESS_FINE_LOCATION 권한이 있는 것
            isPermitted = true;
        }
        //*********************************************************************
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // read_external_storage-related task you need to do.

                    // ACCESS_FINE_LOCATION 권한을 얻음
                    isPermitted = true;

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    // 권한을 얻지 못 하였으므로 location 요청 작업을 수행할 수 없다
                    // 적절히 대처한다
                    isPermitted = false;

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
