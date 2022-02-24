package com.grean.testtools;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.grean.testtools.model.OzoneTestModel;

public class MainActivity extends AppCompatActivity implements TestListener, View.OnClickListener {
    private Button btnPowerTest,btnFunTest;
    private TextView tvStatus,tvContent;
    private OzoneTestModel model;
    private static final int MSG_ENABLE_ALL_FUN =1,MSG_DISABLE_ALL_FUN = 2,MSG_NOTIFY_STATUS = 3,MSG_NOTIFY_CONTENT = 4;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case MSG_DISABLE_ALL_FUN:
                    btnFunTest.setEnabled(false);
                    btnPowerTest.setEnabled(false);
                    break;
                case MSG_ENABLE_ALL_FUN:
                    btnPowerTest.setEnabled(true);
                    btnFunTest.setEnabled(true);
                    break;
                case MSG_NOTIFY_CONTENT:
                    tvContent.setText((String)msg.obj);
                    break;
                case MSG_NOTIFY_STATUS:
                    tvStatus.setText((String)msg.obj);
                    break;
                default:

                    break;
            }

            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnFunTest = findViewById(R.id.btnOzoneFunTest);
        btnPowerTest = findViewById(R.id.btnOzonePowerTest);
        btnPowerTest.setOnClickListener(this);
        btnFunTest.setOnClickListener(this);
        tvContent = findViewById(R.id.tvTestContent);
        tvStatus = findViewById(R.id.tvTestStatus);

        findViewById(R.id.btnTest).setOnClickListener(this);

        model = new OzoneTestModel(this);

    }

    @Override
    public void notifyStatus(String string) {
        Message msg = new Message();
        msg.obj = string;
        msg.what = MSG_NOTIFY_STATUS;
        handler.sendMessage(msg);
    }

    @Override
    public void notifyContent(String string) {
        Message msg = new Message();
        msg.obj = string;
        msg.what = MSG_NOTIFY_CONTENT;
        handler.sendMessage(msg);
    }

    @Override
    public void setFunEnable(boolean enable) {
        if(enable){
            handler.sendEmptyMessage(MSG_ENABLE_ALL_FUN);
        }else{
            handler.sendEmptyMessage(MSG_DISABLE_ALL_FUN);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnOzoneFunTest:
                model.startFunTest();
                break;
            case R.id.btnOzonePowerTest:
                model.startPowerTest();
                break;
            case R.id.btnTest:
                model.startDebugTest();
                break;
            default:

                break;


        }
    }
}