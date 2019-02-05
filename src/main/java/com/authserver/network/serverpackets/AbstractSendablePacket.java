package com.authserver.network.serverpackets;

import com.authserver.network.IServerPacket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class AbstractSendablePacket implements IServerPacket {

    private final ByteArrayOutputStream _bao;

    private byte[] packet;

    protected AbstractSendablePacket()
    {
        _bao = new ByteArrayOutputStream();
        build();
        prepareToSend();
    }

    protected void writeD(int value)
    {
        _bao.write(value & 0xff);
        _bao.write((value >> 8) & 0xff);
        _bao.write((value >> 16) & 0xff);
        _bao.write((value >> 24) & 0xff);
    }

    protected void writeH(int value)
    {
        _bao.write(value & 0xff);
        _bao.write((value >> 8) & 0xff);
    }

//    protected void writeC(int value)
//    {
//        _bao.write(value & 0xff);
//    }

    protected void writeF(double org)
    {
        long value = Double.doubleToRawLongBits(org);
        _bao.write((int) (value & 0xff));
        _bao.write((int) ((value >> 8) & 0xff));
        _bao.write((int) ((value >> 16) & 0xff));
        _bao.write((int) ((value >> 24) & 0xff));
        _bao.write((int) ((value >> 32) & 0xff));
        _bao.write((int) ((value >> 40) & 0xff));
        _bao.write((int) ((value >> 48) & 0xff));
        _bao.write((int) ((value >> 56) & 0xff));
    }

    protected void writeS(String text)
    {
        try
        {
            if (text != null)
            {
                _bao.write(text.getBytes(StandardCharsets.UTF_16LE));
            }
        }
        catch (Exception e)
        {
            System.out.println(getClass().getSimpleName() + ": " + e.getMessage());
        }

        _bao.write(0);
        _bao.write(0);
    }

    protected void writeB(byte[] array)
    {
        try
        {
            _bao.write(array);
        }
        catch (IOException e)
        {
            System.out.println(getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    protected void writeQ(long value)
    {
        _bao.write((int) (value & 0xff));
        _bao.write((int) ((value >> 8) & 0xff));
        _bao.write((int) ((value >> 16) & 0xff));
        _bao.write((int) ((value >> 24) & 0xff));
        _bao.write((int) ((value >> 32) & 0xff));
        _bao.write((int) ((value >> 40) & 0xff));
        _bao.write((int) ((value >> 48) & 0xff));
        _bao.write((int) ((value >> 56) & 0xff));
    }

    public int getLength()
    {
        return _bao.size() + 2;
    }

    private void prepareToSend()
    {
        short packetLength = (short)(_bao.size() + 2);
        ByteArrayOutputStream finalPacket = new ByteArrayOutputStream();
        finalPacket.write(packetLength & 0xff);
        finalPacket.write((packetLength >> 8) & 0xff);
        finalPacket.write(_bao.toByteArray(),0,packetLength - 2);
        System.out.println("Packet size: "+ packetLength);
        packet = finalPacket.toByteArray();
    }
    abstract public void build();

    public byte[] getData()
    {
        return packet;
    }
}
