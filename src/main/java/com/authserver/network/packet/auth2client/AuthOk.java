package com.authserver.network.packet.auth2client;

import com.authserver.network.packet.AbstractSendablePacket;
import com.authserver.network.packet.IServerPacket;

public class AuthOk extends AbstractSendablePacket implements IServerPacket {

    public AuthOk()
    {
        super();
        build();
    }

    @Override
    public void build() {
        writeH(0x02); //packetID
    }
}
