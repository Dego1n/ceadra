package com.authserver.network.packet.client2auth;

import com.authserver.network.packet.AbstractReceivablePacket;
import com.authserver.network.thread.ClientListenerThread;

public class RequestServerList extends AbstractReceivablePacket {

    private final ClientListenerThread _clientListenerThread;

    public RequestServerList(ClientListenerThread clientListenerThread, byte[] packet) {
        super(clientListenerThread, packet);
        _clientListenerThread = clientListenerThread;
        handle();
    }

    private void handle() {
        int session_key = readD();
        _clientListenerThread.SendServerList(session_key);
    }

}
