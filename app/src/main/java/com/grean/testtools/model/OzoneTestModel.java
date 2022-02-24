package com.grean.testtools.model;

import android.util.Log;

import com.grean.testtools.TestListener;
import com.grean.testtools.device.ControlPanel;
import com.grean.testtools.device.DeviceDataFormat;
import com.grean.testtools.device.DeviceManage;
import com.grean.testtools.device.OzoneControlPanel;
import com.grean.testtools.device.OzoneDataFormat;

public class OzoneTestModel {
    private static final String tag = "OzoneTestModel";
    private TestListener listener;
    private DeviceDataFormat dataFormat;
    private ControlPanel panel;
    private OzoneControlPanel ozoneControlPanel;
    private OzoneDataFormat ozoneDataFormat;

    public OzoneTestModel(TestListener listener){
        this.listener = listener;
        dataFormat = DeviceManage.getInstance().getDataFormat();
        panel = DeviceManage.getInstance().getPanel();
        ozoneControlPanel = DeviceManage.getInstance().getOzoneControlPanel();
        ozoneDataFormat = DeviceManage.getInstance().getOzone();
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
            for(int i=1;i<8;i++){
                float f = dataFormat.getAin()[i];
                switch (i){
                    case 1:
                        content+= "D+3.3V电压:"+f;
                        if((f>3)&&(f<3.6)){
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
                    case 6:
                        content+= "光电板B +5V电压:"+f;
                        if((f>4.5)&&(f<5.5)){
                            content+= "正常\n";
                        }else{
                            content+= "异常\n";
                        }
                        break;
                    case 7:
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

    private class DebugTestThread extends Thread{
        private String content="数字输出检测\n";
        @Override
        public void run() {
            listener.notifyStatus("臭氧功能检测中...");
            panel.setAllRelay(false);
            panel.setAnalogOut(2,1f);
            panel.setAnalogOut(3,1f);
            delayS(1);
            ozoneControlPanel.setInitMode();
            delayS(8);
            ozoneControlPanel.setDebugMode();
            delayS(1);
            for(int i=0;i<=10;i++){
                panel.setAnalogOut(2,1f+i*0.1f);
                panel.setAnalogOut(3,1f+i*0.1f);
                delayS(5);
                ozoneControlPanel.inquire();
                delayS(1);
                float v = DeviceDataFormat.calcVoltage(ozoneDataFormat.getPhotoA())/5.1f;
                Log.d(tag,"A"+ozoneDataFormat.getPhotoA()+"光电管A"+v);
                content +="\nAO"+(0.5f+i*0.1f)+"光电管A电路"+v;


                v = DeviceDataFormat.calcVoltage(ozoneDataFormat.getPhotoB())/5.1f;
                Log.d(tag,"B"+ozoneDataFormat.getPhotoB()+"光电管B"+v);
                content+="光电管B电路"+v;
                listener.notifyContent(content);
            }

            listener.notifyContent(content);
            listener.notifyStatus("功能检测完成");
            listener.setFunEnable(true);
        }

        private void init(){
            ozoneControlPanel.setDebugMode();
            panel.setAllRelay(false);
            delayS(1);
            panel.setRelayOut(1,true);
            delayS(1);
            panel.setRelayOut(5,true);
            for(int i=1;i<=5;i++){
                ozoneControlPanel.setRelay(i,false);
            }
            ozoneControlPanel.setPwm(0);
            for(int i=0;i<4;i++){
                delayS(1);
                panel.setAnalogOut(i,0);
            }
        }

        private void getTempModel(){
            ozoneControlPanel.inquire();
            panel.inquireStatus();
            delayS(1);
            float temp = DeviceDataFormat.calcMainBoardVoltage(ozoneDataFormat.getCellTemp());
            Log.d(tag,"lamp temp="+temp);
            if((temp <0.22)&&(temp >0.2)){
                content+="光源温度传感器正常,";
            }else{
                content+="光源温度传感器异常,";
            }
            temp = DeviceDataFormat.calcMainBoardVoltage(ozoneDataFormat.getAirTemp());
            Log.d(tag,"air temp="+temp);
            if((temp <0.22)&&(temp >0.2)){
                content+="样品度传感器正常\n";
            }else{
                content+="样品度传感器异常\n";
            }
            delayS(1);
            panel.setAnalogOut(0,5);
            panel.setAnalogOut(1,5);
            panel.setAnalogOut(2,0.5f);
            panel.setAnalogOut(3,0.5f);
            ozoneControlPanel.setPwm(500);

            ozoneControlPanel.inquire();
        }
    }

    private class OzoneFunTestThread extends Thread{
        private String content="数字输出检测\n";

        @Override
        public void run() {
            listener.notifyStatus("臭氧功能检测中...");
            init();
            getIoModel();
            listener.notifyContent(content);
            delayS(1);
            getTempModel();
            listener.notifyContent(content);
            delayS(5);
            getAnalogIn();
            listener.notifyContent(content);
            delayS(1);
           // endStatus();
            listener.notifyContent(content);
            listener.notifyStatus("功能检测完成");
            listener.setFunEnable(true);
        }

        private void init(){
            ozoneControlPanel.setDebugMode();
            panel.setAllRelay(false);
            delayS(1);
            panel.setRelayOut(1,true);
            delayS(1);
            panel.setRelayOut(5,true);
            for(int i=1;i<=5;i++){
                ozoneControlPanel.setRelay(i,false);
            }
            ozoneControlPanel.setPwm(0);
            for(int i=0;i<4;i++){
                delayS(1);
                panel.setAnalogOut(i,0);
            }
        }

        private void getTempModel(){
            ozoneControlPanel.inquire();
            panel.inquireStatus();
            delayS(1);
            float temp = DeviceDataFormat.calcMainBoardVoltage(ozoneDataFormat.getCellTemp());
            Log.d(tag,"lamp temp="+temp);
            if((temp <0.22)&&(temp >0.2)){
                content+="光源温度传感器正常,";
            }else{
                content+="光源温度传感器异常,";
            }
            temp = DeviceDataFormat.calcMainBoardVoltage(ozoneDataFormat.getAirTemp());
            Log.d(tag,"air temp="+temp);
            if((temp <0.22)&&(temp >0.2)){
                content+="样品度传感器正常\n";
            }else{
                content+="样品度传感器异常\n";
            }
            delayS(1);
            panel.setAnalogOut(0,5);
            panel.setAnalogOut(1,5);
            panel.setAnalogOut(2,1.05f);
            panel.setAnalogOut(3,1.05f);
            ozoneControlPanel.setPwm(500);
        }

        private void getAnalogIn(){
            ozoneControlPanel.inquire();
            panel.inquireStatus();
            delayS(1);
            float v = dataFormat.getAin()[0];
            Log.d(tag,"PWM"+v);//12V/3=4V
            if((v>3.2)&&(v<4.8)){
                content+="PWM输出正常";
            }else{
                content+="PWM出输异常";
            }
            v = DeviceDataFormat.calcMainBoardVoltage(ozoneDataFormat.getPressure());
            Log.d(tag,"压力传感器"+v);
            if((v>2)&&(v<3)){
                content+="压力传感器电路正常";
            }else{
                content+="压力传感器电路异常";
            }
            v = DeviceDataFormat.calcMainBoardVoltage(ozoneDataFormat.getFlowA());
            Log.d(tag,"通道A"+v);
            if((v>2)&&(v<3)){
                content+="通道A流量电路正常";
            }else{
                content+="通道A流量电路异常";
            }
            v = DeviceDataFormat.calcMainBoardVoltage(ozoneDataFormat.getFlowB());
            Log.d(tag,"通道B"+v);
            if((v>2)&&(v<3)){
                content+="通道B流量电路正常";
            }else{
                content+="通道B流量电路异常";
            }

            v = DeviceDataFormat.calcVoltage(ozoneDataFormat.getPhotoA())/5.1f;
            Log.d(tag,"A"+ozoneDataFormat.getPhotoA()+"光电管A"+v);
            if((v>2)&&(v<3)){
                content+="\n光电管A电路正常"+v;
            }else{
                content+="\n光电管A电路异常"+v;
            }

            v = DeviceDataFormat.calcVoltage(ozoneDataFormat.getPhotoB())/5.1f;
            Log.d(tag,"B"+ozoneDataFormat.getPhotoB()+"光电管B"+v);
            if((v>2)&&(v<3)){
                content+="光电管B电路正常"+v;
            }else{
                content+="光电管B电路异常"+v;
            }

        }

        private void endStatus(){
            for(int i=0;i<4;i++){
                panel.setAnalogOut(i,0);
            }
            ozoneControlPanel.setPwm(0);
            for(int i=1;i<=5;i++){
                ozoneControlPanel.setRelay(i,false);
            }
            content+="\n测试完成";
        }

        private void getIoModel(){
            boolean [] din = {false,false,false,false,false};
            ozoneControlPanel.inquire();
            panel.inquireStatus();
            delayS(1);
            Log.d(tag,"getIoModel step 1"+content);
            for(int i=0;i<5;i++){
                if(dataFormat.getDin()[i]){
                    din[i] = true;
                    content += ";"+i+"通道低电平异常";
                }
                ozoneControlPanel.setRelay(i+1,true);
            }
            delayS(1);
            panel.inquireStatus();
            content +="\n";
            delayS(1);
            Log.d(tag,"getIoModel step 2"+content);
            for(int i=0;i<5;i++){
                if(!dataFormat.getDin()[i]){
                    din[i] = true;
                    content += ";"+i+"通道高电平异常";
                }
                ozoneControlPanel.setRelay(i+1,false);
            }
            Log.d(tag,"getIoModel step 3"+content);
            for(int i=0;i<5;i++){
                if(!din[i]){
                    content+="通道"+i+"正常";
                }
            }
            content +="\n";
            Log.d(tag,"getIoModel step 4"+content);
        }
    }

    public void startFunTest(){
        listener.setFunEnable(false);
        new OzoneFunTestThread().start();
    }


    public void startDebugTest(){
        listener.setFunEnable(false);
        new DebugTestThread().start();
    }

}
