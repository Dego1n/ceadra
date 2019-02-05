package com.authserver;

import com.authserver.config.Config;
import com.authserver.database.dao.account.AccountDao;
import com.authserver.network.instance.AuthSocketInstance;

public class AuthServer {

    public static void main( String[] args )
    {
        Config.Load();
        AccountDao accountDao = new AccountDao();
        System.out.println(accountDao.getAccounts().size());
        AuthSocketInstance.getInstance();
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
