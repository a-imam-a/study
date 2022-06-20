package searchengine.parsing;

import searchengine.entity.enums.IndexingStatus;
import searchengine.entity.*;
import searchengine.service.*;

import searchengine.utils.HibernateUtil;

import java.util.*;
import java.util.concurrent.ForkJoinPool;


public class ParsingWebPages implements Runnable{

    private String webSite;
    private String siteName;

    public ParsingWebPages(String webSite, String siteName) {
        this.webSite = webSite;
        this.siteName = siteName;
    }

    @Override
    public void run() {

        String rootWebSite = webSite.substring(0, webSite.length() -1);

        Site site = getSite(rootWebSite, siteName);
        SiteService siteService = new SiteService();
        siteService.insert(site);

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        RecursiveActionParsing recursiveParsing = new RecursiveActionParsing(webSite, site);
        forkJoinPool.invoke(recursiveParsing);
        site.setStatusTime(new Date());
        site.setStatus(IndexingStatus.INDEXED);
        siteService.insert(site);

        HibernateUtil.closeSessionFactory();
    }

    private Site getSite(String url, String name) {
        Site site = new Site();
        site.setUrl(url);
        site.setName(name);
        site.setStatusTime(new Date());
        site.setStatus(IndexingStatus.INDEXING);
        return site;
    }

}
