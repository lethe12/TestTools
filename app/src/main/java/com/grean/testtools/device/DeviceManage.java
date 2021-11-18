package com.grean.testtools.device;

public class DeviceManage {
    private static DeviceManage instance = new DeviceManage();
    private DeviceDataFormat dataFormat = new DeviceDataFormat();
    private ControlPanel panel;

    public DeviceDataFormat getDataFormat() {
        return dataFormat;
    }

    public synchronized ControlPanel getPanel() {
        if(panel==null){
            panel = new Hardware(ComThree.getInstance(),dataFormat);
        }
        return panel;
    }

    public static DeviceManage getInstance() {
        return instance;
    }
}
