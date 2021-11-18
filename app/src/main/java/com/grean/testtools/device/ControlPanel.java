package com.grean.testtools.device;

public interface ControlPanel {
    static final byte ADDRESS_IO = 0x01,ADDRESS_ANALOG_IN = 0x02,ADDRESS_ANALOG_OUT = 0x03;
    /**
     * 查询各个模块状态
     */
    void inquireStatus();
    void setAnalogOut(int channel,float value);
    void setRelayOut(int channel,boolean key);
}
