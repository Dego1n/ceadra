package com.authserver.network.packet.auth2client;

import com.authserver.network.packet.AbstractSendablePacket;
import com.authserver.network.packet.IServerPacket;

public class ConnectionFailed extends AbstractSendablePacket implements IServerPacket {

    public static final short WRONG_PROTOCOL = 0x00;

    private short reason;

    public ConnectionFailed(short reason)
    {
        super();

        this.reason = reason;

        build();
    }
    @Override
    public void build() {
        writeH(0x04);
        writeH(reason);
    }
}
