package com.grean.testtools.device;

public class DeviceDataFormat {
    private float[] ain = new float[8];
    private boolean[] din =new boolean[32];

    public void setAin (int index,float a){
        if((index<ain.length)&&(index >= 0)){
            ain[index] = a;
        }
    }

    public void setDin(int index,boolean d){
        if((index<din.length)&&(index >= 0)){
            din[index] = d;
        }
    }

    public float[] getAin() {
        return ain;
    }

    public boolean[] getDin() {
        return din;
    }
}
