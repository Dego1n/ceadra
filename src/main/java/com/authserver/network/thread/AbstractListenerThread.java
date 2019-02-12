package com.authserver.network.thread;

import com.authserver.network.packet.AbstractSendablePacket;

public abstract class AbstractListenerThread {

    abstract void sendPacket(AbstractSendablePacket packet);

}
