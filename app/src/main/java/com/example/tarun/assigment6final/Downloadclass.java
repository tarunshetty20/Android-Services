package com.example.tarun.assigment6final;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;


class Downloadclass extends AsyncTask<String, Integer, Boolean> {
    private String mdown_link;
    private Context mContext;

    Downloadclass(Context contextIn) {
        mContext = contextIn;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        if(params.length > 0) {
            mdown_link = params[0];
            DataInputStream input = null;
            DataOutputStream output = null;
            try {
                URL url = new URL(mdown_link);
                URLConnection conn = url.openConnection();
                int contentLength = conn.getContentLength();
                input = new DataInputStream(url.openStream());

                String fileName = Abc.getFileNameFromURL(mdown_link);
                File outFile = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS), fileName);
                if(!outFile.exists())
                    outFile.createNewFile();
                output = new DataOutputStream(new FileOutputStream(outFile));

                byte[] byte1 = new byte[4096];
                long bytes = 0;
                int progress = 0;
                while(bytes < contentLength) {
                    int bytesread = input.read(byte1);
                    bytes += (long)bytesread;
                    output.write(byte1, 0, bytesread);
                    output.flush();
                    int percentageComplete = (int)(((double) bytes/contentLength) * 100);
                    if((percentageComplete - progress) >= 1) {

                        publishProgress(percentageComplete);
                        progress = percentageComplete;
                    }
                }
            } catch(FileNotFoundException e) {

                return false;
            } catch (IOException e) {

                return false;
            } finally {
                try {

                    if(input != null)
                        input.close();
                    if(output != null)
                        output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return true;
        } else
            return false;
    }

    @Override
    protected void onProgressUpdate(Integer... update) {

        Intent a = new Intent(Fixedvariables.downloadinprogress);
        a.putExtra(Fixedvariables.downloadurlkey, mdown_link);
        if(update.length > 0)
            a.putExtra(Fixedvariables.downloadkey, update[0]);
        if(mContext != null)
            mContext.sendBroadcast(a);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        Intent intent;
        if(result) {

            intent = new Intent(Fixedvariables.downloadcompleted);
            intent.putExtra(Fixedvariables.downloadurlkey, mdown_link);
        } else {

            intent = new Intent(Fixedvariables.downloadfailed);
            intent.putExtra(Fixedvariables.downloadurlkey, mdown_link);
        }
        if(mContext != null)

            mContext.sendBroadcast(intent);
    }
}