package com.authserver.network.packet;

import com.authserver.network.packet.client2auth.RequestAuth;
import com.authserver.network.packet.client2auth.RequestConnect;
import com.authserver.network.packet.client2auth.RequestServerList;
import com.authserver.network.packet.client2auth.RequestServerLogin;
import com.authserver.network.thread.ClientListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientPackets {
    private static final Logger log = LoggerFactory.getLogger(ClientPackets.class);

    private static final short REQUEST_CONNECT = 0x01;
    private static final short REQUEST_AUTH = 0x02;
    private static final short REQUEST_SERVER_LIST = 0x03;
    private static final short REQUEST_SERVER_LOGIN = 0x04;

    private ClientPackets() {

    }

    public static void handlePacket(ClientListenerThread clientListenerThread, byte[] packet) {
        short packetID = (short) (((packet[1] & 0xFF) << 8) | (packet[0] & 0xFF));
        switch (packetID) {
            case REQUEST_CONNECT:
                new RequestConnect(clientListenerThread, packet);
                break;
            case REQUEST_AUTH:
                new RequestAuth(clientListenerThread, packet);
                break;
            case REQUEST_SERVER_LIST:
                new RequestServerList(clientListenerThread, packet);
                break;
            case REQUEST_SERVER_LOGIN:
                new RequestServerLogin(clientListenerThread, packet);
                break;
            default:
                //TODO: Security audit ?
                log.warn("Client sending unknown packet id: {} ", packetID);
                break;
        }
    }
}
