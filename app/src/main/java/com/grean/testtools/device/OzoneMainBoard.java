package com.grean.testtools.device;

import android.util.Log;

import com.ComReceiveProtocol;
import com.SerialCommunicationController;
import com.tools;

public class OzoneMainBoard implements ComReceiveProtocol,OzoneControlPanel{
    private OzoneDataFormat format;
    protected SerialCommunicationController com;

    public OzoneMainBoard(SerialCommunicationController communicationController,OzoneDataFormat format){
        this.format = format;
        this.com = communicationController;
        this.com.setComReceiveProtocol(this);
    }


    private void getRealTimeData(byte[] rec){
        format.setCellTemp(tools.byte2int(rec,3));
        format.setAirTemp(tools.byte2int(rec,5));
        format.setFlowA(tools.byte2int(rec,7));
        format.setFlowB(tools.byte2int(rec,9));
        format.setPressure(tools.byte2int(rec,11));
        format.setPhotoA(tools.byte2int(rec,13));
        format.setPhotoB(tools.byte2int(rec,15));
    }

    @Override
    public void receiveProtocol(byte[] rec, int size, int state) {
        if(tools.checkFrameWithAddr(rec,size, (byte) 0x03)) {
            Log.d("OzoneMainBoard",tools.bytesToHexString(rec,size));
            switch (state){
                case STATUS_INQUIRE:
                    getRealTimeData(rec);
                    break;
                default:

                    break;
            }


        }
    }

    @Override
    public void receiveAsyncProtocol(byte[] rec, int size) {

    }

    @Override
    public void setDebugMode() {
        Log.d("OzoneMainBoard","setDebugMode");
        com.send(tools.setModBusOneRegister((byte) 0x03, 0x4001, 0), STATUS_OTHER);
    }

    @Override
    public void setInitMode() {
        Log.d("OzoneMainBoard","setInitMode");
        com.send(tools.setModBusOneRegister((byte) 0x03, 0x4002, 2), STATUS_OTHER);

    }

    @Override
    public void inquire() {
        Log.d("OzoneMainBoard","inquire");
        com.send(tools.getModBusRegisters((byte) 0x03,0x2001,14),STATUS_INQUIRE);

    }

    @Override
    public void setPwm(int value) {
        com.send(tools.setModBusOneRegister((byte) 0x03, 0x1006, value),STATUS_OTHER);
    }

    @Override
    public void setRelay(int channel, boolean key) {
        if((channel>0)&&(channel<6)){
            if(key){
                com.send(tools.setModBusOneRegister((byte) 0x03, 0x1000+channel, 0xffff), STATUS_OTHER);
            }else{
                com.send(tools.setModBusOneRegister((byte) 0x03, 0x1000+channel, 0x0), STATUS_OTHER);
            }
        }else{
            return;
        }
    }
}
