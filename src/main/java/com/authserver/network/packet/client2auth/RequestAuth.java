package com.authserver.network.packet.client2auth;

import com.authserver.network.packet.AbstractReceivablePacket;
import com.authserver.network.thread.ClientListenerThread;

public class RequestAuth extends AbstractReceivablePacket {


    private final ClientListenerThread _clientListenerThread;

    public RequestAuth(ClientListenerThread clientListenerThread, byte[] packet)
    {
        super(clientListenerThread, packet);
        _clientListenerThread = clientListenerThread;
        handle(); //TODO: может вызывается само из абстрактного класса? нужно закомментировать и проверить
    }

    @Override
    protected void handle() {

        int session_id = readD();
        String username = readS();
        String password = readS();

        System.out.println("Client trying to auth with this data: Session key:"+session_id + " Username: "+username+ " Password" + password);

        _clientListenerThread.Auth(session_id,username,password);
    }
}
