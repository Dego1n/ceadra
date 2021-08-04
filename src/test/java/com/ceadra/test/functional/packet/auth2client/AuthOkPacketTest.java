package com.ceadra.test.functional.packet.auth2client;

import com.authserver.network.packet.AbstractSendablePacket;

import com.authserver.network.packet.auth2client.AuthOk;
import com.ceadra.test.functional.packet.ReceivablePacket;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthOkPacketTest {

    @Test
    void testAuthOkPacket() {
        AbstractSendablePacket packet = new AuthOk();
        byte[] bytes = packet.prepareAndGetData();
        ReceivablePacket received = new ReceivablePacket(bytes);
        assertEquals(0x02, received.readShort()); //Packet ID
        assertFalse(received.hasMoreData());
    }
}
