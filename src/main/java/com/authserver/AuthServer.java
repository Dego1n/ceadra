package com.authserver;

import com.authserver.config.Config;
import com.authserver.database.presets.AccountPresets;
import com.authserver.network.instance.AuthSocketInstance;
import com.authserver.network.instance.GameServerSocketInstance;

import java.util.Arrays;

class AuthServer {

    public static void main( String[] args )
    {
        System.out.println(Arrays.toString("1_username".getBytes()));
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
            e.printStackTrace();
        }
    }
}
