package com.authserver.network.thread;

import com.authserver.network.instance.GameServerSocketInstance;
import com.authserver.network.packet.AbstractSendablePacket;
import com.authserver.network.packet.auth2game.Ping;
import com.authserver.network.packet.game2auth.RequestRegisterGameServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GameServerListenerThread extends AbstractListenerThread{

    private static final Logger log = LoggerFactory.getLogger(GameServerListenerThread.class);

    private final AsynchronousSocketChannel socketChannel;

    public GameServerListenerThread(AsynchronousSocketChannel socketChanel)
    {
        this.socketChannel = socketChanel;
        packetBuffer = new ArrayList<>();
    }

    private boolean writeIsPending = false;

    private List<AbstractSendablePacket> packetBuffer;

    public void sendPacket(AbstractSendablePacket packet)
    {
        if(writeIsPending)
            packetBuffer.add(packet);
        else {
            writeIsPending = true;
        socketChannel.write(ByteBuffer.wrap(packet.prepareAndGetData()), this, new CompletionHandler<Integer, GameServerListenerThread>() {
            @Override
            public void completed(Integer result, GameServerListenerThread thread) {
                thread.writeIsPending = false;
                if(!packetBuffer.isEmpty())
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


            while( socketChannel.isOpen())
            {
                // Выделаем память 2 байта в байтбаффер для размера пакета
                ByteBuffer byteBuffer = ByteBuffer.allocate( 2 );

                    // Читаем размер пакета
                    socketChannel.read(byteBuffer).get(2, TimeUnit.MINUTES);

                    //Конвертим байтбаффер в массив байтов
                    byte[] bytePacketSize = byteBuffer.array();

                    //Конвертим массив байтов в шорт и получаем длинну пакета
                    short size = (short) (((bytePacketSize[1] & 0xFF) << 8) | (bytePacketSize[0] & 0xFF));

                    //Выделяем память под пакет нужного размера - 2 байта (размер мы уже получили)
                    byteBuffer = ByteBuffer.allocate(size - 2);

                    //Читаем пакет
                    socketChannel.read(byteBuffer).get(30, TimeUnit.SECONDS);

                //Передаем пакет Хендлеру
                GameServerPacketHandler.handlePacket(this,byteBuffer.array());
            }
        }
        catch (InterruptedException | ExecutionException | TimeoutException e)
        {
            log.error("", e);
            Thread.currentThread().interrupt();
        }

        timerPing.cancel();
        GameServerSocketInstance.getInstance().removeGameServerByListenerThread(this);

        try
        {
            // Close the connection if we need to
            if( socketChannel.isOpen() )
            {
                socketChannel.close();
            }
        }
        catch (IOException e)
        {
            log.error("Failed to close connection in game server listener thread", e);
        }
    }
}
class GameServerPacketHandler {
    private static final Logger log = LoggerFactory.getLogger(GameServerPacketHandler.class);

    private static final short REQUEST_REGISTER_GAME_SERVER = 0x01;
    private GameServerPacketHandler() {

    }
    static void handlePacket(GameServerListenerThread authServer, byte [] packet)
    {
        short packetID = (short)(((packet[1] & 0xFF) << 8) | (packet[0] & 0xFF));

        switch(packetID)
        {
            case REQUEST_REGISTER_GAME_SERVER:
                new RequestRegisterGameServer(authServer,packet);
                break;
            default:
                log.warn("Received unknown packet ID: {}", packetID);
                break;
        }
    }
}

class GameServerPing extends TimerTask {

    private GameServerListenerThread thread;

    public GameServerPing(GameServerListenerThread thread)
    {
        this.thread = thread;
    }

    @Override
    public void run() {
        thread.sendPacket(new Ping());
    }
}


