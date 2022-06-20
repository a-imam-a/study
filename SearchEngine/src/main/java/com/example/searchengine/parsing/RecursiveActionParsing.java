package com.example.searchengine.parsing;


import com.example.searchengine.entity.Site;

import org.jsoup.Connection.Response;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.RecursiveAction;

public class RecursiveActionParsing extends RecursiveAction {

    private String webPage;
    private Site site;
    private ParsingWebPages parsingWebPages;
    public RecursiveActionParsing(String webPage, Site site) {
        this.webPage = webPage;
        this.site = site;
        parsingWebPages = new ParsingWebPages();
    }

    @Override
    protected void compute() {

        String rootPath = webPage.replace(site.getUrl(), "");

        if (pathIsExist(rootPath)) return;

        Response response = getResponse(webPage);

        if (response == null) return;

        responseProcessing(rootPath, response);

        ArrayList<String> pageLinks = getPageLinksToProcessing(response);
        for (String pageLink: pageLinks) {
            invokeAll(new RecursiveActionParsing(site.getUrl() + pageLink, site));
        }

    }

    private boolean pathIsExist(String rootPath) {
        return parsingWebPages.pathIsExist(rootPath);
    }

    private void responseProcessing(String rootPath, Response response) {
        parsingWebPages.responseProcessing(rootPath, response, site);
    }

    private Response getResponse(String page) {
        return parsingWebPages.getResponse(page);
    }

    private ArrayList<String> getPageLinksToProcessing(Response response) {
        return parsingWebPages.getPageLinksToProcessing(response);
    }

}
