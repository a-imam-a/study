package searchengine.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import searchengine.utils.HibernateUtil;

public abstract class ModelService {

    Session session;
    Transaction transaction;

    public abstract void truncateTable();

    public void openSessionWithTransaction() {
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.beginTransaction();
    }

    public void openSession() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public void closeSessionWithTransaction() {
        transaction.commit();
        session.close();
    }

    public void closeSession() {
        session.close();
    }

}
