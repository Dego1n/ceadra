package com.ceadra.test.functional.packet.auth2client;

import com.authserver.network.packet.AbstractSendablePacket;
import com.authserver.network.packet.auth2client.GameServerAuthOk;
import com.ceadra.test.functional.packet.ReceivablePacket;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GameServerAuthOkPacketTest {

    @Test
    void testConnectionFailedPacket() {
        SecureRandom random = new SecureRandom();
        int gameSessionId = random.nextInt();
        AbstractSendablePacket packet = new GameServerAuthOk(gameSessionId);
        byte[] bytes = packet.prepareAndGetData();
        ReceivablePacket received = new ReceivablePacket(bytes);
        assertEquals(0x06, received.readShort()); //Packet ID
        assertEquals(gameSessionId, received.readInt()); // Session ID
        assertFalse(received.hasMoreData());
    }
}
