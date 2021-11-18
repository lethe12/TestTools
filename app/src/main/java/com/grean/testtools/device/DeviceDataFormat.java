package com.grean.testtools.device;

public class DeviceDataFormat {
    private float[] ain = new float[8];
    private boolean[] din =new boolean[32];

    private float calcVoltage(int a){
        return (float) (a-32768)/3276.8f;
    }

    public void setAin (int index,int a){
        if((index<ain.length)&&(index >= 0)){
            ain[index] = calcVoltage(a);
        }
    }

    public static int voltageToInt(float a){
        if((a>=-10)&&(a<=10)){
            return (int)(a*3276.8f)+32768;
        }else{
            return 32768;
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
