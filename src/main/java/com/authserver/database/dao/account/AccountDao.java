package com.authserver.database.dao.account;

import com.authserver.database.HibernateSessionFactory;
import com.authserver.database.entity.account.Account;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class AccountDao {
    public void save(Account account) {

        Transaction transaction = null;

        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {

            // start a transaction

            transaction = session.beginTransaction();

            // save the account object

            session.save(account);

            // commit transaction

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {

                transaction.rollback();

            }

            e.printStackTrace();

        }

    }

    public List< Account > getAccounts() {

        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {

            return session.createQuery("from Account", Account.class).list();

        }

    }
}
