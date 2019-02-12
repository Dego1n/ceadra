package com.authserver.network.packet.client2auth;

import com.authserver.network.packet.AbstractReceivablePacket;
import com.authserver.network.thread.ClientListenerThread;

public class RequestConnect extends AbstractReceivablePacket {

    private final ClientListenerThread _clientListenerThread;
    public RequestConnect(ClientListenerThread clientListenerThread, byte[] packet)
    {
        super(clientListenerThread, packet);
        _clientListenerThread = clientListenerThread;
        handle(); //TODO: может вызывается само из абстрактного класса? нужно закомментировать и проверить
    }

    @Override
    protected void handle() {
        short protocol_version = readH();
        System.out.println("Got protocol version: "+protocol_version);
        _clientListenerThread.onProtocolVersionReceived(protocol_version);
    }

}
