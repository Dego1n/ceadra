package com.authserver.database.presets;

import com.authserver.database.dao.account.AccountDao;
import com.authserver.database.entity.account.Account;

public class AccountPresets {

    public static void Load()
    {
        AccountDao accountDao = new AccountDao();

        if(accountDao.getAccountByUsername("playtest") == null)
        {
            System.out.println("Creating account playtest");
            Account account = new Account();
            account.setUsername("playtest");
            account.setPassword("awesome");

            accountDao.save(account);
        }
        else
        {
            System.out.println("Account playtest already exists, skipping");
        }
    }

}
