package com.authserver.network.serverpackets;

public class ConnectionAccepted extends AbstractSendablePacket{

    public ConnectionAccepted()
    {
        super();
    }

    @Override
    public void build() {
        writeH(0x01); //PacketID
        writeD(22869); //SessionID
    }
}
