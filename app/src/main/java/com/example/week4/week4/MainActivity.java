package com.example.week4.week4;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements Runnable {
    TextView tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.textViewOldestAge);
        Button b = (Button)findViewById(R.id.bevestig);
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                tv.setText("wachten...");
                new Thread(MainActivity.this).start();

            }
        });
    }

    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg){
            String s = msg.getData().getString("bericht");
            tv.setText(s);
        }
    };

    public void run() {
        try {

            URL url = new URL(getResources().getString(R.string.url));
            HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
            InputStream in = httpcon.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String s = reader.readLine();
            Message msg = new Message();
            msg.getData().putString("bericht", s);
            myHandler.sendMessage(msg);
        }
        catch(IOException ioe){
            Log.e("MainActivity", ioe.toString());
        }

    }
}