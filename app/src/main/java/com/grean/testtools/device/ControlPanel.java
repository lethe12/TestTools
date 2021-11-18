package com.grean.testtools.device;

public interface ControlPanel {
    static final byte ADDRESS_IO = 0x01,ADDRESS_ANALOG_IN = 0x03,ADDRESS_ANALOG_OUT = 0x02;
    int STATUS_INQUIRE =0,STATUS_SET_ANALOG_OUT=1,STATUS_SET_RELAY_OUT=2,STATUS_INQUIRE_RELAY_IN=3,STATUS_INQUIRE_ANALOG_IN =4;
    /**
     * 查询各个模块状态
     */
    void inquireStatus();
    void setAnalogOut(int channel,float value);
    void setRelayOut(int channel,boolean key);
}
