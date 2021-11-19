package com.grean.testtools.device;

import android.util.Log;

import com.ComReceiveProtocol;
import com.SerialCommunicationController;
import com.tools;

public class Hardware implements ComReceiveProtocol ,ControlPanel{
    private DeviceDataFormat format;
    private int dout = 0;
    protected SerialCommunicationController com;
    public Hardware(SerialCommunicationController communicationController,DeviceDataFormat format){
        this.format = format;
        this.com = communicationController;
        this.com.setComReceiveProtocol(this);
    }

    @Override
    public void receiveProtocol(byte[] rec, int size, int state) {
        Log.d("Hardware",tools.bytesToHexString(rec,size));
        if(tools.checkFrameWithAddr(rec,size,ADDRESS_IO)){
            for(int i=0;i<8;i++){
                byte reg = (byte) (0x01<<i);
                if((rec[3]&reg)!=0x00){
                    format.setDin(8+i,false);
                }else{
                    format.setDin(8+i,true);
                }
            }

            for(int i=8;i<16;i++){
                byte reg = (byte) (0x01<<(i-8));
                if((rec[4]&reg)!=0x00){
                    format.setDin(i,false);
                }else{
                    format.setDin(i,true);
                }
            }

        }else if(tools.checkFrameWithAddr(rec,size,ADDRESS_ANALOG_IN)){
            for(int i=0;i<8;i++){
                format.setAin(i,tools.byte2int(rec,3+i*2));
            }
        }else{

        }
    }

    @Override
    public void receiveAsyncProtocol(byte[] rec, int size) {

    }

    @Override
    public void inquireStatus() {
        Log.d("hardware","inquireStatus");
        com.send(tools.getModBus4xRegisters(ADDRESS_IO,630,1),STATUS_INQUIRE_RELAY_IN);
        com.send(tools.getModBus3xRegisters(ADDRESS_ANALOG_IN,0x0000,8),STATUS_INQUIRE_ANALOG_IN);
    }

    @Override
    public void setAnalogOut(int channel, float value) {
        if((channel>=0)&&(channel<4)) {
            com.send(tools.setModBusOneRegister(ADDRESS_ANALOG_OUT, channel, DeviceDataFormat.voltageToInt(value)), STATUS_SET_ANALOG_OUT);
        }
    }

    @Override
    public void setRelayOut(int channel, boolean key) {
        if((channel>=0)&&(channel<16)) {
            if(key){
                dout |= (0x0001<<channel);
            }else{
                dout &= (0xFFFE<<channel);
            }
            com.send(tools.setModBusOneRegister(ADDRESS_IO, 0x0230, dout), STATUS_SET_RELAY_OUT);
        }
    }

    @Override
    public void setAllRelay(boolean key) {
        if(key){
            dout = 0xffff;
            com.send(tools.setModBusOneRegister(ADDRESS_IO, 0x0230, 0xffff), STATUS_SET_RELAY_OUT);
        }else{
            dout = 0;
            com.send(tools.setModBusOneRegister(ADDRESS_IO, 0x0230, 0), STATUS_SET_RELAY_OUT);
        }
    }
}
