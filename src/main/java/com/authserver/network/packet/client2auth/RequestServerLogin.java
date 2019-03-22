package com.authserver.network.packet.client2auth;

import com.authserver.network.packet.AbstractReceivablePacket;
import com.authserver.network.thread.ClientListenerThread;

public class RequestServerLogin extends AbstractReceivablePacket {

    private final ClientListenerThread _clientListenerThread;

    public RequestServerLogin(ClientListenerThread clientListenerThread, byte[] packet)
    {
        super(clientListenerThread, packet);
        _clientListenerThread = clientListenerThread;
        handle();
    }

    private void handle() {
        int session_key = readD();
        int server_id = readD();
        _clientListenerThread.ServerLogin(session_key,server_id);
    }
}
