package searchengine.service;

import searchengine.entity.Site;

public class SiteService extends ModelService{

    @Override
    public void truncateTable() {
        openSessionWithTransaction();
        session.createSQLQuery("truncate table " + Site.class.getSimpleName()).executeUpdate();
        closeSessionWithTransaction();
    }

    public void insert(Site site) {
        openSessionWithTransaction();
        session.saveOrUpdate(site);
        closeSessionWithTransaction();
    }

}
