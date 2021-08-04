package com.ceadra.test.functional.packet.auth2client;

import com.authserver.network.packet.AbstractSendablePacket;
import com.authserver.network.packet.auth2client.ConnectionFailed;
import com.ceadra.test.functional.packet.ReceivablePacket;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ConnectionFailedPacketTest {

    @Test
    void testConnectionFailedPacket() {
        AbstractSendablePacket packet = new ConnectionFailed(ConnectionFailed.WRONG_PROTOCOL);
        byte[] bytes = packet.prepareAndGetData();
        ReceivablePacket received = new ReceivablePacket(bytes);
        assertEquals(0x04, received.readShort()); //Packet ID
        assertEquals(ConnectionFailed.WRONG_PROTOCOL, received.readShort()); // Reason
        assertFalse(received.hasMoreData());
    }
}
