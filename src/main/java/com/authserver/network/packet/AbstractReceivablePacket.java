package com.authserver.network.packet;

import com.authserver.network.thread.AbstractListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractReceivablePacket {

    private static final Logger log = LoggerFactory.getLogger(AbstractReceivablePacket.class);

    private final AbstractListenerThread listenerthread;
    private final byte[] packet;
    private int pointer;

    protected AbstractReceivablePacket(AbstractListenerThread listenerThread, byte[] packet)
    {
        listenerthread = listenerThread;
        this.packet = packet;
        this.pointer = 2; //skipping packet id
    }

    protected int readD()
    {
        int result = packet[pointer++] & 0xff;
        result |= (packet[pointer++] << 8) & 0xff00;
        result |= (packet[pointer++] << 0x10) & 0xff0000;
        result |= (packet[pointer++] << 0x18) & 0xff000000;
        return result;
    }

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
            int size = 0;
            for(int i = pointer; i < pointer + (packet.length - pointer); i++)
            {
                if(packet[i] == 0x00 && packet[i + 1] == 0x00)
                {
                    break;
                }
                size++;
            }
            result = new String(packet, pointer, size);
            pointer += size + 2;
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
        }
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
