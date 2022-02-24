package com.grean.testtools.device;

import android.util.Log;

import com.ComReceiveProtocol;
import com.SerialCommunication;
import com.SerialCommunicationController;
import com.tools;

public class ComOne extends SerialCommunication implements SerialCommunicationController {
    private ComReceiveProtocol receiveProtocol;
    private static ComOne instance = new ComOne();

    private ComOne(){
        super(1,115200,0);
    }

    public static ComOne getInstance() {
        return instance;
    }

    @Override
    protected boolean checkRecBuff() {
        return true;
    }

    @Override
    protected void communicationProtocol(byte[] rec, int size, int state) {
       // Log.d("ComOne", "sync"+tools.bytesToHexString(rec,size));
        if(receiveProtocol!=null){
            receiveProtocol.receiveProtocol(rec,size,state);
        }
    }

    @Override
    protected void asyncCommunicationProtocol(byte[] rec, int size) {
        //Log.d("ComOne", "async"+tools.bytesToHexString(rec,size));
        if(receiveProtocol!=null){
            receiveProtocol.receiveAsyncProtocol(rec,size);
        }
    }

    @Override
    public void send(byte[] buff, int state) {
        addSendBuff(buff,state);
    }

    @Override
    public void setComReceiveProtocol(ComReceiveProtocol comReceiveProtocol) {
        this.receiveProtocol = comReceiveProtocol;
    }
}
