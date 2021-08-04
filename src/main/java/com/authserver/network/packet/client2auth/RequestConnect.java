package com.authserver.network.packet.client2auth;

import com.authserver.network.packet.AbstractReceivablePacket;
import com.authserver.network.thread.ClientListenerThread;

public class RequestConnect extends AbstractReceivablePacket {

    private final ClientListenerThread clientlistenerthread;
    public RequestConnect(ClientListenerThread clientListenerThread, byte[] packet)
    {
        super(clientListenerThread, packet);
        clientlistenerthread = clientListenerThread;
        handle();
    }

    private void handle() {
        short protocolVersion = readH();
        clientlistenerthread.onProtocolVersionReceived(protocolVersion);
    }

}
