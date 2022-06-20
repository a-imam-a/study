package searchengine.parsing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import searchengine.entity.*;
import searchengine.service.FieldService;
import searchengine.service.IndexService;
import searchengine.service.LemmaService;
import searchengine.service.PageService;
import searchengine.text.Lemmatize;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;

public class RecursiveActionParsing extends RecursiveAction {

    private static final Logger LOGGER = LogManager.getLogger(ParsingWebPages.class);
    private static final Marker PAGES_HISTORY_MARKER = MarkerManager.getMarker("PAGES_HISTORY");
    private static final Marker EXCEPTIONS_MARKER = MarkerManager.getMarker("EXCEPTIONS");

    private static String webPage;
    private static Site site;

    public RecursiveActionParsing(String webPage, Site site) {
        this.webPage = webPage;
        this.site = site;
    }

    @Override
    protected void compute() {

        String rootPath = webPage.replace(site.getUrl(), "");

        if (pathIsExist(rootPath)) return;

        Response response = getResponse(webPage);

        if (response == null) return;

        responseProcessing(rootPath, response);

        HashSet<String> pageLinks = getPageLinksToProcessing(response);
        for (String pageLink: pageLinks) {
            invokeAll(new RecursiveActionParsing(site.getUrl() + pageLink, site));
        }

    }

    private boolean pathIsExist(String rootPath) {
        if (new PageService().pathIsExist(rootPath)) {
            LOGGER.info(PAGES_HISTORY_MARKER, "page {} is exist in table", rootPath);
            return true;
        }
        return false;
    }

    private void responseProcessing(String rootPath, Response response) {

        int responseCode = response.statusCode();
        Page page = getPage(rootPath, responseCode, response.body());
        new PageService().insert(page, site);

        if (responseCode != 200) {
            return;
        }

        HashMap<String, Double> lemmasRank = new HashMap<>();
        HashSet<Field> fields = new FieldService().getFields();
        fields.forEach(t->fieldProcessing(t, response, lemmasRank));
        addLemmasAndIndexes(site, page, lemmasRank);
    }

    private Page getPage(String rootPath, int responseCode, String content) {
        Page page = new Page();
        page.setPath(rootPath);
        page.setCode(responseCode);
        page.setContent(content);
        return page;
    }

    private Response getResponse(String page) {
        try {
            Thread.sleep(100);
            Response response = Jsoup.connect(page)
                    .userAgent("Mozilla/5.0 (Windows; U; " +
                            "WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .execute();
            return response;
        } catch (IOException | InterruptedException e) {
            LOGGER.error(EXCEPTIONS_MARKER, page + ": " + e.getMessage());
            return null;
        }
    }

    private HashSet<String> getPageLinksToProcessing(Response response) {
        HashSet<String> pageLinks = pageLinksFromResponse(response);
        if (pageLinks.isEmpty()) {
            LOGGER.info(PAGES_HISTORY_MARKER, "no links on page {}", response.url());
            return pageLinks;
        }

        ArrayList<String> pathsFromTablePage = new PageService().findExistingPaths(pageLinks);
        pathsFromTablePage.forEach(t -> pageLinks.remove(t));

        return pageLinks;
    }

    private void addLemmasAndIndexes(Site site, Page page, HashMap<String, Double> lemmasRank) {

        LemmaService lemmaService = new LemmaService();
        IndexService indexService = new IndexService();
        lemmaService.openSessionWithTransaction();
        indexService.openSessionWithTransaction();
        lemmasRank.forEach((k, v) -> {
            Lemma lemma = getLemma(k);
            lemmaService.insertWithoutCommit(lemma, site);
            Index index = getIndex(page, lemma,  v);
            indexService.insertWithoutCommit(index);
        });
        lemmaService.closeSessionWithTransaction();
        indexService.closeSessionWithTransaction();

    }

    private Lemma getLemma(String lemmaText) {
        Lemma lemma = new Lemma();
        lemma.setLemma(lemmaText);
        lemma.setFrequency(1);
        return lemma;
    }

    private Index getIndex(Page page, Lemma lemma, Double rank) {
        Index index = new Index();
        index.setPage(page);
        index.setLemma(lemma);
        index.setRank(rank);
        return index;
    }

    private HashSet<String> pageLinksFromResponse(Response response) {

        HashSet<String> pageLinks = new HashSet<String>();
        try {
            Document doc = response.parse();
            pageLinks = (HashSet<String>) doc.select("a[href]").stream()
                    .map(t -> t.attr("href"))
                    .filter(url -> url.matches( "^[/][^.#].*[^\\.jpg]$"))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            LOGGER.error(EXCEPTIONS_MARKER, response.url() + ": " + e.getMessage());
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
            LOGGER.error(EXCEPTIONS_MARKER, response.url() + ": " + e.getMessage());
        }
    }

}
