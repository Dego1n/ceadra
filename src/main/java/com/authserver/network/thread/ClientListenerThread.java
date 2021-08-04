package com.authserver.network.thread;

import com.authserver.database.dao.account.AccountDao;
import com.authserver.database.entity.account.Account;
import com.authserver.network.instance.GameServerSocketInstance;
import com.authserver.network.model.GameServer;
import com.authserver.network.packet.AbstractSendablePacket;
import com.authserver.network.packet.ClientPackets;
import com.authserver.network.packet.auth2client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ClientListenerThread extends AbstractListenerThread {

    private static final Logger log = LoggerFactory.getLogger(ClientListenerThread.class);

    private final AsynchronousSocketChannel socketChannel;

    private int sessionId;

    private boolean writeIsPending = false;

    private List<AbstractSendablePacket> packetBuffer;

    private final SecureRandom random;

    public ClientListenerThread(AsynchronousSocketChannel socketChannel)
    {
        this.socketChannel = socketChannel;
        packetBuffer = new ArrayList<>();
        random = new SecureRandom();
    }

    public void sendPacket(AbstractSendablePacket packet)
    {
        if(writeIsPending)
            packetBuffer.add(packet);
        else {
            writeIsPending = true;
            socketChannel.write(ByteBuffer.wrap(packet.prepareAndGetData()), this, new CompletionHandler<Integer, ClientListenerThread>() {
                @Override
                public void completed(Integer result, ClientListenerThread thread) {
                    thread.writeIsPending = false;
                    if(!packetBuffer.isEmpty())
                    {
                        thread.sendPacket(packetBuffer.get(0));
                    }
                }

                @Override
                public void failed(Throwable exc, ClientListenerThread thread) {
                    //TODO: close connection?
                    thread.writeIsPending = false;
                }
            });
        }
    }

    public void receivableStream()
    {
        try
        {
            while( socketChannel.isOpen())
            {
                // Allocating 2 bytes into bytebuffer for Packet Size
                ByteBuffer byteBuffer = ByteBuffer.allocate( 2 );

                // Reading packet size
                socketChannel.read( byteBuffer ).get( 3, TimeUnit.MINUTES );

                // Converting bytebuffer into byte array
                byte[] bytePacketSize =  byteBuffer.array();

                // Converting byte array into short and getting size of the packet
                short size = (short)(((bytePacketSize[1] & 0xFF) << 8) | (bytePacketSize[0] & 0xFF));

                // Allocating memory based on packet size without 2 bytes (it was packet size and we already got it)
                byteBuffer = ByteBuffer.allocate(size - 2);

                // Reading the packet
                socketChannel.read(byteBuffer).get(20, TimeUnit.SECONDS);

                // Passing packet to handler
                ClientPackets.HandlePacket(this,byteBuffer.array());
            }
        }
        catch (InterruptedException | ExecutionException e)
        {
            log.error("ReceivableStream", e);
            Thread.currentThread().interrupt();
        }
        catch (TimeoutException e)
        {
            log.warn("ReceivableStream (Timeout)", e);
            Thread.currentThread().interrupt();
        }

        try
        {
            // Close the connection if we need to
            if( socketChannel.isOpen() )
            {
                socketChannel.close();
            }
        }
        catch (IOException e)
        {
            log.warn("Failed to close connection on receivable stream", e);
        }
    }

    public void onProtocolVersionReceived(short protocolVersion)
    {
        if(protocolVersion != 0x02) //TODO move this constant
        {
            sendPacket(new ConnectionFailed(ConnectionFailed.WRONG_PROTOCOL));
            closeConnection();
        }
        short protocolVersion1 = protocolVersion;
        sessionId = random.nextInt();
        sendPacket(new ConnectionAccepted(sessionId));
    }

    public void auth(int sessionId, String username, String password)
    {
        if(this.sessionId == sessionId)
        {
            AccountDao accountDao = new AccountDao();

            Account account = accountDao.getAccountByUsername(username);
            if(account != null && account.getPassword().equals(password))
            {
                // Well... we authed successfully, writing user account into the thread and sending AuthOk packet
                sendPacket(new AuthOk());
            }
            else
            {
                sendPacket(new AuthFailed(AuthFailed.INVALID_CREDENTIALS));
                closeConnection();
            }
        }
        else
        {
            // TODO: not valid session key, we need something to reply as an error, maybe AuthFailed packet?
            closeConnection();
        }
    }

    public void sendServerList(int sessionId)
    {
        if(this.sessionId == sessionId)
        {
            List<GameServer> gameServers = GameServerSocketInstance.getInstance().getGameServerList();
            sendPacket(new ServerList(gameServers));
        }
        else
        {
            // TODO: not valid session key, we need something to reply as an error, maybe AuthFailed packet?
            closeConnection();
        }
    }

    private void closeConnection()
    {
        try {
            socketChannel.close();
        } catch (IOException e) {
            log.error("Failed closing connection", e);
        }
    }

    public void serverLogin(int sessionKey, int serverId) {
        if(this.sessionId == sessionKey)
        {
            GameServer gameServer = GameServerSocketInstance.getInstance().getGameServerList().get(serverId);
            if(gameServer == null)
            {
                //TODO: GameServerAuthFail packet
                closeConnection();
            }

            int gameSessionKey = random.nextInt();
            sendPacket(new GameServerAuthOk(gameSessionKey));
        }
        else
        {
            // TODO: not valid session key, we need something to reply as an error, maybe AuthFailed packet?
            closeConnection();
        }
    }
}


