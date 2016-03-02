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

public class MainActivity extends Service {

    private BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
    private AcceptThread mAcceptThread;
    private int counter = 0;

    String ns = Context.NOTIFICATION_SERVICE;
    NotificationManager mNotificationManager;

    private final int SERVICE_RUNNINNG = 1021;
    private final int BT_NOT_SUPPORTED = 1022;
    private final int BT_NOT_ENABLED = 1023;

    @Nullable
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate(){
        Log.i("MainActivity", "onCreate");
        mNotificationManager = (NotificationManager) getSystemService(ns);
        super.onCreate();
        showNotification(SERVICE_RUNNINNG);
        mAcceptThread = new AcceptThread();
        mAcceptThread.start();

    }


    public void onDestroy(){
        super.onDestroy();
        if(mAcceptThread != null)
            mAcceptThread.cancel();
        mNotificationManager.cancel(SERVICE_RUNNINNG);
    }


    private void showNotification(int NOTIFICATION_ID){
        String title = getResources().getString(R.string.oktitle);
        String message = getResources().getString(R.string.okmessage);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setContentTitle(title)
                        .setContentText(message);
        Intent monitorIntent = new Intent(this, ReturnOldestAge.class);

        PendingIntent monitorPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        monitorIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(monitorPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // NOTIFICATION_ID allows you to update the notification later on.
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;
        private final String NAME = "Week4";
        private final UUID MY_UUID = new UUID(0xffffffffffffffffL,0x1L);

        public AcceptThread() {
            // Use a temporary object that is later assigned
            // to mmServerSocket, because mmServerSocket is final
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client
                Log.i("MainActivity", "uuid = " + MY_UUID);
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME,MY_UUID);
            } catch (IOException e) { }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    Log.i("MainActivity", "accepting");
                    socket = mmServerSocket.accept();
                    counter++;
                    String output = "U bent nummer: " + counter + "\n";
                    Log.i("MainActivity", output);

                    OutputStream os = socket.getOutputStream();
                    os.write(output.getBytes());
                    os.flush();

                    Intent intent = new Intent("HITCOUNT_UPDATE");
                    intent.putExtra("aantal", counter);
                    MainActivity.this.getApplicationContext().sendBroadcast(intent);

                } catch (IOException e) {
                    Log.e("MainActivity", e.toString());
                    break;
                }

            }
        }

        /** Will cancel the listening socket, and cause thread to finish */
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) { }
        }
    }//AcceptThread

}
