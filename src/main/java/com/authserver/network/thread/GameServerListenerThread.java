package com.authserver.network.thread;

import com.authserver.network.instance.GameServerSocketInstance;
import com.authserver.network.packet.AbstractSendablePacket;
import com.authserver.network.packet.auth2game.Ping;
import com.authserver.network.packet.game2auth.RequestRegisterGameServer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GameServerListenerThread extends AbstractListenerThread{

    private final AsynchronousSocketChannel _socketChannel;

    public GameServerListenerThread(AsynchronousSocketChannel socketChanel)
    {
        _socketChannel = socketChanel;
        packetBuffer = new ArrayList();
    }

    private boolean writeIsPending = false;

    private List<AbstractSendablePacket> packetBuffer;

    public void sendPacket(AbstractSendablePacket packet)
    {
        if(writeIsPending)
            packetBuffer.add(packet);
        else {
            writeIsPending = true;
        _socketChannel.write(ByteBuffer.wrap(packet.prepareAndGetData()), this, new CompletionHandler<Integer, GameServerListenerThread>() {
            @Override
            public void completed(Integer result, GameServerListenerThread thread) {
                thread.writeIsPending = false;
                if(packetBuffer.size() > 0)
                {
                    thread.sendPacket(packetBuffer.get(0));
                }
            }

            @Override
            public void failed(Throwable exc, GameServerListenerThread thread) {
                //TODO: close connection?
                thread.writeIsPending = false;
            }
        });
    }
    }

    public void receivableStream()
    {
        Timer timerPing = new Timer();
        timerPing.schedule(new GameServerPing(this), 0, 60000);
        try
        {


            while( _socketChannel.isOpen())
            {
                // Выделаем память 2 байта в байтбаффер для размера пакета
                ByteBuffer byteBuffer = ByteBuffer.allocate( 2 );

                    // Читаем размер пакета
                    int bytesRead = _socketChannel.read(byteBuffer).get(2, TimeUnit.MINUTES);

                    //Конвертим байтбаффер в массив байтов
                    byte[] bytePacketSize = byteBuffer.array();

                    //Конвертим массив байтов в шорт и получаем длинну пакета
                    short size = (short) (((bytePacketSize[1] & 0xFF) << 8) | (bytePacketSize[0] & 0xFF));

                    //Выделяем память под пакет нужного размера - 2 байта (размер мы уже получили)
                    byteBuffer = ByteBuffer.allocate(size - 2);

                    //Читаем пакет
                    _socketChannel.read(byteBuffer).get(30, TimeUnit.SECONDS);

                //Передаем пакет Хендлеру
                GameServerPacketHandler.handlePacket(this,byteBuffer.array());
            }
        }
        catch (InterruptedException | ExecutionException e)
        {

        }
        catch (TimeoutException e)
        {
        }

        timerPing.cancel();
        GameServerSocketInstance.getInstance().removeGameServerByListenerThread(this);

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
}
class GameServerPacketHandler {

    private static final short REQUEST_REGISTER_GAME_SERVER = 0x01;
    static void handlePacket(GameServerListenerThread authServer, byte [] packet)
    {
        short packetID = (short)(((packet[1] & 0xFF) << 8) | (packet[0] & 0xFF));

        switch(packetID)
        {
            case REQUEST_REGISTER_GAME_SERVER:
                new RequestRegisterGameServer(authServer,packet);
                break;
        }
    }
}

class GameServerPing extends TimerTask {

    private GameServerListenerThread _thread;

    public GameServerPing(GameServerListenerThread thread)
    {
        _thread = thread;
    }

    @Override
    public void run() {
        _thread.sendPacket(new Ping());
    }
}


