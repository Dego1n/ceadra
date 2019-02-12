package com.authserver.network.model;

import com.authserver.network.thread.GameServerListenerThread;

public class GameServer {

    private final GameServerListenerThread _listenerThread;
    private final String _address;
    private final short _port;
    private final String _name;

    public GameServer(GameServerListenerThread listenerThread, String address, short port, String name)
    {
        _listenerThread = listenerThread;
        _address = address;
        _port = port;
        _name = name;
        System.out.println("Added new server to server list. Name: "+_name+" Address:"+_address+" Port:"+port);
    }

    public GameServerListenerThread getListenerThread()
    {
        return _listenerThread;
    }

    public String getAddress() {
        return _address;
    }

    public short getPort() {
        return _port;
    }

    public String getName() {
        return _name;
    }
}
