package com.authserver.network.instance;

import com.authserver.network.AuthSocket;
import com.authserver.network.model.Client;

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

    private List<Client> _clients;

    private AuthSocketInstance()
    {
        AuthSocket _socket = new AuthSocket();
        _clients = new ArrayList<>();
    }

    public Client newClient(AsynchronousSocketChannel socketChannel)
    {
        Client client = new Client(socketChannel);
        _clients.add(client);
        return client;
    }

}
