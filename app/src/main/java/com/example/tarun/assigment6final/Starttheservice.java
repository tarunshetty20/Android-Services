package com.example.tarun.assigment6final;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;



public class Starttheservice extends Service {

    private int mdownloadlink;
    private String[] murl;
    private Toast mToast;

    private BroadcastReceiver downloadprogress = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int progress = intent.getIntExtra(Fixedvariables.downloadkey, 0);
            String url = intent.getStringExtra(Fixedvariables.downloadurlkey);
            if (url != null) {

                mToast.setText("Download in progress");
                mToast.show();
            }
        }
    };

    private BroadcastReceiver downloadcomplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String url = intent.getStringExtra(Fixedvariables.downloadurlkey);
            if(url != null) {

                mToast.setText("Download complete");
                mToast.show();
            }
            mdownloadlink++;
            if(mdownloadlink == murl.length)
                finish();
            else
                nextfiletodownload();
        }
    };

    private BroadcastReceiver downloadfailed = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String url = intent.getStringExtra(Fixedvariables.downloadurlkey);
            if(url != null) {
                mToast.setText("Download failed");
                mToast.show();
            }
            mdownloadlink++;
            if(mdownloadlink == murl.length)
                finish();
            else
                nextfiletodownload();
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
    }

    private void nextfiletodownload() {
        new Downloadclass(getBaseContext()).execute(murl[mdownloadlink]);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        murl = intent.getStringArrayExtra(Fixedvariables.downloadurlkey);
        mdownloadlink = 0;

        registerReceiver(downloadprogress, new IntentFilter(Fixedvariables.downloadinprogress));
        registerReceiver(downloadcomplete, new IntentFilter(Fixedvariables.downloadcompleted));
        registerReceiver(downloadfailed, new IntentFilter(Fixedvariables.downloadfailed));

        nextfiletodownload();

        return START_STICKY;
    }


    private void finish() {
        unregisterReceiver(downloadprogress);
        unregisterReceiver(downloadcomplete);
        unregisterReceiver(downloadfailed);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

}
