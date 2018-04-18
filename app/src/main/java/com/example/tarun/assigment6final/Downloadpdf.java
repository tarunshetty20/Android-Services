package com.example.tarun.assigment6final;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class Downloadpdf extends Activity {

    Boundservice mService;
    boolean mBound;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Boundservice.LocalBinder binder = (Boundservice.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
    }

    protected void onStart() {
        super.onStart();
        Intent i = new Intent(this, Boundservice.class);
        bindService(i, mConnection, Context.BIND_AUTO_CREATE);
        Toast.makeText(this, "Trying to bind to Bound Service", Toast.LENGTH_SHORT).show();
    }
    public void downloadpdf(View v) {
        String link1 = ((TextView) findViewById(R.id.link1)).getText().toString();
        String link2 = ((TextView) findViewById(R.id.link2)).getText().toString();
        String link3 = ((TextView) findViewById(R.id.link3)).getText().toString();
        String link4 = ((TextView) findViewById(R.id.link4)).getText().toString();
        String link5 = ((TextView) findViewById(R.id.link5)).getText().toString();

        Intent a = new Intent(this, Starttheservice.class);
        a.putExtra( Fixedvariables.downloadurlkey, new String[] {link1, link2, link3, link4, link5});
        startService(a);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }
    public void downloadbound(View v) {
        String link6 = ((TextView) findViewById(R.id.link1)).getText().toString();
        String link7 = ((TextView) findViewById(R.id.link2)).getText().toString();
        String link8 = ((TextView) findViewById(R.id.link3)).getText().toString();
        String link9 = ((TextView) findViewById(R.id.link4)).getText().toString();
        String link10 = ((TextView) findViewById(R.id.link5)).getText().toString();

        if(mBound)

            mService.downloadFiles(new String[] {link6,link7,link8,link9,link10});
    }

    @Override
    protected void onDestroy() {

        this.finish();
        super.onDestroy();

    }


}