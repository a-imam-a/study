package searchengine.utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {

        if (sessionFactory != null) {
            return sessionFactory;
        }
        synchronized (HibernateUtil.class) {
            if (sessionFactory == null) {
                createSessionFactory();
            }
        }
        return sessionFactory;
    }

    private static void createSessionFactory() {

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        sessionFactory = metadata.getSessionFactoryBuilder().build();

    }

    public static void closeSessionFactory() {

        if (sessionFactory.isOpen()) {
            sessionFactory.close();
        }
    }

}
