package searchengine.searching;

import searchengine.entity.Lemma;
import searchengine.entity.Page;
import searchengine.service.PageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import searchengine.service.LemmaService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import searchengine.text.Lemmatize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

public class TextSearch {

    private static final Logger LOGGER = LogManager.getLogger(TextSearch.class);
    private static final Marker EXCEPTIONS_MARKER = MarkerManager.getMarker("EXCEPTIONS");
    private static final Marker PAGES_HISTORY_MARKER = MarkerManager.getMarker("PAGES_HISTORY");

    public TreeSet<SearchedPage> foundPages(String queryString) {

        TreeSet<SearchedPage> foundPages = new TreeSet<>();
        try {
            HashSet<String> wordLemmas = Lemmatize.getWordsLemmas(queryString);
            HashSet<Lemma> orderedLemmas = new LemmaService().getOrderedLemmas(wordLemmas);
            if (wordLemmas.size() != orderedLemmas.size()) {
                LOGGER.info(PAGES_HISTORY_MARKER, "no found pages on query string {}", queryString);
                return foundPages;
            }
            ArrayList<Page> indexesPages = getIndexesPages(orderedLemmas);
            foundPages = getSearchedPages(orderedLemmas, indexesPages);
        }
        catch (IOException e) {
            LOGGER.error(EXCEPTIONS_MARKER, e.getMessage());
            return foundPages;
        }
        return foundPages;
    }

    private ArrayList<Page> getIndexesPages(HashSet<Lemma> orderedLemmas) {
        ArrayList<Page> indexesPages = new ArrayList<>();
        for (Lemma lemma: orderedLemmas) {
            if (indexesPages.isEmpty()) {
                indexesPages = new PageService().findByLemma(lemma);
            }
            else {
                indexesPages = new PageService().findByLemmaOnPages(lemma, indexesPages);
            }
            if (indexesPages.isEmpty()) {
                break;
            }
        }
        return indexesPages;
    }

    private TreeSet<SearchedPage> getSearchedPages(HashSet<Lemma> orderedLemmas, ArrayList<Page> indexesPages) {

        TreeSet<SearchedPage> searchedPages = new TreeSet<>();
        if (indexesPages.isEmpty()) {
            return searchedPages;
        }
        HashMap<Page, Double> rankPages = new PageService().getRankPages(orderedLemmas, indexesPages);
        rankPages.forEach((page, rank) -> {
            searchedPages.add(searchedPage(page, rank, orderedLemmas));
        });
        return searchedPages;
    }

    private SearchedPage searchedPage(Page page, Double rank, HashSet<Lemma> lemmas) {

        String content = page.getContent();
        Document doc = Jsoup.parse(content);
        String title = doc.select("head title").text();
        String snippet = getSnippet(doc, lemmas);

        SearchedPage searchedPage = new SearchedPage();
        searchedPage.setUri(page.getPath());
        searchedPage.setRelevance(rank);
        searchedPage.setTitle(title);
        searchedPage.setSnippet(snippet);

        return searchedPage;
    }

    private String getSnippet(Document doc, HashSet<Lemma> lemmas) {

        return "";
    }

}
