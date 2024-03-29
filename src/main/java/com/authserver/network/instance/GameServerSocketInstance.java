package com.authserver.network.instance;

import com.authserver.network.GameServerListenerSocket;
import com.authserver.network.model.GameServer;
import com.authserver.network.packet.auth2game.ConnectionAccepted;
import com.authserver.network.thread.GameServerListenerThread;

import java.util.ArrayList;
import java.util.List;

public class GameServerSocketInstance {

    private static GameServerSocketInstance instance;

    public static GameServerSocketInstance getInstance()
    {
        if(instance == null)
            instance = new GameServerSocketInstance();

        return instance;
    }

    private List<GameServer> gameServerList;

    private GameServerSocketInstance()
    {
        gameServerList = new ArrayList<>();
        new GameServerListenerSocket();
    }

    public void addGameServer(GameServer server)
    {
        gameServerList.add(server);
        server.getListenerThread().sendPacket(new ConnectionAccepted());
    }

    public void removeGameServerByListenerThread(GameServerListenerThread listenerThread)
    {
        for(GameServer gs : gameServerList)
        {
            if(gs.getListenerThread() == listenerThread)
            {
                gameServerList.remove(gs);
                break;
            }
        }
    }

    public List<GameServer> getGameServerList()
    {
        return gameServerList;
    }
}
