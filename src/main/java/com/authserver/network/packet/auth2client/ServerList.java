package com.authserver.network.packet.auth2client;

import com.authserver.network.model.GameServer;
import com.authserver.network.packet.AbstractSendablePacket;
import com.authserver.network.packet.IServerPacket;

import java.util.List;

public class ServerList extends AbstractSendablePacket implements IServerPacket {

    private List<GameServer> gameServers;
    public ServerList(List<GameServer> gameServerList)
    {
        super();
        gameServers = gameServerList;
        build();
    }

    @Override
    public void build() {
        writeH(0x05);
        writeH(gameServers.size());
        int server_id = 0;
        for(GameServer server : gameServers)
        {
            writeH(server_id);
            writeS(server.getName());
//            writeS(server.getAddress()); //TODO:
            writeS("127.0.0.1");
            writeD(server.getPort());
            writeH(0x00); //TODO: Server status
            server_id++;
        }
    }
}
