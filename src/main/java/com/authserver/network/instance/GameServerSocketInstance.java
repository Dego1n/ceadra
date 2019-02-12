package com.authserver.network.instance;

import com.authserver.network.GameServerListenerSocket;
import com.authserver.network.model.GameServer;
import com.authserver.network.packet.auth2game.ConnectionAccepted;

import java.util.ArrayList;
import java.util.List;

public class GameServerSocketInstance {

    private static GameServerSocketInstance _instance;

    public static GameServerSocketInstance getInstance()
    {
        if(_instance == null)
            _instance = new GameServerSocketInstance();

        return _instance;
    }

    private final GameServerListenerSocket listenerSocket;
    private List<GameServer> gameServerList;

    private GameServerSocketInstance()
    {
        gameServerList = new ArrayList<>();
        listenerSocket = new GameServerListenerSocket();
    }

    public void addGameServer(GameServer server)
    {
        gameServerList.add(server);
        server.getListenerThread().sendPacket(new ConnectionAccepted());
    }

    public List<GameServer> getGameServerList()
    {
        return gameServerList;
    }
}
