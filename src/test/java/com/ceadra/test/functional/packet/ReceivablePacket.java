package com.ceadra.test.functional.packet;

import com.authserver.network.packet.AbstractReceivablePacket;

public class ReceivablePacket extends AbstractReceivablePacket {
    public ReceivablePacket(byte[] packet) {
        super(null, packet);
    }

    public int readInt() {
        return this.readD();
    }

    public short readShort() {
        return this.readH();
    }

    public String readString() {
        return this.readS();
    }

    public boolean hasMoreData() {
        return super.hasMoreData();
    }
}
