package com.grean.testtools.device;

import com.ComReceiveProtocol;
import com.SerialCommunication;
import com.SerialCommunicationController;

public class ComThree extends SerialCommunication implements SerialCommunicationController {
    private ComReceiveProtocol receiveProtocol;
    private static ComThree instance = new ComThree();

    public static ComThree getInstance() {
        return instance;
    }

    public ComThree() {
        super(3, 9600, 0);
    }

    @Override
    protected boolean checkRecBuff() {
        return true;
    }

    @Override
    protected void communicationProtocol(byte[] rec, int size, int state) {
        if(receiveProtocol!=null){
            receiveProtocol.receiveProtocol(rec,size,state);
        }
    }

    @Override
    protected void asyncCommunicationProtocol(byte[] rec, int size) {
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
