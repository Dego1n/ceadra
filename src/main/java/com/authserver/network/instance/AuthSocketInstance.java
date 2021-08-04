package com.authserver.network.instance;

import com.authserver.network.AuthSocket;
import com.authserver.network.thread.ClientListenerThread;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.List;

public class AuthSocketInstance {

    private static AuthSocketInstance instance;

    public static AuthSocketInstance getInstance()
    {
        if(instance == null)
            instance = new AuthSocketInstance();

        return instance;
    }

    private final List<ClientListenerThread> clientListenerThreads;

    private AuthSocketInstance()
    {
        new AuthSocket();
        clientListenerThreads = new ArrayList<>();
    }

    public ClientListenerThread newClient(AsynchronousSocketChannel socketChannel)
    {
        ClientListenerThread clientListenerThread = new ClientListenerThread(socketChannel);
        clientListenerThreads.add(clientListenerThread);
        return clientListenerThread;
    }

}
