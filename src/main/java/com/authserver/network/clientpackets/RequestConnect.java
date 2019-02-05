package com.authserver.network.clientpackets;

import com.authserver.network.model.Client;

public class RequestConnect extends AbstractReceivablePacket {

    public RequestConnect(Client client, byte[] packet)
    {
        super(client, packet);
        handle();
    }

    @Override
    void handle() {
        short protocol_version = readH();
        System.out.println("Got protocol version: "+protocol_version);
        _client.onProtocolVersionReceived(protocol_version);
    }

}
