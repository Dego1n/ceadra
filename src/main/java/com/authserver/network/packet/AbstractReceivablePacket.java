package com.authserver.network.packet;

import com.authserver.network.thread.AbstractListenerThread;

public abstract class AbstractReceivablePacket {

    private final AbstractListenerThread _listenerThread;
    private final byte[] packet;
    private int pointer;

    protected AbstractReceivablePacket(AbstractListenerThread listenerThread, byte[] packet)
    {
        _listenerThread = listenerThread;
        this.packet = packet;
        this.pointer = 2; //skipping packet id
    }

    protected abstract void handle();
    
    protected int readD()
    {
        int result = packet[pointer++] & 0xff;
        result |= (packet[pointer++] << 8) & 0xff00;
        result |= (packet[pointer++] << 0x10) & 0xff0000;
        result |= (packet[pointer++] << 0x18) & 0xff000000;
        return result;
    }

//    public int readC()
//    {
//        int result = packet[pointer++] & 0xff;
//        return result;
//    }

    protected short readH()
    {
        int result = packet[pointer++] & 0xff;
        result |= (packet[pointer++] << 8) & 0xff00;
        return (short)result;
    }

    public double readF()
    {
        long result = packet[pointer++] & 0xff;
        result |= (packet[pointer++] & 0xffL) << 8L;
        result |= (packet[pointer++] & 0xffL) << 16L;
        result |= (packet[pointer++] & 0xffL) << 24L;
        result |= (packet[pointer++] & 0xffL) << 32L;
        result |= (packet[pointer++] & 0xffL) << 40L;
        result |= (packet[pointer++] & 0xffL) << 48L;
        result |= (packet[pointer++] & 0xffL) << 56L;
        return Double.longBitsToDouble(result);
    }

    protected String readS()
    {
        String result = null;
        try
        {
            result = new String(packet, pointer, packet.length - pointer);
            result = result.substring(0, result.indexOf(0x00));
            pointer += (result.length()) + 2;
        }
        catch (Exception e)
        {
            System.out.println(getClass().getSimpleName() + ": " + e.getMessage());
        }

        return result;
    }

    public final byte[] readB(int length)
    {
        byte[] result = new byte[length];
        System.arraycopy(packet, pointer, result, 0, length);
        pointer += length;
        return result;
    }

    public long readQ()
    {
        long result = packet[pointer++] & 0xff;
        result |= (packet[pointer++] & 0xffL) << 8L;
        result |= (packet[pointer++] & 0xffL) << 16L;
        result |= (packet[pointer++] & 0xffL) << 24L;
        result |= (packet[pointer++] & 0xffL) << 32L;
        result |= (packet[pointer++] & 0xffL) << 40L;
        result |= (packet[pointer++] & 0xffL) << 48L;
        result |= (packet[pointer++] & 0xffL) << 56L;
        return result;
    }
}
