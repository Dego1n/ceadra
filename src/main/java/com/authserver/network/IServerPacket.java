package com.authserver.network;

public interface IServerPacket {
    byte[] getData();
    void build();

}
