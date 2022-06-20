package searchengine.service;

import searchengine.entity.Index;
import searchengine.entity.Lemma;
import searchengine.entity.Page;
import org.hibernate.query.Query;
import searchengine.entity.Site;

import java.util.*;

public class PageService extends ModelService {

    @Override
    public void truncateTable() {
        openSessionWithTransaction();
        session.createSQLQuery("truncate table " + Page.class.getSimpleName()).executeUpdate();
        closeSessionWithTransaction();
    }

    public ArrayList<String> findExistingPaths(HashSet<String> pageLinks) {
        ArrayList<String> findingPaths = new ArrayList<>();
        openSession();
        Query query = session.createQuery("from " + Page.class.getSimpleName() + " where path in :pageLinks");
        query.setParameter("pageLinks", pageLinks);
        List<Page> pages = query.list();
        pages.forEach(t->findingPaths.add(t.getPath()));
        closeSession();
        return findingPaths;
    }

    public boolean pathIsExist(String path) {
        openSession();
        Query query = session.createQuery("from Page where path = :path");
        query.setParameter("path", path);
        boolean pathIsExist = !(query.list().isEmpty());
        closeSession();
        return pathIsExist;
    }

    public void insert(Page page, Site site) {
        openSessionWithTransaction();
        page.setSite(site);
        site.setStatusTime(new Date());
        session.save(page);
        session.update(site);
        closeSessionWithTransaction();
    }

    public ArrayList<Page> findByLemma(Lemma lemma) {
        openSession();
        Query query = session.createQuery("select t.page from " + Index.class.getSimpleName() + " t where t.lemma = :lemma");
        query.setParameter("lemma", lemma);
        ArrayList<Page> indexList = (ArrayList<Page>) query.list();
        closeSession();
        return indexList;
    }

    public ArrayList<Page> findByLemmaOnPages(Lemma lemma, ArrayList<Page> indexesPages) {
        openSession();
        Query query = session.createQuery("select t.page from " + Index.class.getSimpleName() +" t where t.page in :indexesPages and t.lemma = :lemma");
        query.setParameter("lemma", lemma);
        query.setParameter("indexesPages", indexesPages);
        ArrayList<Page> indexList = (ArrayList<Page>) query.list();
        closeSession();
        return indexList;
    }

    public HashMap<Page, Double> getRankPages(HashSet<Lemma> orderedLemmas, ArrayList<Page> indexesPages) {

        String sql = "select t.page, sum(t.rank) as sum_rank from " +
                Index.class.getSimpleName() +
                " t where t.page in :indexesPages and t.lemma in :lemma" +
                " group by t.page order by sum(t.rank) desc";
        openSession();
        Query query = session.createQuery(sql);
        query.setParameter("lemma", orderedLemmas);
        query.setParameter("indexesPages", indexesPages);
        List<Object[]> indexList = query.list();
        closeSession();
        HashMap<Page, Double> rankPages = new HashMap<Page, Double>();
        for (Object[] index: indexList) {
            rankPages.put((Page) index[0], (Double) index[1]);
        }

        return rankPages;
    }

}
