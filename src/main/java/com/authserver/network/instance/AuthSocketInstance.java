package com.authserver.network.instance;

import com.authserver.network.AuthSocket;
import com.authserver.network.thread.ClientListenerThread;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.List;

public class AuthSocketInstance {

    private static AuthSocketInstance _instance;

    public static AuthSocketInstance getInstance()
    {
        if(_instance == null)
            _instance = new AuthSocketInstance();

        return _instance;
    }

    private final List<ClientListenerThread> _clientListenerThreads;

    private AuthSocketInstance()
    {
        new AuthSocket();
        _clientListenerThreads = new ArrayList<>();
    }

    public ClientListenerThread newClient(AsynchronousSocketChannel socketChannel)
    {
        ClientListenerThread clientListenerThread = new ClientListenerThread(socketChannel);
        _clientListenerThreads.add(clientListenerThread);
        return clientListenerThread;
    }

}
