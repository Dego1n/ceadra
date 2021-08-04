package com.ceadra.test.functional.packet.auth2client;

import com.authserver.network.packet.AbstractSendablePacket;
import com.authserver.network.packet.auth2client.AuthFailed;
import com.ceadra.test.functional.packet.ReceivablePacket;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AuthFailedPacketTest {

    @Test
    void testAuthFailedPacket() {
        AbstractSendablePacket packet = new AuthFailed(AuthFailed.INVALID_CREDENTIALS);
        byte[] bytes = packet.prepareAndGetData();
        ReceivablePacket received = new ReceivablePacket(bytes);
        assertEquals(0x03, received.readShort()); //Packet ID
        assertEquals(AuthFailed.INVALID_CREDENTIALS, received.readShort()); // Failed reason
        assertFalse(received.hasMoreData());
    }
}
