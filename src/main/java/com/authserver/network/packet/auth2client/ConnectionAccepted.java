package com.authserver.network.packet.auth2client;

import com.authserver.network.packet.AbstractSendablePacket;

public class ConnectionAccepted extends AbstractSendablePacket {

    private int _session_id;
    public ConnectionAccepted(int session_id)
    {
        super();
        _session_id = session_id;
        build();
    }

    public void build() {
        writeH(0x01); //PacketID
        writeD(_session_id); //SessionID
    }
}
