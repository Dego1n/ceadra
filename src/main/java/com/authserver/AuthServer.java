package com.authserver;

import com.authserver.config.Config;
import com.authserver.database.presets.AccountPresets;
import com.authserver.network.instance.AuthSocketInstance;
import com.authserver.network.instance.GameServerSocketInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AuthServer {
    private static final Logger log = LoggerFactory.getLogger(AuthServer.class);

    public static void main( String[] args )
    {
        Config.Load();
        AccountPresets.Load();
        AuthSocketInstance.getInstance();
        GameServerSocketInstance.getInstance();
        try
        {
            Thread.sleep( 6000000 );
        }
        catch( Exception e )
        {
            log.error("",e);
            Thread.currentThread().interrupt();
        }
    }
}
