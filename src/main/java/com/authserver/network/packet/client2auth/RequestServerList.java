package com.authserver.network.packet.client2auth;

import com.authserver.network.packet.AbstractReceivablePacket;
import com.authserver.network.thread.ClientListenerThread;

public class RequestServerList extends AbstractReceivablePacket {

    private final ClientListenerThread clientListenerThread;

    public RequestServerList(ClientListenerThread clientListenerThread, byte[] packet) {
        super(clientListenerThread, packet);
        this.clientListenerThread = clientListenerThread;
        handle();
    }

    private void handle() {
        int sessionKey = readD();
        clientListenerThread.sendServerList(sessionKey);
    }

}
