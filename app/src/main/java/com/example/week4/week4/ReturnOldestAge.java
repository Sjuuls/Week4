package com.example.week4.week4;

/**
 * Created by Julien on 2-3-2016.
 */
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReturnOldestAge extends Activity{

    private BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mAdapter == null){
            setContentView(R.layout.activity_main);
            return;
        }
        setContentView(R.layout.activity_main);
        Button startButton = (Button) findViewById(R.id.bevestig);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                startService();
                TextView tv =  (TextView) findViewById(R.id.textViewOldestAge);
                tv.setText("" + 0);
            }
        });
/*
        Button stopButton = (Button)findViewById(R.id.stopbutton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                stopService(new Intent(HitCountServiceMonitor.this,
                        HitCountService.class));
            }
        });
*/
        IntentFilter filter = new IntentFilter("HITCOUNT_UPDATE");
        registerReceiver(myReceiver, filter);
    }

    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    private int REQUEST_DISCOVERABLE = 234;

    public void startService(){
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivityForResult(discoverableIntent, REQUEST_DISCOVERABLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_DISCOVERABLE && resultCode != RESULT_CANCELED){
            startService(new Intent(this, MainActivity.class));
        }

    }

    private BroadcastReceiver  myReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            int i = intent.getIntExtra("aantal", 0);
            TextView tv =  (TextView) findViewById(R.id.textViewOldestAge);
            tv.setText("" + i);
        }
    };






}
