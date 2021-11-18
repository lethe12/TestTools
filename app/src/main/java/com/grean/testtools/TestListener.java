package com.grean.testtools;

public interface TestListener {
    void notifyStatus(String string);
    void notifyContent(String string);
    void setFunEnable(boolean enable);
}
