package com.grean.testtools.device;

public class OzoneDataFormat {
    private int cellTemp,airTemp,flowA,flowB,pressure,photoA,photoB;
    public static float calcVoltage(int value){
        return (float)value/13107f;
    }

    public int getCellTemp() {
        return cellTemp;
    }

    public void setCellTemp(int cellTemp) {
        this.cellTemp = cellTemp;
    }

    public int getAirTemp() {
        return airTemp;
    }

    public void setAirTemp(int airTemp) {
        this.airTemp = airTemp;
    }

    public int getFlowA() {
        return flowA;
    }

    public void setFlowA(int flowA) {
        this.flowA = flowA;
    }

    public int getFlowB() {
        return flowB;
    }

    public void setFlowB(int flowB) {
        this.flowB = flowB;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getPhotoA() {
        return photoA;
    }

    public void setPhotoA(int photoA) {
        this.photoA = photoA;
    }

    public int getPhotoB() {
        return photoB;
    }

    public void setPhotoB(int photoB) {
        this.photoB = photoB;
    }
}
