package com.authserver.network.packet;

import com.authserver.network.packet.client2auth.RequestAuth;
import com.authserver.network.packet.client2auth.RequestConnect;
import com.authserver.network.packet.client2auth.RequestServerList;
import com.authserver.network.packet.client2auth.RequestServerLogin;
import com.authserver.network.thread.ClientListenerThread;

public class ClientPackets {

    public static void HandlePacket(ClientListenerThread clientListenerThread, byte [] packet)
    {
        short packetID = (short)(((packet[1] & 0xFF) << 8) | (packet[0] & 0xFF));
        switch (packetID)
        {
            case 0x01:
                new RequestConnect(clientListenerThread,packet);
                break;
            case 0x02:
                new RequestAuth(clientListenerThread, packet);
                break;
            case 0x03:
                new RequestServerList(clientListenerThread, packet);
                break;
            case 0x04:
                new RequestServerLogin(clientListenerThread,packet);
                break;
        }
    }
}
