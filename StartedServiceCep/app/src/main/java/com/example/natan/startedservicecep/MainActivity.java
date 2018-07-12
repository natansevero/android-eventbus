package com.example.natan.startedservicecep;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView mLogradouroTextView;
    private TextView mComplementoTextView;
    private TextView mBairroTextView;
    private TextView mCidadeTextView;
    private TextView mEstadoTextView;

    private EditText mCepTextView;

    private Button mBuscarButton;
    private ProgressBar mLoadingProgressBar;

//    public static Handler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogradouroTextView = (TextView) findViewById(R.id.tv_logradouro);
        mComplementoTextView = (TextView) findViewById(R.id.tv_complemento);
        mBairroTextView = (TextView) findViewById(R.id.tv_bairro);
        mCidadeTextView = (TextView) findViewById(R.id.tv_cidade);
        mEstadoTextView = (TextView) findViewById(R.id.tv_estado);

//        myHandler = new MyHandler();

        mCepTextView = (EditText) findViewById(R.id.et_cep);
        mBuscarButton = (Button) findViewById(R.id.bt_buscar);
        mLoadingProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
    }

    public void buscarCep(View view){
        mLoadingProgressBar.setVisibility(View.VISIBLE);
        mBuscarButton.setVisibility(View.INVISIBLE);

        String cep = mCepTextView.getText().toString();

        Intent intent = new Intent(this, MyService.class);
        intent.putExtra(Intent.EXTRA_TEXT, cep);
        startService(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event event){
        String response = event.getMessage();

        try {
            JSONObject jo = new JSONObject(response);

            mLogradouroTextView.setText(jo.getString("logradouro"));
            mComplementoTextView.setText(jo.getString("complemento"));
            mBairroTextView.setText(jo.getString("bairro"));
            mCidadeTextView.setText(jo.getString("localidade"));
            mEstadoTextView.setText(jo.getString("uf"));

            mLoadingProgressBar.setVisibility(View.INVISIBLE);
            mBuscarButton.setVisibility(View.VISIBLE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("CEP", response);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

//    public class MyHandler extends Handler {
//
//        @Override
//        public void handleMessage(Message msg) {
//
//            String anwser = (String) msg.obj;
//
//            try {
//                JSONObject jo = new JSONObject(anwser);
//
//                mLogradouroTextView.setText(jo.getString("logradouro"));
//                mComplementoTextView.setText(jo.getString("complemento"));
//                mBairroTextView.setText(jo.getString("bairro"));
//                mCidadeTextView.setText(jo.getString("localidade"));
//                mEstadoTextView.setText(jo.getString("uf"));
//
//                mLoadingProgressBar.setVisibility(View.INVISIBLE);
//                mBuscarButton.setVisibility(View.VISIBLE);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            Log.d("CEP", anwser);
//        }
//    }
}
