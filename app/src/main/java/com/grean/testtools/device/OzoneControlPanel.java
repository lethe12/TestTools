package com.grean.testtools.device;

public interface OzoneControlPanel {
    int STATUS_INQUIRE =0,STATUS_OTHER =1;
    void setDebugMode();
    void setInitMode();
    void inquire();
    void setPwm(int value);
    void setRelay(int channel,boolean key);
}
