package com.grean.testtools.model;

import com.grean.testtools.TestListener;
import com.grean.testtools.device.ControlPanel;
import com.grean.testtools.device.DeviceDataFormat;
import com.grean.testtools.device.DeviceManage;

public class OzoneTestModel {
    private TestListener listener;
    private DeviceDataFormat dataFormat;
    private ControlPanel panel;

    public OzoneTestModel(TestListener listener){
        this.listener = listener;
        dataFormat = DeviceManage.getInstance().getDataFormat();
        panel = DeviceManage.getInstance().getPanel();
    }

    public void startPowerTest(){
        listener.setFunEnable(false);
        new OzonePowerTestThread().start();

    }

    private void delayS(int i){
        try {
            Thread.sleep(i*1000+100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private class OzonePowerTestThread extends Thread{
        @Override
        public void run() {
            listener.notifyStatus("电源电压检测中...");
            listener.notifyContent("all power is ok");
            delayS(5);
            listener.notifyStatus("电源电压检测完成");
            listener.setFunEnable(true);
        }
    }

    private class OzoneFunTestThread extends Thread{
        @Override
        public void run() {
            listener.notifyStatus("臭氧功能检测中...");
            listener.notifyContent("all fun is ok");
            delayS(5);
            listener.notifyStatus("功能检测完成");
            listener.setFunEnable(true);
        }
    }

    public void startFunTest(){
        listener.setFunEnable(false);
        new OzoneFunTestThread().start();
    }


}
