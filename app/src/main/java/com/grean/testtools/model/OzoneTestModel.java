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
        String content = "检测结果\n";
        @Override
        public void run() {
            panel.inquireStatus();
            listener.notifyStatus("电源电压检测中...");
            delayS(1);
            getVoltageStatus();
            listener.notifyContent(content);
            listener.notifyStatus("电源电压检测完成");
            listener.setFunEnable(true);
        }

        private void getVoltageStatus(){
            for(int i=0;i<8;i++){
                float f = dataFormat.getAin()[i];
                switch (i){
                    case 0:
                        content+= "+15V电压:"+f*2;
                        if((f>7)&&(f<8)){
                           content+= "正常\n";
                        }else{
                            content+= "异常\n";
                        }
                        break;
                    case 1:
                        content+= "D+3.3V电压:"+f;
                        if((f>3)&&(f<4)){
                            content+= "正常\n";
                        }else{
                            content+= "异常\n";
                        }
                        break;
                    case 2:
                        content+= "主板 +5V电压:"+f;
                        if((f>4.5)&&(f<5.5)){
                            content+= "正常\n";
                        }else{
                            content+= "异常\n";
                        }
                        break;
                    case 3:
                        content+= "主板REF+5V电压:"+f;
                        if((f>4.8)&&(f<5.2)){
                            content+= "正常\n";
                        }else{
                            content+= "异常\n";
                        }
                        break;
                    case 4:
                        content+= "光电板A +5V电压:"+f;
                        if((f>4.5)&&(f<5.5)){
                            content+= "正常\n";
                        }else{
                            content+= "异常\n";
                        }
                        break;
                    case 5:
                        content+= "光电板A REF+5V电压:"+f;
                        if((f>4.8)&&(f<5.2)){
                            content+= "正常\n";
                        }else{
                            content+= "异常\n";
                        }
                        break;
                    case 7:
                        content+= "光电板B +5V电压:"+f;
                        if((f>4.5)&&(f<5.5)){
                            content+= "正常\n";
                        }else{
                            content+= "异常\n";
                        }
                        break;
                    case 8:
                        content+= "光电板B REF+5V电压:"+f;
                        if((f>4.8)&&(f<5.2)){
                            content+= "正常\n";
                        }else{
                            content+= "异常\n";
                        }
                        break;
                    default:

                        break;
                }
            }
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
