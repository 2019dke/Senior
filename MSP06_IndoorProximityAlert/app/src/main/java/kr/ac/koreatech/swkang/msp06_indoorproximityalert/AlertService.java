package kr.ac.koreatech.swkang.msp06_indoorproximityalert;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AlertService extends Service {

    private static final String TAG = "AlertService";

    WifiManager wifiManager;
    List<ScanResult> scanList;

    Timer timer = new Timer();
    TimerTask timerTask = null;
    ArrayList<String> wifiS = new ArrayList<>();

    IBinder mBinder = new MyBinder();
    int ji=0;

    class MyBinder extends Binder {
        AlertService getService() { // 서비스 객체를 리턴
            return AlertService.this;
        }
    }


    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
                checkProximity();
        }
    };

    public int getJi() {
        return ji;
    }

    // 등록 장소 근접 여부를 판단하고 그에 따라 알림을 주기 위한 메소드
    private void checkProximity() {
        //몇번째 지역인지 알려주는 변수, 아무곳도 아니라면 0이다.
        ji=0;
        scanList = wifiManager.getScanResults();
        boolean isProximate = false;
        //allCnt는 합집합의 개수 이다.
        double[] allCnt = {0,0,0,0};
        //locCnt는 교집합의 개수 이다.
        double[] locCnt = {0,0,0,0};

        //장소를 저장한 것의 개수를 allCnt에 넣어준다.
        for(int i=0;i<wifiS.size();i++){ allCnt[Integer.parseInt("" + wifiS.get(i).charAt(wifiS.get(i).length()-1))]+=1; }


        for(int i = 0; i < scanList.size(); i++) {

            ScanResult result = scanList.get(i);
            String tmp = result.SSID+result.BSSID;
            //분모가 될 변수에 +1를 해준다.
            allCnt[1]+=1;
            allCnt[2]+=1;
            allCnt[3]+=1;
            for(int j=1; j<=3;j++){
                for(int k=0;k<wifiS.size();k++){
                    //같은 것이 있다면 교집합으로 하나빼준다 A+B-AnB
                    if( (tmp+j).equals(wifiS.get(k))) {locCnt[j] += 1;allCnt[j]-=1;}
                }
            }
        }

        //allCnt[Integer.parseInt("" + wifiS.get(k).charAt(wifiS.get(k).length()-1))]+=1;
        //Log.d("1", "1 : "+locCnt[1] + " ,"+locCnt[2] + " ,"+locCnt[3] );
        for(int i=1;i<=3;i++){
            if(allCnt[i]!=0){ locCnt[i] = locCnt[i]/allCnt[i]; }
            else{locCnt[i]=0;}
        }
        //가장 큰 값을 저장한다.
        double maxi = locCnt[1];
        ji=1;
        for(int i=2;i<=3;i++){
            if(maxi<locCnt[i]){
                ji=i;
                maxi=locCnt[i];
            }
        }
        //만약 가장 큰값이 0.3 이하라면 어떤 위치에도 있지 않는다.
        if(maxi <= 0.3){ji=0;}
            //long[] pattern = {0, 200, 100, 200, 100, 200};
            //vib.vibrate(pattern, -1);
            //Toast.makeText(this, "** " + placeName + "에 있거나 그 근처에 있습니다 **", Toast.LENGTH_SHORT).show();
            //vib.vibrate(200);

        Log.d("1", "1 : "+locCnt[1] + " ,"+locCnt[2] + " ,"+locCnt[3] );
        Toast.makeText(this, "** " + locCnt[1] + "**" + locCnt[2]+"**" +locCnt[3], Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate()");


        wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);

        IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(mReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // intent: startService() 호출 시 넘기는 intent 객체
        // flags: service start 요청에 대한 부가 정보. 0, START_FLAG_REDELIVERY, START_FLAG_RETRY
        // startId: start 요청을 나타내는 unique integer id

        Toast.makeText(this, "AlertService 시작", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onStartCommand()");

        // 넘어온 intent에서 등록 장소 및 AP 정보 추출
        wifiS = intent.getStringArrayListExtra("place");
        // 주기적으로 wifi scan 수행하기 위한 timer 가동
        startTimerTask();

        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        Toast.makeText(this, "AlertService 중지", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onDestroy()");

        stopTimerTask();
        unregisterReceiver(mReceiver);
    }

    private void startTimerTask() {
        // TimerTask 생성한다
        timerTask = new TimerTask() {
            @Override
            public void run() {
                wifiManager.startScan();
            }
        };

        // TimerTask를 Timer를 통해 실행시킨다
        timer.schedule(timerTask, 1000, 10000); // 1초 후에 타이머를 구동하고 10초마다 반복한다
        //*** Timer 클래스 메소드 이용법 참고 ***//
        // 	schedule(TimerTask task, long delay, long period)
        // http://developer.android.com/intl/ko/reference/java/util/Timer.html
        //***********************************//
    }

    private void stopTimerTask() {
        // 1. 모든 태스크를 중단한다
        if(timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }
}
