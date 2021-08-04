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
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ClientListenerThread extends AbstractListenerThread {

    private static final Logger log = LoggerFactory.getLogger(ClientListenerThread.class);

    private final AsynchronousSocketChannel _socketChannel;

    private int sessionId;

    private Account _account;

    private GameServer _gameServer;

    private int _gameSessionKey;

    private boolean writeIsPending = false;

    private List<AbstractSendablePacket> packetBuffer;

    private SecureRandom random;

    public ClientListenerThread(AsynchronousSocketChannel socketChannel)
    {
        _socketChannel = socketChannel;
        packetBuffer = new ArrayList<>();
        random = new SecureRandom();
    }

    public void sendPacket(AbstractSendablePacket packet)
    {
        if(writeIsPending)
            packetBuffer.add(packet);
        else {
            writeIsPending = true;
            _socketChannel.write(ByteBuffer.wrap(packet.prepareAndGetData()), this, new CompletionHandler<Integer, ClientListenerThread>() {
                @Override
                public void completed(Integer result, ClientListenerThread thread) {
                    thread.writeIsPending = false;
                    if(packetBuffer.size() > 0)
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
            while( _socketChannel.isOpen())
            {
                // Выделаем память 2 байта в байтбаффер для размера пакета
                ByteBuffer byteBuffer = ByteBuffer.allocate( 2 );

                // Читаем размер пакета
                _socketChannel.read( byteBuffer ).get( 3, TimeUnit.MINUTES );

                //Конвертим байтбаффер в массив байтов
                byte[] bytePacketSize =  byteBuffer.array();

                //Конвертим массив байтов в шорт и получаем длинну пакета
                short size = (short)(((bytePacketSize[1] & 0xFF) << 8) | (bytePacketSize[0] & 0xFF));

                //Выделяем память под пакет нужного размера - 2 байта (размер мы уже получили)
                byteBuffer = ByteBuffer.allocate(size - 2);

                //Читаем пакет
                _socketChannel.read(byteBuffer).get(20, TimeUnit.SECONDS);

                //Передаем пакет Хендлеру
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
            if( _socketChannel.isOpen() )
            {
                _socketChannel.close();
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

    public void Auth(int sessionId, String username, String password)
    {
        if(this.sessionId == sessionId)
        {
            AccountDao accountDao = new AccountDao();

            Account account = accountDao.getAccountByUsername(username);
            if(account != null && account.getPassword().equals(password))
            {
                //ух... удачно авторизировались, записываем аккаунт в тред и отправляем AuthOk
                _account = account;
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
            //TODO не валидный ключ сессии, что то нужно отдать как ошибку, скорее всего пакет AuthFailed
            closeConnection();
        }
    }

    public void SendServerList(int sessionId)
    {
        if(this.sessionId == sessionId)
        {
            List<GameServer> gameServers = GameServerSocketInstance.getInstance().getGameServerList();
            sendPacket(new ServerList(gameServers));
        }
        else
        {
            //TODO не валидный ключ сессии, что то нужно отдать как ошибку, скорее всего пакет AuthFailed
            closeConnection();
        }
    }

    private void closeConnection()
    {
        try {
            _socketChannel.close();
        } catch (IOException e) {
            log.error("Failed closing connection", e);
        }
    }

    public void ServerLogin(int session_key, int server_id) {
        if(this.sessionId == session_key)
        {
            _gameServer = GameServerSocketInstance.getInstance().getGameServerList().get(server_id);
            if(_gameServer == null)
            {
                //TODO: GameServerAuthFail packet
                closeConnection();
            }

            _gameSessionKey = random.nextInt();
            sendPacket(new GameServerAuthOk(_gameSessionKey));
        }
        else
        {
            //TODO не валидный ключ сессии, что то нужно отдать как ошибку, скорее всего пакет AuthFailed
            closeConnection();
        }
    }
}


