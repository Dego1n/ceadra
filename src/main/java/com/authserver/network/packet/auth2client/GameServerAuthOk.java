package com.authserver.network.packet.auth2client;

import com.authserver.network.packet.AbstractSendablePacket;

public class GameServerAuthOk extends AbstractSendablePacket {

    private int _gameSessionKey;

    public GameServerAuthOk(int _gameSessionKey)
    {
        super();
        this._gameSessionKey = _gameSessionKey;
        build();
    }

    @Override
    public void build() {
        writeH(0x06); //packetID
        writeD(_gameSessionKey);
    }
}
