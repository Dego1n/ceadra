package com.authserver.network;


import com.authserver.network.clientpackets.RequestConnect;
import com.authserver.network.model.Client;

public class ClientPackets {

    public static void HandlePacket(Client client, byte [] packet)
    {
        short packetID = (short)(((packet[1] & 0xFF) << 8) | (packet[0] & 0xFF));
        switch (packetID)
        {
            case 0x01:
                new RequestConnect(client,packet);
                break;
        }
    }
}
