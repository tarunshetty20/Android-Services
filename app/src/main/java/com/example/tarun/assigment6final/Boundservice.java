package com.example.tarun.assigment6final;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class Boundservice extends Service {
    class LocalBinder extends Binder {
        Boundservice getService() {
            return Boundservice.this;
        }
    }

    private IBinder mBinder = new LocalBinder();
    private int mnewcount;
    private String[] murllist;
    private Toast mToast;

    private BroadcastReceiver downloadinprogress = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int progress = intent.getIntExtra(Fixedvariables.downloadkey, 0);
            String url = intent.getStringExtra(Fixedvariables.downloadurlkey);
            if (url != null) {
                mToast.setText("Download in Progress");
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
            mnewcount++;
            if(mnewcount == murllist.length)
                finish();
            else
                nextdownloadfile();
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
            mnewcount++;
            if(mnewcount == murllist.length)
                finish();
            else
                nextdownloadfile();
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void downloadFiles(String[] urls) {
        murllist = urls;
        mnewcount = 0;
        registerReceiver(downloadinprogress, new IntentFilter(Fixedvariables.downloadinprogress));
        registerReceiver(downloadcomplete, new IntentFilter(Fixedvariables.downloadcompleted));
        registerReceiver(downloadfailed, new IntentFilter(Fixedvariables.downloadfailed));
        nextdownloadfile();
    }

    private void nextdownloadfile() {;
        new Downloadclass(getBaseContext()).execute(murllist[mnewcount]);
    }


    private void finish() {
        unregisterReceiver(downloadinprogress);
        unregisterReceiver(downloadcomplete);
        unregisterReceiver(downloadfailed);
    }


    @Override
    public void onCreate() {
        Toast.makeText(this, "Service starting", Toast.LENGTH_SHORT).show();
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }


    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service stopping", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}
