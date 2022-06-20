package com.example.searchengine.service;

import com.example.searchengine.entity.Lemma;
import com.example.searchengine.entity.Page;
import com.example.searchengine.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class PageService {

    @Autowired
    PageRepository pageRepository;

    public ArrayList<String> findExistingPaths(ArrayList<String> paths) {
        List<Page> pageList = pageRepository.findAllByPathIn(paths);
        ArrayList<String> pathsList = new ArrayList<>();
        pageList.forEach(page->pathsList.add(page.getPath()));
        return pathsList;
    }

    public boolean pathIsExist(String path) {
        return pageRepository.existsByPath(path);
    }

    public void insert(Page page) {
        pageRepository.save(page);
    }

    public ArrayList<Page> findByLemma(Lemma lemma) {
        List<Page> pageList = pageRepository.findAllByLemmaList(lemma);
        ArrayList<Page> pageArrayList = new ArrayList<>(pageList);
        return pageArrayList;
    }

    public ArrayList<Page> findByLemmaOnPages(Lemma lemma, ArrayList<Page> indexesPages) {
        List<Page> pageList = pageRepository.findAllByLemmaListAndPathIn(lemma, indexesPages);
        ArrayList<Page> pageArrayList = new ArrayList<>(pageList);
        return pageArrayList;
    }

    public void deleteAll() {
        pageRepository.deleteAll();
    }

    public long count() {
        return pageRepository.count();
    }
}
