package com.authserver.network.packet.client2auth;

import com.authserver.network.packet.AbstractReceivablePacket;
import com.authserver.network.thread.ClientListenerThread;

public class RequestServerLogin extends AbstractReceivablePacket {

    private final ClientListenerThread clientListenerThread;

    public RequestServerLogin(ClientListenerThread clientListenerThread, byte[] packet)
    {
        super(clientListenerThread, packet);
        this.clientListenerThread = clientListenerThread;
        handle();
    }

    private void handle() {
        int sessionKey = readD();
        int serverId = readD();
        clientListenerThread.serverLogin(sessionKey,serverId);
    }
}
