package com.authserver.network.packet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public abstract class AbstractSendablePacket implements IServerPacket {

    private static final Logger log = LoggerFactory.getLogger(AbstractSendablePacket.class);

    private final ByteArrayOutputStream byteArrayOutputStream;

    protected AbstractSendablePacket()
    {
        byteArrayOutputStream = new ByteArrayOutputStream();
    }

    protected void writeD(int value)
    {
        byteArrayOutputStream.write(value & 0xff);
        byteArrayOutputStream.write((value >> 8) & 0xff);
        byteArrayOutputStream.write((value >> 16) & 0xff);
        byteArrayOutputStream.write((value >> 24) & 0xff);
    }

    protected void writeH(int value)
    {
        byteArrayOutputStream.write(value & 0xff);
        byteArrayOutputStream.write((value >> 8) & 0xff);
    }

    protected void writeF(double org)
    {
        long value = Double.doubleToRawLongBits(org);
        byteArrayOutputStream.write((int) (value & 0xff));
        byteArrayOutputStream.write((int) ((value >> 8) & 0xff));
        byteArrayOutputStream.write((int) ((value >> 16) & 0xff));
        byteArrayOutputStream.write((int) ((value >> 24) & 0xff));
        byteArrayOutputStream.write((int) ((value >> 32) & 0xff));
        byteArrayOutputStream.write((int) ((value >> 40) & 0xff));
        byteArrayOutputStream.write((int) ((value >> 48) & 0xff));
        byteArrayOutputStream.write((int) ((value >> 56) & 0xff));
    }

    protected void writeS(String text)
    {
        try
        {
            if (text != null)
            {
                byteArrayOutputStream.write(text.getBytes(StandardCharsets.UTF_8));
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
        }

        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(0);
    }

    protected void writeQ(long value)
    {
        byteArrayOutputStream.write((int) (value & 0xff));
        byteArrayOutputStream.write((int) ((value >> 8) & 0xff));
        byteArrayOutputStream.write((int) ((value >> 16) & 0xff));
        byteArrayOutputStream.write((int) ((value >> 24) & 0xff));
        byteArrayOutputStream.write((int) ((value >> 32) & 0xff));
        byteArrayOutputStream.write((int) ((value >> 40) & 0xff));
        byteArrayOutputStream.write((int) ((value >> 48) & 0xff));
        byteArrayOutputStream.write((int) ((value >> 56) & 0xff));
    }

    public byte[] prepareAndGetData()
    {
        short packetLength = (short)(byteArrayOutputStream.size() + 2);
        ByteArrayOutputStream finalPacket = new ByteArrayOutputStream();
        finalPacket.write(packetLength & 0xff);
        finalPacket.write((packetLength >> 8) & 0xff);
        finalPacket.write(byteArrayOutputStream.toByteArray(),0,packetLength - 2);

        return finalPacket.toByteArray();
    }
}
