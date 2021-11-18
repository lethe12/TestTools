package com;

/**
 * Created by weifeng on 2021/4/8.
 */

public interface SerialCommunicationController {
    void send(byte[] buff,int state);
    void setComReceiveProtocol(ComReceiveProtocol comReceiveProtocol);
}
