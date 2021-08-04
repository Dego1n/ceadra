package com.authserver.network.model;

import com.authserver.network.thread.GameServerListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameServer {

    private static final Logger log = LoggerFactory.getLogger(GameServer.class);

    private final GameServerListenerThread listenerThread;
    private final String address;
    private final short port;
    private final String name;

    public GameServer(GameServerListenerThread listenerThread, String address, short port, String name)
    {
        this.listenerThread = listenerThread;
        this.address = address;
        this.port = port;
        this.name = name;
        log.info("Added new server to server list. Name: {} Address: {} Port:{}", this.name, this.address, this.port);
    }

    public GameServerListenerThread getListenerThread()
    {
        return listenerThread;
    }

    public String getAddress() {
        return address;
    }

    public short getPort() {
        return port;
    }

    public String getName() {
        return name;
    }
}
