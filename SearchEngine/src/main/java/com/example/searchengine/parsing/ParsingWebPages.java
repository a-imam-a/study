package com.example.searchengine.parsing;

import com.example.searchengine.entity.*;
import com.example.searchengine.entity.enums.IndexingStatus;
import com.example.searchengine.service.*;
import com.example.searchengine.text.Lemmatize;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.IOException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class ParsingWebPages {

    public void startParsingWebPages(String webSite, String siteName, SiteService siteService) {

        String rootWebSite = webSite.substring(0, webSite.length() -1);

        Site site = getSite(rootWebSite, siteName);
        siteService.insert(site);

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        RecursiveActionParsing recursiveParsing = new RecursiveActionParsing(webSite, site);
        forkJoinPool.invoke(recursiveParsing);
        site.setStatusTime(new Date());
        site.setStatus(IndexingStatus.INDEXED);
        siteService.insert(site);

       }


    private Site getSite(String url, String name) {
        Site site = new Site();
        site.setUrl(url);
        site.setName(name);
        site.setStatusTime(new Date());
        site.setStatus(IndexingStatus.INDEXING);
        return site;
    }

    private Page getPage(String rootPath, int responseCode, String content) {
        Page page = new Page();
        page.setPath(rootPath);
        page.setCode(responseCode);
        page.setContent(content);
        return page;
    }
    public boolean pathIsExist(String rootPath) {
        if (new PageService().pathIsExist(rootPath)) {
            //LOGGER.info(PAGES_HISTORY_MARKER, "page {} is exist in table", rootPath);
            return true;
        }
        return false;
    }

    public void responseProcessing(String rootPath, Response response, Site site) {

        int responseCode = response.statusCode();
        Page page = getPage(rootPath, responseCode, response.body());
        page.setSite(site);
        new PageService().insert(page);
        site.setStatusTime(new Date());
        new SiteService().insert(site);

        if (responseCode != 200) {
            return;
        }

        HashMap<String, Double> lemmasRank = new HashMap<>();
        ArrayList<Field> fields = new FieldService().getFields();
        fields.forEach(t->fieldProcessing(t, response, lemmasRank));
        addLemmasAndIndexes(site, page, lemmasRank);

    }

    private void addLemmasAndIndexes(Site site, Page page, HashMap<String, Double> lemmasRank) {
        ArrayList<Lemma> lemmaList = new ArrayList<>();
        ArrayList<Index> indexList = new ArrayList<>();
        lemmasRank.forEach((k, v) -> {
            Lemma lemma = getLemma(k, site);
            Index index = getIndex(page, lemma,  v);
        });
        new LemmaService().insertAll(lemmaList);
        new IndexService().insertAll(indexList);
    }

    private Lemma getLemma(String lemmaText, Site site) {
        Lemma lemma = new Lemma();
        lemma.setLemma(lemmaText);
        lemma.setFrequency(1);
        lemma.setSite(site);
        return lemma;
    }

    private Index getIndex(Page page, Lemma lemma, Double rank) {
        Index index = new Index();
        index.setPage(page);
        index.setLemma(lemma);
        index.setRank(rank);
        return index;
    }

    public Response getResponse(String page) {
        try {
            Thread.sleep(100);
            Response response = Jsoup.connect(page)
                    .userAgent("Mozilla/5.0 (Windows; U; " +
                            "WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .execute();
            return response;
        } catch (IOException | InterruptedException e) {
            //LOGGER.error(EXCEPTIONS_MARKER, page + ": " + e.getMessage());
            return null;
        }
    }

    public ArrayList<String> getPageLinksToProcessing(Response response) {

        ArrayList<String> pageLinks = pageLinksFromResponse(response);
        if (pageLinks.isEmpty()) {
            //LOGGER.info(PAGES_HISTORY_MARKER, "no links on page {}", response.url());
            return pageLinks;
        }

        ArrayList<String> pathsFromTablePage = new PageService().findExistingPaths(pageLinks);
        pathsFromTablePage.forEach(t -> pageLinks.remove(t));

        return pageLinks;

    }

    private ArrayList<String> pageLinksFromResponse(Response response) {

        ArrayList<String> pageLinks = new ArrayList<String>();
        try {
            Document doc = response.parse();
            pageLinks = (ArrayList<String>) doc.select("a[href]").stream()
                    .map(t -> t.attr("href"))
                    .filter(url -> url.matches( "^[/][^.#].*[^\\.jpg]$"))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            //LOGGER.error(EXCEPTIONS_MARKER, response.url() + ": " + e.getMessage());
        }
        return pageLinks;

    }

    private void fieldProcessing(Field field, Response response, HashMap<String, Double> lemmasRank) {

        try {
            Document doc = response.parse();
            String text = doc.select(field.getSelector()).text();
            HashMap<String, Integer> wordLemmas = Lemmatize.getWordsLemmasWithQty(text);
            for (Map.Entry<String, Integer> entry: wordLemmas.entrySet()) {
                String lemma = entry.getKey();
                int count = entry.getValue();
                double currentRank = count * field.getWeight();
                double defaultRank = lemmasRank.getOrDefault(lemma, 0.0);
                lemmasRank.put(lemma, defaultRank + currentRank);
            }

        } catch (IOException e) {
            //LOGGER.error(EXCEPTIONS_MARKER, response.url() + ": " + e.getMessage());
        }
    }

}
