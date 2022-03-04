package com.grean.testtools.model;

import android.icu.util.LocaleData;
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
        private String content="";
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
                float v = DeviceDataFormat.calcPhotoVoltage(ozoneDataFormat.getPhotoA())/0.0408f;
                Log.d(tag,"A"+ozoneDataFormat.getPhotoA()+"光电管A"+v+"pA");
                content +="\nAO"+(i*0.1f)+"光电管A电路"+v+"pA";


                v = DeviceDataFormat.calcPhotoVoltage(ozoneDataFormat.getPhotoB())/0.0408f;
                Log.d(tag,"B"+ozoneDataFormat.getPhotoB()+"光电管B"+v+"pA");
                content+="光电管B电路"+v+"pA";
                listener.notifyContent(content);
            }

            listener.notifyContent(content);
            listener.notifyStatus("功能检测完成");
            listener.setFunEnable(true);
        }

    }

    private class OzoneFunTestThread extends Thread{
        private String content="";

        @Override
        public void run() {
            listener.notifyStatus("臭氧功能检测中...");
            listener.notifyContent(content);
            init();
            delayS(1);
            getIoModel();
            listener.notifyContent(content);
            delayS(1);
            getTempModel();
            listener.notifyContent(content);
            delayS(1);
            getAnalogIn();
            listener.notifyContent(content);
            delayS(1);
            endStatus();
            listener.notifyContent(content);
            listener.notifyStatus("功能检测完成");
            listener.setFunEnable(true);
        }

        private void init(){
            listener.notifyStatus("PCB初始化...");
            panel.setAllRelay(false);
            panel.setAnalogOut(2,1f);
            panel.setAnalogOut(3,1f);
            delayS(2);
            ozoneControlPanel.setInitMode();
            delayS(6);
            ozoneControlPanel.setDebugMode();
            listener.notifyStatus("开始检测...");
            delayS(1);
            panel.setRelayOut(1,true);
            delayS(1);
            panel.setRelayOut(5,true);
            for(int i=1;i<=5;i++){
                ozoneControlPanel.setRelay(i,false);
                delayS(1);
            }
            ozoneControlPanel.setPwm(1);
            panel.setAnalogOut(0,1f);
            panel.setAnalogOut(1,1f);
        }

        private void getTempModel(){
            listener.notifyStatus("检测温度传感器...");
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
            panel.setAnalogOut(2,2f);
            panel.setAnalogOut(3,2f);
            ozoneControlPanel.setPwm(500);
        }


        private void getAnalogIn(){
            listener.notifyStatus("检测模拟输入...");
            delayS(4);//等待电平稳定
            ozoneControlPanel.inquire();
            panel.inquireStatus();
            delayS(1);
            float v = dataFormat.getAin()[0];
            Log.d(tag,"PWM"+v);//12V/3=4V
            if((v>3.2)&&(v<4.8)){
                content+="PWM输出正常"+v+"V;";
            }else{
                content+="PWM出输异常"+v+"V;";
            }
            v = DeviceDataFormat.calcMainBoardVoltage(ozoneDataFormat.getPressure());
            Log.d(tag,"压力传感器"+v);
            if((v>2)&&(v<3)){
                content+="压力传感器电路正常"+v+"V;";
            }else{
                content+="压力传感器电路异常"+v+"V;";
            }
            v = DeviceDataFormat.calcMainBoardVoltage(ozoneDataFormat.getFlowA());
            Log.d(tag,"通道A"+v);
            if((v>2)&&(v<3)){
                content+="\n通道A流量电路正常"+v+"V;";
            }else{
                content+="\n通道A流量电路异常"+v+"V;";
            }
            v = DeviceDataFormat.calcMainBoardVoltage(ozoneDataFormat.getFlowB());
            Log.d(tag,"通道B"+v);
            if((v>2)&&(v<3)){
                content+="通道B流量电路正常"+v+"V;";
            }else{
                content+="通道B流量电路异常"+v+"V;";
            }

            v = DeviceDataFormat.calcPhotoVoltage(ozoneDataFormat.getPhotoA())/0.0408f;
            Log.d(tag,"A"+ozoneDataFormat.getPhotoA()+"光电管A"+v+"pA");
            if((v>70f)&&(v<120f)){
                content+="\n光电管A电路正常"+v+"pA";
            }else{
                content+="\n光电管A电路异常"+v+"pA";
            }

            v = DeviceDataFormat.calcPhotoVoltage(ozoneDataFormat.getPhotoB())/0.0408f;
            Log.d(tag,"B"+ozoneDataFormat.getPhotoB()+"光电管B"+v+"pA");
            if((v>70f)&&(v<120f)){
                content+="光电管B电路正常"+v+"pA";
            }else{
                content+="光电管B电路异常"+v+"pA";
            }

        }

        private void endStatus(){
            listener.notifyStatus("结束检测...");
            for(int i=0;i<4;i++){
                panel.setAnalogOut(i,1f);
            }
            ozoneControlPanel.setPwm(1);
            for(int i=1;i<=5;i++){
                ozoneControlPanel.setRelay(i,false);
            }
            content+="\n测试完成";
        }

        private void getIoModel(){
            listener.notifyStatus("检测数字输出...");
            boolean [] din = {false,false,false,false,false};
            ozoneControlPanel.inquire();
            panel.inquireStatus();
            delayS(2);
            //Log.d(tag,"getIoModel step 1"+content);
            for(int i=0;i<5;i++){
                if(dataFormat.getDin()[i]){
                    din[i] = true;
                    Log.d(tag,""+i+"error");
                    content += ""+i+"通道低电平异常;";
                }
                ozoneControlPanel.setRelay(i+1,true);
            }
            delayS(3);
            panel.inquireStatus();
            content +="\n";
            delayS(1);
            //Log.d(tag,"getIoModel step 2"+content);
            for(int i=0;i<5;i++){
                if(!dataFormat.getDin()[i]){
                    din[i] = true;
                    content += ""+i+"通道高电平异常;";
                }
                ozoneControlPanel.setRelay(i+1,false);
            }
            //Log.d(tag,"getIoModel step 3"+content);
            for(int i=0;i<5;i++){
                if(!din[i]){
                    content+="通道"+i+"正常;";
                }
            }
            content +="\n";
            //Log.d(tag,"getIoModel step 4"+content);
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
