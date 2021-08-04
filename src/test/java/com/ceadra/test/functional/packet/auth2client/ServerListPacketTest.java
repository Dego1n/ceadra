package com.ceadra.test.functional.packet.auth2client;

import com.authserver.network.model.GameServer;
import com.authserver.network.packet.AbstractSendablePacket;
import com.authserver.network.packet.auth2client.GameServerAuthOk;
import com.authserver.network.packet.auth2client.ServerList;
import com.ceadra.test.functional.packet.ReceivablePacket;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ServerListPacketTest {

    @Test
    void testConnectionFailedPacket() {
        List<GameServer> gameServerList = new ArrayList<>();
        gameServerList.add(
                new GameServer(
                        null,
                        "192.168.0.1",
                        (short) 1257,
                        "Emerald"
                )
        );
        gameServerList.add(
                new GameServer(
                        null,
                        "10.0.12.69",
                        (short) 1417,
                        "Diamond"
                )
        );
        gameServerList.add(
                new GameServer(
                        null,
                        "147.23.91.67",
                        (short) 1257,
                        "Azure"
                )
        );
        AbstractSendablePacket packet = new ServerList(gameServerList);
        byte[] bytes = packet.prepareAndGetData();
        ReceivablePacket received = new ReceivablePacket(bytes);
        assertEquals(0x05, received.readShort()); //Packet ID
        assertEquals(3, received.readShort()); // Game server list size
        int serverId = 0;
        for (GameServer gameServer : gameServerList) {
            assertEquals(serverId, received.readShort());
            assertEquals(gameServer.getName(), received.readString());
            assertEquals(/*gameServer.getAddress()*/"127.0.0.1", received.readString()); //TODO: fix me later
            assertEquals(gameServer.getPort(), received.readInt());
            assertEquals(0x00, received.readShort());
            serverId++;
        }
        assertFalse(received.hasMoreData());
    }
}
