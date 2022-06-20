package com.example.searchengine.service;

import com.example.searchengine.entity.Index;
import com.example.searchengine.entity.Lemma;
import com.example.searchengine.entity.Page;
import com.example.searchengine.repository.IndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Service
public class IndexService {

    @Autowired
    IndexRepository indexRepository;

    public void insertAll(ArrayList<Index> indexList) {
        indexRepository.saveAll(indexList);
    }

    public HashMap<Page, Double> getRankPages(HashSet<Lemma> orderedLemmas, ArrayList<Page> indexesPages) {

        List<Object[]> pagesWithRank = indexRepository.findPagesWithRank(orderedLemmas, indexesPages);
        HashMap<Page, Double> rankPages = new HashMap<Page, Double>();
        for (Object[] pageWithRank: pagesWithRank) {
            rankPages.put((Page) pageWithRank[0], (Double) pageWithRank[1]);
        }

        return rankPages;
    }

    public void deleteAll() {
        indexRepository.deleteAll();
    }

}
