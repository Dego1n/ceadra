package com.ceadra.test.functional.packet.auth2client;

import com.authserver.network.packet.AbstractSendablePacket;
import com.authserver.network.packet.auth2client.ConnectionAccepted;
import com.ceadra.test.functional.packet.ReceivablePacket;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ConnectionAcceptedPacketTest {

    @Test
    void testConnectionAcceptedPacket() {
        SecureRandom random = new SecureRandom();
        int sessionId = random.nextInt();
        AbstractSendablePacket packet = new ConnectionAccepted(sessionId);
        byte[] bytes = packet.prepareAndGetData();
        ReceivablePacket received = new ReceivablePacket(bytes);
        assertEquals(0x01, received.readShort()); //Packet ID
        assertEquals(sessionId, received.readInt()); // Session ID
        assertFalse(received.hasMoreData());
    }
}
