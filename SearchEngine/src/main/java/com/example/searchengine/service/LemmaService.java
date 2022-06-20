package com.example.searchengine.service;

import com.example.searchengine.entity.Index;
import com.example.searchengine.entity.Lemma;
import com.example.searchengine.repository.LemmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class LemmaService {

    @Autowired
    LemmaRepository lemmaRepository;

    public void insertAll(ArrayList<Lemma> lemmaList) {
        lemmaRepository.saveAll(lemmaList);
    }

    public HashSet<Lemma> getOrderedLemmas(HashSet<String> wordLemmas) {
        List<Lemma> lemmaList = lemmaRepository.findAllByLemmaInOrderByFrequency(wordLemmas);
        HashSet<Lemma> lemmaHashSet = new HashSet<>(lemmaList);
        return lemmaHashSet;
    }

    public void deleteAll() {
        lemmaRepository.deleteAll();
    }

}
