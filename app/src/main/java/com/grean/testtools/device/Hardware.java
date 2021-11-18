package com.grean.testtools.device;

import com.ComReceiveProtocol;
import com.SerialCommunicationController;
import com.tools;

public class Hardware implements ComReceiveProtocol ,ControlPanel{
    private DeviceDataFormat format;
    protected SerialCommunicationController com;
    public Hardware(SerialCommunicationController communicationController,DeviceDataFormat format){
        this.format = format;
        this.com = communicationController;
        this.com.setComReceiveProtocol(this);
    }

    @Override
    public void receiveProtocol(byte[] rec, int size, int state) {

    }

    @Override
    public void receiveAsyncProtocol(byte[] rec, int size) {

    }

    @Override
    public void inquireStatus() {
        com.send(tools.getModBusRegisters(ADDRESS_IO,0x0001,));
    }

    @Override
    public void setAnalogOut(int channel, float value) {

    }

    @Override
    public void setRelayOut(int channel, boolean key) {

    }
}
