package searchengine.service;

import searchengine.entity.Field;
import org.hibernate.Session;
import searchengine.utils.HibernateUtil;

import java.util.HashSet;
import java.util.List;

public class FieldService {

    private static HashSet<Field> fields;

    public HashSet<Field> getFields() {

        if (fields != null) return fields;

        synchronized (FieldService.class) {
            Session session = HibernateUtil.getSessionFactory().openSession();
            List<Field> resultList = session.createQuery("from Field").getResultList();
            fields = new HashSet<>(resultList);
            session.close();
        }
        return fields;
    }
}
