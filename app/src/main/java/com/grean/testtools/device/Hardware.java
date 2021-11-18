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
        if(tools.checkFrameWithAddr(rec,size,ADDRESS_IO)){
            for(int i=0;i<8;i++){
                byte reg = (byte) (0x01<<i);
                if((rec[3]&reg)!=0x00){
                    format.setDin(i,false);
                }else{
                    format.setDin(i,true);
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

            for(int i=16;i<24;i++){
                byte reg = (byte) (0x01<<(i-16));
                if((rec[5]&reg)!=0x00){
                    format.setDin(i,false);
                }else{
                    format.setDin(i,true);
                }
            }

            for(int i=24;i<32;i++){
                byte reg = (byte) (0x01<<(i-24));
                if((rec[6]&reg)!=0x00){
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
        com.send(tools.getModBus4xRegisters(ADDRESS_IO,631,2),STATUS_INQUIRE_RELAY_IN);
        com.send(tools.getModBus3xRegisters(ADDRESS_ANALOG_IN,0x0001,16),STATUS_INQUIRE_ANALOG_IN);
    }

    @Override
    public void setAnalogOut(int channel, float value) {
        com.send(tools.setModBusOneRegister(ADDRESS_ANALOG_OUT,0x0001,4095),STATUS_SET_ANALOG_OUT);
    }

    @Override
    public void setRelayOut(int channel, boolean key) {
        com.send(tools.setModBusOneRegister(ADDRESS_IO,561,4095),STATUS_SET_ANALOG_OUT);
    }
}
