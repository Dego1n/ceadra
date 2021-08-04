package com.authserver.network.packet.auth2client;

import com.authserver.network.packet.AbstractSendablePacket;

public class GameServerAuthOk extends AbstractSendablePacket {

    private int gameSessionKey;

    public GameServerAuthOk(int gameSessionKey)
    {
        super();
        this.gameSessionKey = gameSessionKey;
        build();
    }

    @Override
    public void build() {
        writeH(0x06); //packetID
        writeD(gameSessionKey);
    }
}
