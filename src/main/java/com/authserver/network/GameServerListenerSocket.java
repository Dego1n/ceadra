package com.authserver.network;

import com.authserver.network.thread.GameServerListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class GameServerListenerSocket {

    private static final Logger log = LoggerFactory.getLogger(GameServerListenerSocket.class);

    public GameServerListenerSocket()
    {
        try (            final AsynchronousServerSocketChannel listener =
                                 AsynchronousServerSocketChannel.open().bind(new InetSocketAddress("0.0.0.0",1234)))
        {
            log.info("Listening game servers on {}:{}","0.0.0.0",1234);
            // Callback for accept
            listener.accept( null, new CompletionHandler<AsynchronousSocketChannel,Void>() {

                @Override
                public void completed(AsynchronousSocketChannel ch, Void att)
                {
                    // Accepting connection
                    listener.accept( null, this );
                    GameServerListenerThread gameServer = new GameServerListenerThread(ch);
                    gameServer.receivableStream();
                }

                @Override
                public void failed(Throwable exc, Void att) {
                    //TODO
                }
            });
        }
        catch (IOException e)
        {
            log.error(GameServerListenerSocket.class.toString(), e);
        }
    }
}
