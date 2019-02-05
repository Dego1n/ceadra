package com.authserver.network.model;

import com.authserver.network.ClientPackets;
import com.authserver.network.serverpackets.AbstractSendablePacket;
import com.authserver.network.serverpackets.ConnectionAccepted;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Client {
    private AsynchronousSocketChannel _socketChannel;

    public short protocolVersion;

    public Client(AsynchronousSocketChannel socketChannel)
    {
        _socketChannel = socketChannel;
    }

    public void sendPacket(AbstractSendablePacket packet)
    {
        _socketChannel.write(ByteBuffer.wrap(packet.getData()));
    }

    public void receivableStream()
    {
        try
        {
            while( _socketChannel.isOpen())
            {
                // Выделаем память 2 байта в байтбаффер для размера пакета
                ByteBuffer byteBuffer = ByteBuffer.allocate( 2 );

                // Читаем размер пакета
                int bytesRead = _socketChannel.read( byteBuffer ).get( 3, TimeUnit.MINUTES );

                //Конвертим байтбаффер в массив байтов
                byte[] bytePacketSize =  byteBuffer.array();

                //Конвертим массив байтов в шорт и получаем длинну пакета
                short size = (short)(((bytePacketSize[1] & 0xFF) << 8) | (bytePacketSize[0] & 0xFF));

                //Выделяем память под пакет нужного размера - 2 байта (размер мы уже получили)
                byteBuffer = ByteBuffer.allocate(size - 2);

                //Читаем пакет
                _socketChannel.read(byteBuffer).get(20, TimeUnit.SECONDS);

                System.out.println(Arrays.toString(byteBuffer.array()));

                //Передаем пакет Хендлеру
                ClientPackets.HandlePacket(this,byteBuffer.array());
            }
        }
        catch (InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
        } catch (TimeoutException e)
        {
            // The user exceeded the 20 second timeout, so close the connection
            _socketChannel.write( ByteBuffer.wrap( "Good Bye\n".getBytes() ) );
            System.out.println( "Connection timed out, closing connection" );
        }

        System.out.println( "End of conversation" );
        try
        {
            // Close the connection if we need to
            if( _socketChannel.isOpen() )
            {
                _socketChannel.close();
            }
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
    }

    public void onProtocolVersionReceived(short protocolVersion)
    {
        this.protocolVersion = protocolVersion;
        sendPacket(new ConnectionAccepted());
    }
}
