package com.grean.testtools.device;

public class DeviceManage {
    private static DeviceManage instance = new DeviceManage();
    private DeviceDataFormat dataFormat = new DeviceDataFormat();
    private OzoneDataFormat ozone = new OzoneDataFormat();
    private ControlPanel panel;
    private OzoneControlPanel ozoneControlPanel;
    public DeviceDataFormat getDataFormat() {
        return dataFormat;
    }

    public synchronized ControlPanel getPanel() {
        if(panel==null){
            panel = new Hardware(ComThree.getInstance(),dataFormat);
        }
        return panel;
    }

    public synchronized OzoneControlPanel getOzoneControlPanel(){
        if (ozoneControlPanel==null){
            ozoneControlPanel = new OzoneMainBoard(ComOne.getInstance(),ozone);
        }
        return ozoneControlPanel;
    }

    public static DeviceManage getInstance() {
        return instance;
    }
}
