package com.authserver.network.packet.game2auth;

import com.authserver.network.instance.GameServerSocketInstance;
import com.authserver.network.model.GameServer;
import com.authserver.network.packet.AbstractReceivablePacket;
import com.authserver.network.thread.GameServerListenerThread;

public class RequestRegisterGameServer extends AbstractReceivablePacket {

    private final GameServerListenerThread gameServerListenerThread;
    public RequestRegisterGameServer(GameServerListenerThread listenerThread, byte[] packet) {
        super(listenerThread, packet);
        gameServerListenerThread = listenerThread;
        handle();
    }

    private void handle() {

        int serverKey = readD();
        String address = readS();
        short port = readH();
        String serverName = readS();

        //TODO: Check server key
        GameServerSocketInstance.getInstance().addGameServer(new GameServer(gameServerListenerThread, address, port, serverName));
    }
}
