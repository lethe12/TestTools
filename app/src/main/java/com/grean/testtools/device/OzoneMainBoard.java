package com.grean.testtools.device;

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

    }

    @Override
    public void inquire() {
        com.send(tools.getModBusRegisters((byte) 0x03,0x2001,14),STATUS_INQUIRE);

    }

    @Override
    public void setPwm(int value) {

    }

    @Override
    public void setRelay(int channel, boolean key) {

    }
}
