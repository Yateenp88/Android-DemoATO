package com.yp.demoato;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    //Internet check start
    NetworkCheckerListener networkCheckerListener = new NetworkCheckerListener();
    //Internet check end


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }






    @Override
    protected void onStart() {
        //Internet check start------
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkCheckerListener, filter);
        //Internet check end--------
        super.onStart();
    }

    @Override
    protected void onStop() {
        //Internet check start------
        unregisterReceiver(networkCheckerListener);
        //Internet check end--------
        super.onStop();
    }
}