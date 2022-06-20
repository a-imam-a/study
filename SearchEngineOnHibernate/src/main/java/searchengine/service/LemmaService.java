package searchengine.service;

import searchengine.entity.Lemma;
import org.hibernate.query.Query;
import searchengine.entity.Site;

import java.util.HashSet;
import java.util.List;

public class LemmaService extends ModelService {

    public void insertWithoutCommit(Lemma lemma, Site site) {
        lemma.setSite(site);
        session.saveOrUpdate(lemma);
    }

    public HashSet<Lemma> getOrderedLemmas(HashSet<String> wordLemmas) {

        openSession();
        Query query = session.createQuery("from " + Lemma.class.getSimpleName() +
                " where lemma in :wordLemmas Order by frequency");
        query.setParameter("wordLemmas", wordLemmas);
        List<Lemma> orderedLemmas = query.list();
        closeSession();
        return new HashSet<>(orderedLemmas);
    }

    @Override
    public void truncateTable() {
        openSessionWithTransaction();
        session.createSQLQuery("truncate table " + Lemma.class.getSimpleName()).executeUpdate();
        closeSessionWithTransaction();
    }

}
