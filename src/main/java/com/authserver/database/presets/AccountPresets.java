package com.authserver.database.presets;

import com.authserver.database.dao.account.AccountDao;
import com.authserver.database.entity.account.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountPresets {

    private static final Logger log = LoggerFactory.getLogger(AccountPresets.class);

    private AccountPresets() {

    }

    public static void Load()
    {
        AccountDao accountDao = new AccountDao();

        if(accountDao.getAccountByUsername("playtest") == null)
        {
            log.info("Creating account playtest");
            Account account = new Account();
            account.setUsername("playtest");
            account.setPassword("awesome");

            accountDao.save(account);
        }
        else
        {
            log.info("Account playtest already exists, skipping");
        }
    }

}
