package com.authserver.network.packet.auth2game;

import com.authserver.network.packet.AbstractSendablePacket;

public class ConnectionAccepted  extends AbstractSendablePacket {

    public ConnectionAccepted()
    {
        super();
        build();
    }
    public void build() {
        writeH(0x01); //packetID
        writeH(0x00); //0x00 - ConnectionAccepted
    }
}
