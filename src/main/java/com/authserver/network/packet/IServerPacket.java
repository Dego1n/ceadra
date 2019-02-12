package com.authserver.network.packet;

public interface IServerPacket {
    byte[] prepareAndGetData();
    void build();

}
