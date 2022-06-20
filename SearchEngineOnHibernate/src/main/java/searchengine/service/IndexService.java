package searchengine.service;

import searchengine.entity.Index;


public class IndexService extends ModelService {

    public void insertWithoutCommit(Index index) {
        session.save(index);
    }

    @Override
    public void truncateTable() {
        openSessionWithTransaction();
        session.createSQLQuery("truncate table `index`").executeUpdate();
        closeSessionWithTransaction();
    }

}
