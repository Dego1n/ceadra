package com.authserver.network;

import com.authserver.config.Config;
import com.authserver.network.instance.AuthSocketInstance;
import com.authserver.network.thread.ClientListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AuthSocket {
    private static final Logger log = LoggerFactory.getLogger(AuthSocket.class);
    public AuthSocket()
    {
        try
        {
            log.info("Listening clients on {}:{}", Config.AUTH_SOCKET_LISTEN_ADDRESS, Config.AUTH_SOCKET_LISTEN_PORT);
            // Создаем AsynchronousServerSocketChannel, адрес и порт слушателя достаем из конфига
            final AsynchronousServerSocketChannel listener =
                    AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(Config.AUTH_SOCKET_LISTEN_ADDRESS,Config.AUTH_SOCKET_LISTEN_PORT));

            // Делаем коллбек на accept
            listener.accept( null, new CompletionHandler<AsynchronousSocketChannel,Void>() {

                @Override
                public void completed(AsynchronousSocketChannel ch, Void att)
                {
                    // Принимаем соединение
                    listener.accept( null, this );
                    ///

                    ClientListenerThread clientListenerThread = AuthSocketInstance.getInstance().newClient(ch);
                    clientListenerThread.receivableStream();

                }

                @Override
                public void failed(Throwable exc, Void att) {
                    //TODO
                }
            });
        }
        catch (IOException e)
        {
            log.error(e.getMessage());
        }
    }
}
