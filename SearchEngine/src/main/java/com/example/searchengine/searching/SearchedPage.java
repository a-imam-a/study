package com.example.searchengine.searching;

import lombok.Data;

@Data
public class SearchedPage implements Comparable<SearchedPage>{

    private String uri;
    private String title;
    private String snippet;
    private double relevance;

    @Override
    public int compareTo(SearchedPage o) {
        return Double.compare(o.getRelevance(), relevance);
    }
}
