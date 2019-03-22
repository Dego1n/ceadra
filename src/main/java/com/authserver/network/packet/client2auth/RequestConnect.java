package com.authserver.network.packet.client2auth;

import com.authserver.network.packet.AbstractReceivablePacket;
import com.authserver.network.thread.ClientListenerThread;

public class RequestConnect extends AbstractReceivablePacket {

    private final ClientListenerThread _clientListenerThread;
    public RequestConnect(ClientListenerThread clientListenerThread, byte[] packet)
    {
        super(clientListenerThread, packet);
        _clientListenerThread = clientListenerThread;
        handle();
    }

    private void handle() {
        short protocol_version = readH();
        _clientListenerThread.onProtocolVersionReceived(protocol_version);
    }

}
