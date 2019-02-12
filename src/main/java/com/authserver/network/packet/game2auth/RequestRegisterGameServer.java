package com.authserver.network.packet.game2auth;

import com.authserver.network.instance.GameServerSocketInstance;
import com.authserver.network.model.GameServer;
import com.authserver.network.packet.AbstractReceivablePacket;
import com.authserver.network.thread.GameServerListenerThread;

public class RequestRegisterGameServer extends AbstractReceivablePacket {

    private final GameServerListenerThread _gameServerListenerThread;
    public RequestRegisterGameServer(GameServerListenerThread listenerThread, byte[] packet) {
        super(listenerThread, packet);
        _gameServerListenerThread = listenerThread;
        handle();
    }

    @Override
    protected void handle() {

        int server_key = readD();
        String address = readS();
        short port = readH();
        String server_name = readS();

        //TODO: Check server key
        GameServerSocketInstance.getInstance().addGameServer(new GameServer(_gameServerListenerThread, address,port,server_name));
    }
}
