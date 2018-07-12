package com.example.natan.startedservicecep;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyService extends Service {

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String cep = intent.getStringExtra(Intent.EXTRA_TEXT);

                    String url = "https://viacep.com.br/ws/"+ cep +"/json/";

                    URL u = new URL(url);
                    HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();

                    InputStream in = urlConnection.getInputStream();
                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

                    StringBuilder responseStrBuilder = new StringBuilder();

                    String inputStr;
                    while((inputStr = streamReader.readLine()) != null){
                        responseStrBuilder.append(inputStr);
                    }

                    JSONObject json = new JSONObject(responseStrBuilder.toString());

                    String response = json.toString();

//                    Message msg = new Message();
//                    msg.obj = response;
//
//                    MainActivity.myHandler.sendMessage(msg);

                    EventBus.getDefault().post(new Event(response));

                    stopSelf();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
