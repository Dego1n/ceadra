package com.authserver.network.packet.auth2client;

import com.authserver.network.packet.AbstractSendablePacket;
import com.authserver.network.packet.IServerPacket;

public class AuthFailed extends AbstractSendablePacket implements IServerPacket {

    public final static short SYSTEM_ERROR = 0x00;
    public final static short INVALID_CREDENTIALS = 0x01;
    public final static short ACCESS_DENIED = 0x02;
    public final static short ACCOUNT_ALREADY_IN_USE = 0x03;
    public final static short SERVICE_UNAVAILABLE = 0x04;

    private short reason;

    public AuthFailed(short reason)
    {
        super();

        this.reason = reason;

        build();
    }

    @Override
    public void build() {
        writeH(0x03);
        writeH(reason);
    }
}
