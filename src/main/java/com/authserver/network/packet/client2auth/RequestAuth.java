package com.authserver.network.packet.client2auth;

import com.authserver.network.packet.AbstractReceivablePacket;
import com.authserver.network.thread.ClientListenerThread;

public class RequestAuth extends AbstractReceivablePacket {


    private final ClientListenerThread clientListenerThread;

    public RequestAuth(ClientListenerThread clientListenerThread, byte[] packet)
    {
        super(clientListenerThread, packet);
        this.clientListenerThread = clientListenerThread;
        handle();
    }

    private void handle() {

        int sessionId = readD();
        String username = readS();
        String password = readS();

        clientListenerThread.auth(sessionId,username,password);
    }
}
