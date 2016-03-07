package com.example.week4.week4;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import android.net.Uri;
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
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends Activity implements Runnable {
    TextView tv;
    EditText etname, etage;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.textViewOldestAge);
        etage = (EditText) findViewById(R.id.editTextAge);
        etname = (EditText) findViewById(R.id.editTextName);
        Button b = (Button) findViewById(R.id.bevestig);
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                tv.setText("wachten...");
                new Thread(MainActivity.this).start();

            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            String s = msg.getData().getString("bericht");
            tv.setText(s);
        }
    };

    public void run() {
        try {

            String par = "?naam=" + etname.getText() + "&leeftijd=" + etage.getText();
            String s = "http://145.89.119.34:8080/oldestperson" + par;
            URL url = new URL(s);
            HttpURLConnection con =  (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            //of con.setDoOutput(true);
//            int contentLength = par.length();
//
//            con. setFixedLengthStreamingMode(contentLength);
//            OutputStream out = con.getOutputStream();
//            out.write(par.getBytes());
            InputStream in = con.getInputStream();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(in));
            String result = reader.readLine();
            Message msg = new Message();
            msg.getData().putString("bericht", result);
            myHandler.sendMessage(msg);

            con.disconnect();
        } catch (IOException ioe) {
            Log.e("MainActivity", ioe.toString());
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.week4.week4/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.week4.week4/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}