package com.authserver.network.packet.auth2game;

import com.authserver.network.packet.AbstractSendablePacket;

public class Ping   extends AbstractSendablePacket {
    public Ping()
    {
        super();
        build();
    }
    public void build() {
        writeH(0x02); //packetID
    }
}
