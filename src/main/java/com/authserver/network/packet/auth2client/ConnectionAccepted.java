package com.authserver.network.packet.auth2client;

import com.authserver.network.packet.AbstractSendablePacket;

public class ConnectionAccepted extends AbstractSendablePacket {

    private final int sessionId;
    public ConnectionAccepted(int sessionId)
    {
        super();
        this.sessionId = sessionId;
        build();
    }

    public void build() {
        writeH(0x01); //PacketID
        writeD(sessionId); //SessionID
    }
}
